package Rhapsody.server.proxies;

import Rhapsody.common.Message;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.ArrivalExit;

/**
 * Arrival exit proxy for the servers
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalExitProxy implements SharedMemoryProxy {

    /**
     * Arrival Exit
     * 
     * @serialField
     */
    private final ArrivalExit arrivalExit;

    /**
     * Simulation status
     * 
     * @serialField
     */
    private boolean finished;

    /**
     * Arrival Exit Proxy constructor
     * 
     * @param arrivalExit
     */
    public ArrivalExitProxy(ArrivalExit arrivalExit) {
        this.arrivalExit = arrivalExit;
        this.finished = false;
    }

    /**
     * Process the message packet and generates a reply
     * 
     * @param pkt message to be processsed
     * @return reply
     */
    public Message proccesPacket(Message pkt) {

        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();
        System.out.println("Got message of type " + pkt.getType());

        switch (pkt.getType()) {
            // in case it is a pssenger going home
            case PASSENGER_GOING_HOME:
                provider.setEntityID(pkt.getId());
                provider.setEntityState(pkt.getState());
                arrivalExit.synchBlocked();
                arrivalExit.goHome(pkt.getBool1()); // enters a blocking state
                reply.setState(provider.getEntityState());
                break;
            // in case it is departure entrance synching a new passenger
            case DEPARTURE_SYNCH:
                arrivalExit.synchBlocked();
                break;
            // in case it is departure entrance wanting to know how many are blocked @ exit
            case DEPARTURE_REQUEST_HOWMANY:
                reply.setInt1(arrivalExit.currentBlockedPassengers());
                // arrivalExit.synchBlocked();
                break;
            // in case it is departure entrance wanting to wake up all passengers
            case DEPARTURE_REQUEST_WAKEUP:
                arrivalExit.wakeCurrentBlockedPassengers();
                break;
            case SIM_ENDED:
                this.finished = true;
                break;
            default:
                throw new RuntimeException("Wrong operation in message: " + pkt.getType());
        }
        return reply;
    }

    /**
     * Checks if the simulation has ended, terminates when all flights have been
     * completed
     */
    public boolean hasSimEnded() {
        return this.finished;
    }
}