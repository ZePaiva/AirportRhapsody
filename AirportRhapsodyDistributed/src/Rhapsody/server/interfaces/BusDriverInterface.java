package Rhapsody.server.interfaces;

import Rhapsody.common.States;

/**
 * Bus driver interface to be provided by the tunnel provider for the proxies
 * 
 * @author José Paiva
 * @author André Mourato
 */
public interface BusDriverInterface {
    
    /**
     * Set new busdriver state
     * 
     * @param state
     */
    public void setEntityState(States state);

    /**
     * Get busdriver current state
     * @return bus driver state
     */
    public States getEntityState();

    /**
     * Sets the current seats on the bus
     * 
     * @param seats
     */
    public void setSeats(int[] seats);

    /**
     * Gets the seats in the bus
     * 
     * @return bus seats queue
     */
    public int[] getSeats();

}