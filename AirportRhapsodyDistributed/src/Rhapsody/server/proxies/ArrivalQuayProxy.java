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
    
    private final ArrivalQuay arrivalQuay;
    private int finished;

    public ArrivalQuayProxy(ArrivalQuay arrivalQuay) {
        this.arrivalQuay=arrivalQuay;
        this.finished=0;
    }

	@Override
	public Message proccesPacket(Message pkt) {
        
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();

        switch (pkt.getType()) {
            // in case a passenger arrives to the 
            case PASSENGERS_WAITING:
                break;
            default:
                throw new RuntimeOperationsException(new RuntimeException("Wrong operation in message: " + pkt.getType()));
        }

        
        return null;
	}

	@Override
	public boolean hasSimEnded() {
		// TODO Auto-generated method stub
		return false;
	}

}