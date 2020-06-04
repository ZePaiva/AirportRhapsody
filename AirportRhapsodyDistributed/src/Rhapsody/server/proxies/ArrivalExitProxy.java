package Rhapsody.server.proxies;

import javax.management.RuntimeOperationsException;

import Rhapsody.common.Message;
import Rhapsody.common.RunParameters;
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
    private int finished;

    /**
     * Arrival Exit Proxy constructor
     * @param arrivalExit
     */
    private ArrivalExitProxy(ArrivalExit arrivalExit) {
        this.arrivalExit = arrivalExit;
        this.finished = 0;
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

        switch (pkt.getType()) {
            // in case it is a pssenger going home
            case PASSENGER_GOING_HOME:
                break;
            // in case it is departure entrance synching a new passenger
            case DEPARTURE_SYNCH:
                break;
            // in case it is departure entrance wanting to know how many are blocked @ exit
            case DEPARTURE_REQUEST_HOWMANY:
                break;
            // in case it is departure entrance wanting to wake up all passengers
            case DEPARTURE_REQUEST_WAKEUP:
                break;
            
            default:
                throw new RuntimeOperationsException(new RuntimeException("Wrong operation in message: " + pkt.getType()));
        }

        
        return null;
    }

    /**
     * Checks if the simulation has ended, terminates when all flights have been completed
     */
    public boolean hasSimEnded() {
        return this.finished==RunParameters.K;
    }
}