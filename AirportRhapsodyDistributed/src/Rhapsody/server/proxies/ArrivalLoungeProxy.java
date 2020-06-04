package Rhapsody.server.proxies;

import Rhapsody.common.Message;
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
    private int finished;

    /**
     * Arrival lounge proxy constructor
     * 
     * @param arrivalLounge
     */
    public ArrivalLoungeProxy(ArrivalLounge arrivalLounge) {
        this.arrivalLounge = arrivalLounge;
        this.finished = 0;
    }

    public Message proccesPacket(Message pkt) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasSimEnded() {
        // TODO Auto-generated method stub
        return false;
    }
}