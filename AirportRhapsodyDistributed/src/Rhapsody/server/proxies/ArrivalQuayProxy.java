package Rhapsody.server.proxies;

import javax.management.RuntimeOperationsException;

import Rhapsody.common.Message;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.ArrivalQuay;

/**
 * Arrival Quay proxy 
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalQuayProxy implements SharedMemoryProxy{
    
    /**
     * entity to proxy
     */
    private final ArrivalQuay arrivalQuay;
    
    /**
     * Simulation status
     */
    private int finished;

    /**
     * Proxy constructor
     * @param arrivalQuay
     */
    public ArrivalQuayProxy(ArrivalQuay arrivalQuay) {
        this.arrivalQuay=arrivalQuay;
        this.finished=0;
    }

    /**
     * Process message and generate reply
     */
	public Message proccesPacket(Message pkt) {
        
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();

        switch (pkt.getType()) {
            // in case a passenger arrives to the 
            case PASSENGERS_WAITING:
                provider.setEntityID(pkt.getId());
                arrivalQuay.takeABus();         // will sert in a blocking state
                reply.setState(provider.getEntityState());
                break;
            default:
                throw new RuntimeOperationsException(new RuntimeException("Wrong operation in message: " + pkt.getType()));
        }

        
        return null;
	}

    /**
     * Check simulation status
     */
	public boolean hasSimEnded() {
		// TODO Auto-generated method stub
		return false;
	}

}