package Rhapsody.sharedMems;

import java.util.Hashtable;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.utils.Logger;

/**
 * Lounge datatype implements the Arrival Lounge shared memory region.
 * <p/>
 * This is the starting region of all passengers.
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class Lounge {

	/**
	 * Logger for debugging and pontuation purposes
	 */
	private Logger logger;

	/**
	 * Information about amount of passengers of each flight. <p/>
	 * Index is the flight id. <p/>
	 * Starts with -1 in all, 0 means all passengers disembarked.
	 */
	private int[] passengersPerFlight;

	/**
	 * Contructor method for the Lounge class
	 * 
	 * @param logger
	 */
	public Lounge(Logger logger, int[] passengersPerFlight) {
		this.logger=logger;
		this.passengersPerFlight=passengersPerFlight;
	}

	/**
	 * Methdo to alter the passengers on a flight
	 * @param flightId
	 * @param newPassengers
	 */
	public synchronized void changeFlightPassengers(int flightId, int newPassengers) {
		this.passengersPerFlight[flightId]=newPassengers;
	}

	/**
	 * Method to disembark a passenger
	 */
	public synchronized void passengerDisembarked() {
		Passenger passenger = (Passenger)Thread.currentThread();
		
		// set passenger data
		passenger.setCurrentState(PassengerState.AT_DISEMBARKING_ZONE);
		
		// set lounge data
		this.passengersPerFlight[passenger.getFlight()]-=1;
	}

	/**
	 * Method to wait for all passengers from a flight to arrive
	 * Returns only when all have disembarked
	 */
	public synchronized void waitForPassengers() {
		Porter porter = (Porter)Thread.currentThread();
		
	}





}