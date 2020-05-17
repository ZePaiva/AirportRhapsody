package Rhapsody.server.proxies;

import Rhapsody.common.Packet;
import Rhapsody.common.RunParameters;
import Rhapsody.server.sharedRegions.GeneralRepository;

/**
 * Repository proxy data type
 */
public class GeneralRepositoryProxy implements SharedMemoryProxy {

    /**
     * Repository
     * 
     * @serialField repository
     */
    private final GeneralRepository repository;

    /**
     * Number of finished passengers
     * 
     * @serialField finishedPassengers
     */
    private int finishedPassengers;

    /**
     * RepositoryProxy Construtor method
     * 
     * @param repository
     */
    public GeneralRepositoryProxy(GeneralRepository repository) {
        this.repository = repository;
        this.finishedPassengers=0;
    }

    /**
     * Message processor
     * @param packet message from clients
     * @return reply to message
     */
    public Packet proccesPacket(Packet packet) {

        Packet pkt = new Packet();

        /**
        switch(pkt.getType()) {

        }
        */

        return packet;
    }

    public boolean hasSimEnded() {
        return this.finishedPassengers==RunParameters.N;
    }
}