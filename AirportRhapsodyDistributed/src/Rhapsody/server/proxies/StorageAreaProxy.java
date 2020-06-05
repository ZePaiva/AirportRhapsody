package Rhapsody.server.proxies;

import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.StorageArea;

/**
 * Storage Area proxy
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class StorageAreaProxy implements SharedMemoryProxy {

    /**
     * Entity to proxy
     */
    private StorageArea storageArea;

    /**
     * Simulation status
     */
    private boolean finished;

    /**
     * Proxy construtctor
     * 
     * @param storageArea
     */
    public StorageAreaProxy(StorageArea storageArea) {
        this.storageArea=storageArea;
        this.finished=false;
    }

    /**
     * Process and reply method
     * 
     * @param pkt
     * 
     * @return reply
     */
    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();

        switch (pkt.getType()) {
            case PORTER_STORE_BAG_SR:
                storageArea.carryItToAppropriateStore(new Luggage(pkt.getInt1(), pkt.getBool1() ? "FDT" : "TRT"));
                reply.setState(provider.getEntityState());
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
     * Method to check simulation status
     * 
     * @return simStatus
     */
    public boolean hasSimEnded() {
        return this.finished;
    }
    
}