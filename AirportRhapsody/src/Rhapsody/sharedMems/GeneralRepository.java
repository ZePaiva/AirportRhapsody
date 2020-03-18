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
     * Passengers whose life-cycle has ended
     */
    private int passengersTerminated;

    /**
     * variable to signal the end of the simulation
     */
    private boolean allFlightsSimulated;

    /**
     * Indicates if there is a new flight or not. <p/>
     * Useful to block passengers before they regenerate
     */
    private boolean newFlight;

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
        this.allFlightsSimulated=false;
        this.passengersTerminated=this.passengersPerFlight;
        this.newFlight=false;
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
            }
        }
        passenger.setPassengerType(situation);
        passenger.setStartingBags(randBags);
        passenger.setCurrentBags(0);
        passenger.setCurrentState(PassengerState.AT_DISEMBARKING_ZONE);
        passenger.lostBags(false);
        this.logger.updatePlaneHoldBags(randBags, true);
        this.logger.updateBagsInPlane(this.luggageOnPlane.size(), false);
        this.logger.updateSituation(passenger.getPassengerId(), situation, true);
        this.logger.updateStartingBags(passenger.getPassengerId(), randBags, true);
        this.logger.updateCurrentBags(passenger.getPassengerId(), 0, true);
        this.logger.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
        System.out.printf("P%d generated | SB %d | Sit %s\n", passenger.getPassengerId(), randBags, situation);
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
        // checks if there ar luggages in the planes hold
        if (this.luggageOnPlane.empty()) {
            porter.planeHasBags(false);
        } else {
            porter.planeHasBags(true);
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
            this.logger.updateBagsInPlane(this.luggageOnPlane.size(), true);
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
    
    /**
     * Used to signal that the simulation has ended
     */
    public synchronized void allFlightsEnded() {
        this.allFlightsSimulated=true;
        this.newFlight=true;
        System.out.printf("Notify %s\n", this.newFlight);
        notifyAll();
    }

    /**
     * Clears all flight data
     */
    public synchronized void clearFlight(int flightId) {
        // waits until all passengers have terminated
        while(this.passengersTerminated<this.passengersPerFlight){
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("[GENERALREPOSITORY] Porter interrupoted while trying to clear flight data");
                System.exit(3);
            }
        }
        // sets the new flightId
        this.logger.updateFlight(flightId, true);
        // clears all pasenger data relative to the flight
        this.logger.clearFlight(true);
        // resets the value of passengers
        this.passengersTerminated=0;
        // signals it is ready to receive a new flight
        this.newFlight=true;
        // notifies passengers
        notifyAll();
    }

    /**
     * Method used by passenger to signal it's life-cycle has ended
     */
    public synchronized void passengerTerminated() {
        // increases terminated passengers semaphore
        this.passengersTerminated++;
        // unsets new flight variable
        if (!this.allFlightsSimulated) {
            this.newFlight=false;
        }
        // wakes up all threads , notifies porter in case it is the last
        notifyAll();
    }

    public synchronized void willFlyMore() {
        Passenger passenger = (Passenger) Thread.currentThread();
        System.out.printf("P%d terminating? %s\n", passenger.getPassengerId(), this.newFlight);
        // waits until it can generate a new flight or simulation ends
        while (!this.newFlight) {
            try {
                System.out.printf("P%d waiting... %s\n", passenger.getPassengerId(), this.newFlight);
                wait();
            } catch (InterruptedException e) {
                System.err.print("[GENERALREPOSITORY] Passenger interrupted for some reason\n");
                System.exit(3);
            }
        }
        System.out.printf("P%d will fly again? %s\n", passenger.getPassengerId(), this.allFlightsSimulated);
        // signals passenger if simulation has ended
        passenger.canFly(!this.allFlightsSimulated);
    }
}