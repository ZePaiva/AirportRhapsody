package Rhapsody;

import java.util.Arrays;

import Rhapsody.entities.BusDriver;
import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.utils.Logger;
import Rhapsody.utils.Luggage;

/**
 * Airport Main class 
 * <p/>
 * Main function launches all threads, in the future it will e configurable via external file
 * 
 * @author Jose Paiva 
 * @author Andre Mourato
 */
public class Airport {

	// Simulation parameters
	/**
	 * Number of planes landing
	 */
	public static final int K = 5;

	/**
	 * Number of Passengers arriving in each plane
	 */
	public static final int N = 6;

	/**
	 * Number of maximum baggages
	 */
	public static final int M = 2;

	/**
	 * Maximum seats in a bus
	 */
	public static final int T = 3;

	/**
	 * Logfile name
	 */
	public static final String logFile = "logs/log.txt";

	/**
	 * Main method
	 * 
	 * @param args runtime arguments
	 */
	public static void main(String args[]) {
		
		// Generate entities
		Porter porter = new Porter();
		BusDriver busDriver = new BusDriver();
		Passenger[] passengers = new Passenger[N];

		// Generate shared memory regions
		/**
		 * TBD
		 */

		// Generate logger
		// create empty arrays
		int[] flightPassengers = new int[K];
		Arrays.fill(flightPassengers, -1);
		Luggage[] bagsOnPlane = new Luggage[N*M];
		int[] waitingQueue = new int[N];
		Arrays.fill(waitingQueue, -1);
		int[] seats = new int[T];
		Arrays.fill(seats, -1);
		PassengerState[] passengerStates = new PassengerState[K*N];
		String[] passengerSituation = new String[K*N];
		int[] bags = new int[K*N];
		// create logger
		Logger logger = new Logger(logFile, K, flightPassengers, bagsOnPlane.length, 
									porter.getPorterState(), 0, 0, busDriver.getBusDriverState(), 
									waitingQueue, seats, passengerStates, passengerSituation, seats, 
									bags, bags);

		// Initialize Problem
		/**
		 * TBD
		 */

		// Start Simulation
		/**
		 * TBD
		 */

		// Wait until the end of simulation
		/**
		 * TBD
		 */

		// exit cleanly
		/**
		 * TBD
		 */
	} 
}