package Rhapsody;

import java.util.Arrays;

import Rhapsody.entities.BusDriver;
import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.sharedMems.ArrivalTerminalExit;
import Rhapsody.sharedMems.ArrivalTerminalTransfer;
import Rhapsody.sharedMems.BaggageCollectionPoint;
import Rhapsody.sharedMems.BaggageReclaim;
import Rhapsody.sharedMems.DepartureTerminalEntrance;
import Rhapsody.sharedMems.DepartureTerminalTransfer;
import Rhapsody.sharedMems.GeneralRepository;
import Rhapsody.sharedMems.Lounge;
import Rhapsody.sharedMems.StoreRoom;
import Rhapsody.utils.Logger;

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
	public static final int K = 2;

	/**
	 * Number of Passengers arriving in each plane
	 */
	public static final int N = 1;

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

		// Generate logger
		// create empty arrays
		int[] flightPassengers = new int[N];
		Arrays.fill(flightPassengers, -1);
		int[] waitingQueue = new int[N];
		Arrays.fill(waitingQueue, -1);
		int[] seats = new int[T];
		Arrays.fill(seats, -1);
		PassengerState[] passengersStates = new PassengerState[N];
		Arrays.fill(passengersStates, null);
		String[] passengersSituation = new String[N];
		Arrays.fill(passengersSituation, null);
		int[] bags = new int[N];
		Arrays.fill(bags, -1);
		// create logger
		Logger logger = new Logger(logFile, 1, flightPassengers, 0, null, 0, 0, null, waitingQueue, 
									seats, passengersStates, passengersSituation, bags, bags.clone());

		// Generate shared memory entities
		GeneralRepository generalRepository = new GeneralRepository(logger, N, M);
		Lounge lounge = new Lounge(logger, N, K);
		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint(logger);
		StoreRoom storeRoom = new StoreRoom(logger);
		BaggageReclaim baggageReclaim = new BaggageReclaim(logger);
		ArrivalTerminalExit arrivalTerminalExit = new ArrivalTerminalExit(logger);
		ArrivalTerminalTransfer arrivalTerminalTransfer = new ArrivalTerminalTransfer();
		DepartureTerminalTransfer departureTerminalTransfer = new DepartureTerminalTransfer();
		DepartureTerminalEntrance departureTerminalEntrance = new DepartureTerminalEntrance();

		// Generate working entities
		Porter porter = new Porter(generalRepository, storeRoom, lounge, baggageCollectionPoint, K);
		porter.start();
		BusDriver busDriver = new BusDriver();
		busDriver.start();
		Passenger[] passengers = new Passenger[N];
		for (int i=0; i<passengers.length; i++) {
			passengers[i]= new Passenger(i, logger, lounge, baggageCollectionPoint, baggageReclaim, 
											arrivalTerminalExit, arrivalTerminalTransfer, 
											departureTerminalTransfer, departureTerminalEntrance, 
											generalRepository);
			passengers[i].start();	
		}

		// Wait until the end of simulation
		// end of passengers
		for (int i = 0; i < passengers.length; i++) {
			try {
				passengers[i].join();
			} catch (InterruptedException e) {
				System.err.println("Something happened with a passenger thread");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// end of porter
		try {
			porter.join();
		} catch (InterruptedException e) {
			System.err.println("Something happened with the porter thread");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// end of bus driver
		try {
			busDriver.join();
		} catch (InterruptedException e) {
			System.err.println("Something happened with the bus driver thread");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// exit cleanly
		logger.finish();
		System.exit(0);
	} 
}