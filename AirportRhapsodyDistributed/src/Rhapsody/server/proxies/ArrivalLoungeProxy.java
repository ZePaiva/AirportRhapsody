package Rhapsody.server.proxies;

import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.ArrivalLounge;

/**
 * Arrival lounge proxy to process and reply to messages
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalLoungeProxy implements SharedMemoryProxy {

    /**
     * Entity that this proxy will handle
     */
    private ArrivalLounge arrivalLounge;

    /**
     * Amount of flights, used when checking if it has ended or not
     */
    private boolean finished;

    /**
     * Arrival lounge proxy constructor
     * 
     * @param arrivalLounge
     */
    public ArrivalLoungeProxy(ArrivalLounge arrivalLounge) {
        this.arrivalLounge = arrivalLounge;
        this.finished = false;
    }

    /**
     * Process message and generate reply
     */
    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();

        switch (pkt.getType()) {
            // Porter login (takeARest)
            case PORTER_WAITING:
                reply.setBool1(arrivalLounge.takeARest());
                reply.setState(provider.getEntityState());
                break;
            // passenger login (whatShouldIDo), must allow passengers to add starting bags
            case PASSENGER_ARRIVED:
                provider.setEntityID(pkt.getId());
                provider.setEntityState(pkt.getState());
                arrivalLounge.whatShouldIDo(provider.getEntityID()); //induces blocking state
                reply.setState(provider.getEntityState());
                break;
            // Porter collecting bags from plane hold, will parse to a boolean and a int instead of a luggage
            case PORTER_COLLECT_BAG:
                Luggage bag = arrivalLounge.tryToCollectABag();
                reply.setInt1(bag.getPassengerId());
                reply.setBool1(bag.getLuggageType().equals("FDT"));
                break;
            // in case the passenger is logging into the lounge for the first time, useful to register it's situations and starting bags on the plane
            case PASSENGER_IN:
                arrivalLounge.updateStartingBags(pkt.getId(), pkt.getIntArray1(), pkt.getIntArray2());
                break;
            // simulation has ended
            case SIM_ENDED:
                arrivalLounge.endOfWork();
                this.finished = true;
            default:
                throw new RuntimeException("Wrong operation in message: " + pkt.getType());
        }
        return reply;
    }

    /**
     * Check simulation status
     */
    public boolean hasSimEnded() {
        return this.finished;
    }
}