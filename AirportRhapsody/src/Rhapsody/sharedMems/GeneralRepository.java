package Rhapsody.sharedMems;

import java.util.ArrayList;
import java.util.List;

import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Logger;
import Rhapsody.utils.Luggage;

/**
 * General repository of information for the rhapsody
 * <p/>
 * Here lies the data that is general to all, like flightId, luggage on plane
 * and the real age fo the earth.
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class GeneralRepository {
    
    /**
     * Logger for debugging purposes
     */
    private Logger logger;

    /**
     * Luggage on plane's hold
     */
    private List<Luggage> luggageOnPlane;
    
    /**
     * General Repository constructor
     */
    public GeneralRepository(Logger logger){
        this.logger=logger;
    }

    /**
     * Called when a new plane arrives to update the luggage on the plane conveyor
     * @param newFlight
     */
    public synchronized void setLuggageOnPlain(List<Luggage> newFlight) {
        this.luggageOnPlane=newFlight;
        this.logger.updateBagsInPlane(newFlight.size());
    }

    /**
     * Puts porter to sleep while no plane arrives
     */
    public synchronized void noMoreBagsToCollect(){
        Porter porter = (Porter)Thread.currentThread();
        porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
        this.logger.updatePorterState(porter.getPorterState());
    }

    /**
     * When called will make porter attempt to collect a bag if available
     */
    public synchronized void tryToCollectABag() {
        // update porter state
        Porter porter = (Porter)Thread.currentThread();
        porter.setPorterState(PorterState.AT_THE_PLANES_HOLD);
        this.logger.updatePorterState(porter.getPorterState());

        //
    }


}