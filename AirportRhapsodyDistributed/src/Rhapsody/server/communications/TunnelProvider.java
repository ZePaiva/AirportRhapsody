package Rhapsody.server.communications;

import Rhapsody.common.Packet;
import Rhapsody.common.States;
import Rhapsody.server.interfaces.BusDriverInterface;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.interfaces.PorterInterface;
import Rhapsody.server.proxies.SharedMemoryProxy;

/**
 * Provides the necessary channels to redirect the messages 
 * in order to be unwrapped and processed.
 * Impersonates the clients
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class TunnelProvider extends Thread implements PassengerInterface, 
                                        BusDriverInterface, PorterInterface {
    
    /**
     * Shared Memory Proxy Interface, generic to all proxies
     * 
     * @serialField sharedMemory
     */
    private SharedMemoryProxy sharedMemory;

    /**
     * Server communication channel
     * 
     * @serialField serverCom
     */
    private ServerCom serverCom;


    /**
     * Entity state
     * 
     * @serialField state
     */
    private States state;

    /**
     * Entity ID
     * 
     * @serialField entityID
     */
    private int entityID;

    /**
     * Current flight being simulated
     * 
     * @serialField flightID
     */
    private int flightID;

    /**
     * Amount of luggages the passenger posseses, -1 if it is not a passenger
     * 
     * @serialField luggages
     */
    private int luggages;

    /**
     * Tunnel Provider constructor method
     * 
     * @param sharedMemory shared memory corresponding to this provider
     * @param serverCom coms channel corresponding to the target
     */
    public TunnelProvider(SharedMemoryProxy sharedMemory, ServerCom serverCom) {
        this.sharedMemory = sharedMemory;
        this.serverCom = serverCom;
    }

    /**
     * Wait & send method for the shared memory
     */
    @Override
    public void run() {
        Packet rcv = (Packet) serverCom.readObject();
        Packet snd = sharedMemory.proccesPacket(rcv);
        serverCom.writeObject(snd);
        serverCom.close();
    }

    public States getEntityState(){
        return this.state;
    }

    public void setEntityState(States state) {
        this.state=state;
    }

    public int getEntityId() {
        return this.entityID;
    }

    public void setEntityID(int id) {
        this.entityID=id;
    }
}