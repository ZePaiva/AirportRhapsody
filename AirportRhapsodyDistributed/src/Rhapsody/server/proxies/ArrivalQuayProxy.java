package Rhapsody.server.proxies;

import java.util.Queue;
import java.util.Set;

import javax.management.RuntimeOperationsException;

import org.omg.CORBA.PRIVATE_MEMBER;

import Rhapsody.common.Message;
import Rhapsody.common.RunParameters;
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
            // in case a passenger arrives to the arrival quay 
            case PASSENGERS_WAITING:
                provider.setEntityID(pkt.getId());
                arrivalQuay.takeABus();         // will sert in a blocking state
                reply.setState(provider.getEntityState());
                break;
            // has bus and bus has seats
            case PASSENGER_INTO_BUS:
                provider.setEntityID(pkt.getId());
                arrivalQuay.enterTheBus();
                reply.setState(provider.getEntityState());
                break;
            // simulation has ended
            case SIM_ENDED:
                arrivalQuay.endOfWork();
                this.finished=RunParameters.K;
                break;
            // bus driver checking sim status
            case BD_HAS_ENDED:
                provider.setEntityState(pkt.getState());
                reply.setBool1(arrivalQuay.hasDaysWorkEnded());
                reply.setState(provider.getEntityState());
                break;
            // bus driver arrived
            case BD_ANNOUNCING_BOARDING:
                arrivalQuay.announcingBusBoarding();
                break;
            // bus driver arrives
            case BD_ARRIVING:
                provider.setEntityState(pkt.getState());
                arrivalQuay.parkTheBus();
                reply.setState(provider.getEntityState());
                break;
            // bus driver goes to DTQ
            case BD_DRIVING:
                provider.setEntityState(pkt.getState());
                reply.setIntArray1(arrivalQuay.goToDepartureTerminal());
                reply.setState(provider.getEntityState());
                break;
            default:
                throw new RuntimeException("Wrong operation in message: " + pkt.getType());
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