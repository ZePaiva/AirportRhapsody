package Rhapsody.sharedMems;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;

import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Logger;
import Rhapsody.utils.Luggage;

/**
 * General repository of information for the rhapsody
 * <p/>
 * Here lies the data that is general to all, like flightId, luggage on plane, passenger type 
 * and the real age fo the earth according to the holy bible.
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

    // generation parameters
    /**
     * amount of passengers per flight
     */
    private int passengersPerFlight;

    /**
     * maximum luggage per passenger
     */
    private int maxLuggageAmount;

    /**
     * random util for lfight generation
     */
    private Random random;
    
    /**
     * General Repository constructor
     */
    public GeneralRepository(Logger logger, int passengersPerFlight, int maxLuggageAmount){
        this.logger=logger;
        this.luggageOnPlane=new Stack<>();
        this.passengersPerFlight=passengersPerFlight;
        this.maxLuggageAmount=maxLuggageAmount;
        this.random=new Random();
    }

    /**
     * Generates new starting parameters for the flight 
     */
    public synchronized void generateFlight(){
        for (int p = 0; p < this.passengersPerFlight; p++) {
            int randBags = random.nextInt(this.maxLuggageAmount);
            System.out.println(randBags);
            String situation = random.nextBoolean() ? "TRT" : "FDT";
            for (int b = 0; b < randBags; b++) {
                this.luggageOnPlane.push(new Luggage(p, situation));
            }
        }
        // only used to cause randomness, can be deactivated
        Collections.shuffle(this.luggageOnPlane);
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