package Rhapsody.server.proxies;

import java.security.Provider;

import Rhapsody.common.Luggage;
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

    /**
     * Proxy constructor
     * @param baggageCollection
     */
    public BaggageCollectionProxy(BaggageCollection baggageCollection) {
        this.baggageCollection=baggageCollection;
        this.finished=0;
    }

    /**
     * Proccess received message and generate reply
     */
    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();
        System.out.println("Got message of type " + pkt.getType());

        switch (pkt.getType()) {
            // porter is carrying a luggage to teh CB
            case PORTER_STORE_BAG_CB:
                provider.setEntityState(pkt.getState());
                baggageCollection.carryItToAppropriateStore(new Luggage(pkt.getInt1(), pkt.getBool1()? "FDT" : "TRT"));
                reply.setState(provider.getEntityState());
                break;
            // porter signals no more bags
            case PORTER_NO_MORE_BAGS:
                baggageCollection.noMoreBagsToCollect();
                reply.setState(provider.getEntityState());
                break;
            // passenger will try to collect a bag
            case PASSENGER_COLLECTING_BAG:
                provider.setEntityID(pkt.getId());
                provider.setEntityState(pkt.getState());
                reply.setInt1(baggageCollection.goCollectABag(pkt.getInt1()));
                reply.setState(provider.getEntityState());
                break;
            // case porter resetted
            case NEW_FLIGHT:
                baggageCollection.newFlight();
                break;
            case SIM_ENDED:
                this.finished++;
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
        return finished==RunParameters.N+1;
    }

}