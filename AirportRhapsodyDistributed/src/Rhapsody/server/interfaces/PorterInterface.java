package Rhapsody.server.interfaces;

import Rhapsody.common.Luggage;
import Rhapsody.common.States;

/**
 * Porter interface data-type
 * 
 * @author José Paiva
 * @author André Mourato
 */
public interface PorterInterface {
    
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
     * Gets teh current bag the porter is holding
     * @return current bag
     */
    public Luggage getCurrentBag();

    /**
     * Sets the bag the porter is holding to the specified bag
     * 
     * @param bag
     */
    public void setCurrentBag(Luggage bag);
}