package Rhapsody.sharedMems;

import java.util.EmptyStackException;
import java.util.Stack;

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
    private Stack<Luggage> luggageOnPlane;
    
    /**
     * General Repository constructor
     */
    public GeneralRepository(Logger logger){
        this.logger=logger;
        this.luggageOnPlane=new Stack<>();
    }

    /**
     * Called when a new plane arrives to update the luggage on the plane conveyor
     * @param newFlight
     */
    public synchronized void setLuggageOnPlain(Stack<Luggage> newFlight) {
        this.luggageOnPlane=newFlight;
        this.logger.updateBagsInPlane(newFlight.size());
    }

    /**
     * Puts porter to sleep while no plane arrives
     */
    public synchronized void noMoreBagsToCollect(){
        Porter porter = (Porter)Thread.currentThread();
        porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
        // alert all passengers bags are ready to collect
        notifyAll();
        this.logger.updatePorterState(porter.getPorterState());
    }

    /**
     * When called will make porter attempt to collect a bag if available
     */
    public synchronized void tryToCollectABag() {
        // update porter state
        Porter porter = (Porter)Thread.currentThread();
        porter.setPorterState(PorterState.AT_THE_PLANES_HOLD);
        if (this.luggageOnPlane.empty()) {
            porter.planeHasBags(false);
        }
        this.logger.updatePorterState(porter.getPorterState());
    }

    /**
     * Plane hold method to update the luggages accordingly. <p/>
     * <b>DOES NOT ALTER BAGS IN STOREROOM OR CONVEYOR BELT<b/>  
     */
    public synchronized void carryItToAppropriateStore() {
        Porter porter = (Porter) Thread.currentThread();
        try {
            Luggage bag = this.luggageOnPlane.pop();
            porter.setCurrentLuggage(bag);
            this.logger.updateBagsInPlane(this.luggageOnPlane.size());
        } catch (EmptyStackException e) {
            System.err.print("[GeneralRepository] Luggage stack already empty, reseting porter");
            // resetting porter
            porter.planeHasBags(false);
            porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentLuggage(null);
            // resetting luggage stack
            this.luggageOnPlane=new Stack<>();
            this.logger.updatePorterState(porter.getPorterState());
            this.logger.updateBagsInPlane(0);
        }
    }
}