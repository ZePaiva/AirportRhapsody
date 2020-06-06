package Rhapsody.server.proxies;

import Rhapsody.common.Message;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.DepartureEntrance;

/**
 * Proxy for DTE
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureEntranceProxy implements SharedMemoryProxy{

    /**
     * Departure entrance entity
     */
    private DepartureEntrance departureEntrance;
    
    /**
     * Simulation status
     */
    private boolean finished;

    /**
     * Constructor for the proxy
     * 
     * @param departureEntrance
     */
    public DepartureEntranceProxy(DepartureEntrance departureEntrance) {
       this.departureEntrance=departureEntrance;
       this.finished=false; 
    }

    /**
     * Proccess message and generate reply
     */
    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();

        switch (pkt.getType()) {
            // passenger is preparing next flight
            case PASSENGER_NEXT_FLIGHT:
                provider.setEntityID(pkt.getId());
                departureEntrance.prepareNextLeg(pkt.getBool1());
                reply.setState(provider.getEntityState());
                break;
            // ATE synching blocked passengers
            case ATE_SYNCH:
                departureEntrance.synchBlocked();
                break;
            // ATE requesting how many are blocked
            case ATE_REQUEST_HOWMANY:
                reply.setInt1(departureEntrance.currentBlockedPassengers());
                break;
            // ATE requesting to wake up
            case ATE_REQUEST_WAKEUP:
                departureEntrance.wakeCurrentBlockedPassengers();
                break;
            case SIM_ENDED:
                this.finished=true;
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