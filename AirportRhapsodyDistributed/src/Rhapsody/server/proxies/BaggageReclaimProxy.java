package Rhapsody.server.proxies;

import Rhapsody.common.Message;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.BaggageReclaim;

/**
 * Baggage reclaim office proxy
 * 
 * @author José Paiva
 * @author Andŕe Mourato
 */
public class BaggageReclaimProxy implements SharedMemoryProxy {

    /**
     * Baggage reclaim entity
     */
    private BaggageReclaim baggageReclaim;

    /**
     * Finishing counter
     */
    private boolean finished;

    /**
     * Constructor of the proxy
     * 
     * @param baggageReclaim
     */
    public BaggageReclaimProxy(BaggageReclaim baggageReclaim) {
        this.baggageReclaim = baggageReclaim;
        this.finished = false;
    }

    /**
     * Process message and generate reply
     */
    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();
        System.out.println("Got message of type " + pkt.getType());

        switch (pkt.getType()) {
            // passenger is reporting a loss bag
            case PASSENGER_COMPLAINT:
                provider.setEntityID(pkt.getId());
                baggageReclaim.reportMissingBags(pkt.getInt1());
                reply.setState(provider.getEntityState());
                break;
            // signaling simulation finish
            case SIM_ENDED:
                this.finished = true;
                break;
            default:
                throw new RuntimeException("Wrong operation in message: " + pkt.getType());
        }

        return reply;
    }

    /**
     * Check simulation status
     */
    public boolean hasSimEnded() {
        return finished;
    }

}