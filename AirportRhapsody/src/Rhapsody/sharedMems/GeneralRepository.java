package Rhapsody.sharedMems;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
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
     * @param logger
     * @param passengersPerFlight
     * @param maxLuggaageAmount
     */
    public GeneralRepository(Logger logger, int passengersPerFlight, int maxLuggageAmount){
        this.logger=logger;
        this.luggageOnPlane=new Stack<>();
        this.passengersPerFlight=passengersPerFlight;
        this.maxLuggageAmount=maxLuggageAmount;
        this.random=new Random();
    }

    /**
     * Method to generate passenger situation
     */
    public synchronized void generatePassenger() {
        Passenger passenger = (Passenger)Thread.currentThread();
        int randBags = random.nextInt(this.maxLuggageAmount+1);
        String situation = random.nextBoolean() ? "TRT" : "FDT";
        for (int b = 0; b < randBags; b++) {
            if (this.luggageOnPlane.size() >= this.passengersPerFlight * this.maxLuggageAmount) {
                System.err.print("[GENERALREPOSITORY] cant stop for some reason\n");
                System.exit(3);
            }
            if (random.nextInt(100) <= 90) {
                this.luggageOnPlane.push(new Luggage(passenger.getPassengerId(), situation));
                this.logger.updatePlaneHoldBags(+1, true);   
            }
        }
        passenger.setPassengerType(situation);
        passenger.setStartingBags(randBags);
        passenger.setCurrentState(PassengerState.AT_DISEMBARKING_ZONE);
        System.out.printf("P%d | Bags: %d\n", passenger.getPassengerId(), randBags);
        this.logger.updateBagsInPlane(this.luggageOnPlane.size(), true);
        this.logger.updateStartingBags(passenger.getPassengerId(), randBags, true);
        this.logger.updateSituation(passenger.getPassengerId(), situation, true);
        this.logger.addPassengerToFlight(passenger.getPassengerId(), true);
        this.logger.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
    }

    /**
     * Called when a new plane arrives to update the luggage on the plane conveyor
     * @param newFlight
     */
    public synchronized void setLuggageOnPlain(Stack<Luggage> newFlight) {
        this.luggageOnPlane=newFlight;
        this.logger.updateBagsInPlane(newFlight.size(), true);
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
        this.logger.updatePorterState(porter.getPorterState(), false);
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
            System.out.printf("%s\n", bag.toString());
            this.logger.updateBagsInPlane(this.luggageOnPlane.size(), false);
        } catch (EmptyStackException e) {
            System.err.print("[GeneralRepository] Luggage stack already empty, reseting porter");
            // resetting porter
            porter.planeHasBags(false);
            porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentLuggage(null);
            // resetting luggage stack
            this.luggageOnPlane=new Stack<>();
            this.logger.updatePorterState(porter.getPorterState(), true);
            this.logger.updateBagsInPlane(0, false);
        }
    }
}