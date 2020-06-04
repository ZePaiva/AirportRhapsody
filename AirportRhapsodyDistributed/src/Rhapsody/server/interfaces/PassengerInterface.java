package Rhapsody.server.interfaces;

import Rhapsody.common.States;

/**
 * Passenger interface to be used for the provider
 * 
 * @author José Paiva
 * @author André Mourato
 */
public interface PassengerInterface {
    
    /**
     * Get current entity ID
     * @return
     */
    public int getEntityID();

    /**
     * Sets new ID if needed
     * 
     * @param id
     */
    public void setEntityID(int id);
    
    /**
     * Get current entity state
     * @return current state
     */
    public States getEntityState();

    /**
     * Set current entity state
     * 
     * @param state
     */
    public void setEntityState(States state);
    
    /**
     * Gets the current bags the passenger has
     * @return current bags
     */
    public int getCurrentBags();

    /**
     * Sets the current bags the passenger is holding to the given value
     * @param bags
     */
    public void setCurrentBags(int bags);

    /**
     * Gets the passenger starting bags for this flight
     * @return starting bags for the flight
     */
    public int getStartingBags();

    /**
     * Gets the current flight
     * @return flight ID
     */
    public int getFlightID();

    /**
     * Get passenger situation for this flight
     * @return passenger situation
     */
    public String getSituation();
}