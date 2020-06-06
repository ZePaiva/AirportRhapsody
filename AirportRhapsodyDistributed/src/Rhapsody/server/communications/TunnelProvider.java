package Rhapsody.server.communications;

import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.common.States;
import Rhapsody.server.interfaces.BusDriverInterface;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.interfaces.PorterInterface;
import Rhapsody.server.proxies.SharedMemoryProxy;

/**
 * Provides the necessary channels to redirect the messages in order to be
 * unwrapped and processed. Impersonates the clients
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class TunnelProvider extends Thread implements PassengerInterface, BusDriverInterface, PorterInterface {

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
     * Starting bags of the passenger
     * 
     * @serialField startingBags
     */
    private int[] startingBags;

    /**
     * Bus seats
     * 
     * @serialField seats
     */
    private int[] seats;

    /**
     * Situations array if it is a passenger
     * 0 is FDT 1 is TRT
     */
    private int[] situation;

    /**
     * The bag the porter is holding, null if not a porter
     * 
     * @serialField currentBag
     */
    private Luggage currentBag;

    /**
     * Tunnel Provider constructor method
     * 
     * @param sharedMemory shared memory corresponding to this provider
     * @param serverCom    coms channel corresponding to the target
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
        Message rcv = (Message) serverCom.readObject();
        if (rcv!=null) {
            Message snd = sharedMemory.proccesPacket(rcv);
            serverCom.writeObject(snd);
            serverCom.close();
        } else {
            System.out.println("Received no message");
        }
    }

    /**
     * Return the state of the current entity
     * 
     * @return entity state
     */
    public States getEntityState() {
        return this.state;
    }

    /**
     * Set current entity state
     * 
     * @param state entity state
     */
    public void setEntityState(States state) {
        this.state = state;
    }

    /**
     * Get entity ID
     * 
     * @return entity ID
     */
    @Override
    public int getEntityID() {
        return this.entityID;
    }

    /**
     * Set entity ID
     * 
     * @param id entity ID
     */
    public void setEntityID(int id) {
        this.entityID = id;
    }

    /**
     * Get current clight ID
     * 
     * @return current flight
     */
    public int getFlightID() {
        return this.flightID;
    }

    /**
     * Set the flight ID, useful for altering the flight ID mid-simulation
     * 
     * @param flightID new flight
     */
    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    /**
     * 7Get the amount of luggages the passenger has, or -1 if it is a
     * Porter/BusDriver entity
     * 
     * @return luggages
     */
    public int getLuggages() {
        return this.luggages;
    }

    /**
     * Sets the amount of luggages the entity has or -1 if it is a Porter/BusDriver
     * 
     * @param luggages entity luggages
     */
    public void setLuggages(int luggages) {
        this.luggages = luggages;
    }

    /**
     * Sets the seats in the bus
     * 
     * @param seats bus seats
     */
    public void setSeats(int[] seats) {
        this.seats = seats;

    }

    /**
     * Gets the seats in the bus
     * 
     * @return bus seats
     */
    public int[] getSeats() {
        return seats;
    }

    /**
     * Gets the current bags of the passenger
     * 
     * @return current bags
     */
    public int getCurrentBags() {
        return this.luggages;
    }

    /**
     * Sets the current luggages to the specified value
     * 
     * @param bags
     */
    public void setCurrentBags(int bags) {
        this.luggages = bags;
    }

    /**
     * gets the starting bags for the current flight
     * 
     * @return starting bags
     */
    public int getStartingBags() {
        return this.startingBags[this.flightID];
    }

    /**
     * Get current luggage the porter is holding
     * 
     * @return current porter bag
     */
    public Luggage getCurrentBag() {
        return this.currentBag;
    }

    /**
     * Sets the current luggage the porter is holding
     * 
     * @param bag
     */
    public void setCurrentBag(Luggage bag) {
        this.currentBag=bag;
    }

    @Override
    public String getSituation() {
        return this.situation[this.flightID]==0 ? "FDT" : "TRT";
    }
}