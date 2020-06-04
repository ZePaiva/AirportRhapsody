package Rhapsody.server.proxies;

import Rhapsody.common.Message;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.BaggageCollection;

/**
 * Luggage collection point proxy
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class BaggageCollectionProxy implements SharedMemoryProxy {

    /**
     * Luggage collection that we will proxy with the current class
     */
    private BaggageCollection baggageCollection;

    /**
     * Check finished status
     */
    private int finished;

    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();

        switch (pkt.getType()) {
            //
        }

        return reply;
    }

    public boolean hasSimEnded() {
        return finished==RunParameters.K;
    }

}