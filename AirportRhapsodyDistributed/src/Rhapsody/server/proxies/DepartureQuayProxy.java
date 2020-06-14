package Rhapsody.server.proxies;

import java.util.LinkedList;
import java.util.Queue;

import Rhapsody.common.Message;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.DepartureQuay;

/**
 * Proxy for the departure quay
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureQuayProxy implements SharedMemoryProxy {

    /**
     * Entity to proxy
     */
    private DepartureQuay departureQuay;

    /**
     * Simulation status
     */
    private boolean finished;

    /**
     * Constructor method
     * 
     * @param departureQuay
     */
    public DepartureQuayProxy(DepartureQuay departureQuay) {
        this.finished = false;
        this.departureQuay = departureQuay;
    }

    /**
     * Processmessages and reply
     */
    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();
        System.out.println("Got message of type " + pkt.getType());

        switch (pkt.getType()) {
            // passenger is leaving the bus
            case PASSENGER_EXITING_BUS:
                provider.setEntityID(pkt.getId());
                departureQuay.leaveTheBus();
                reply.setState(provider.getEntityState());
                break;
            // bus driver is parking the bus
            case BD_PARKING:
                Queue<Integer> q = new LinkedList<>();
                for (int i : pkt.getIntArray1()) {
                    System.out.println(i);
                    q.add(i);
                }
                departureQuay.parkTheBusAndLetPassOff(q);
                reply.setState(provider.getEntityState());
                break;
            // bus driver is starting travel to ATQ
            case BD_DRIVING:
                departureQuay.goToArrivalTerminal();
                reply.setState(provider.getEntityState());
                break;
            // simulation ended
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