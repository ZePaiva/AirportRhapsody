package Rhapsody.server.proxies;

import Rhapsody.common.Packet;

/**
 * Generic interface for all the shared memories
 */
public interface SharedMemoryProxy {
    
    /**
     * Generic method to proccess a packet and generate a reply
     * 
     * @param pkt received message
     * @return reply
     */
    public Packet proccesPacket(Packet pkt);

    /**
     * Generic method to check if the simulation has ended
     * 
     * @return has simulation ended
     */
    public boolean hasSimEnded();
}