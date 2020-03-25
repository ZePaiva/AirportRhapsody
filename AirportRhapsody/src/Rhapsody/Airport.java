package Rhapsody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.Queue;

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
	 * Sleep time for the BusDriver
	 */
	public static final int sleepTime = 100;

	/**
	 * Logfile name
	 */
	public static final String logFile = "logs/log.txt";

	/**
	 * Random to help generate passengers and luggages
	 */
	public static final Random random = new Random();

	/**
	 * Main method
	 * 
	 * @param args runtime arguments
	 * @throws InterruptedException
	 */
	public static void main(String args[]) throws InterruptedException {

		// Generate bags and where to store as well as passengers situation for each flight   
		Queue<Luggage>[] planeHoldLuggage = new Queue[K];
		int[][] luggageForPassengers = new int[N][K];
		String[][] passengersSituation = new String[N][K];

		for (int flights=0; flights < K; flights++) {
			for (int passengers=0; passengers < N; passengers++) {
				int randBags = random.nextInt(M+1);
				String situation = random.nextBoolean() ? "TRT" : "FDT";
				//String situation = "FDT";
				luggageForPassengers[passengers][flights] = randBags;
				passengersSituation[passengers][flights] = situation;
				planeHoldLuggage[flights] = new LinkedList<>();
				for (int i = 0; i < randBags; i++) {
					if (random.nextInt(101) <= 90) {
						planeHoldLuggage[flights].add(new Luggage(passengers, situation));
					}
				}

			}
		}

		System.out.println("Starting variables:\nPlane  Hold Luggagge");
		Arrays.asList(planeHoldLuggage).stream().forEach(p-> {
			p.stream().forEach(l -> System.out.printf("%s ", l.toString()));
			System.out.println("Faux pass");
		});
		System.out.println("Passenger Luggage:");
		Arrays.asList(luggageForPassengers).stream().forEach(p -> {
			System.out.println("New pass");
			for (int i= 0; i < p.length; i++) { System.out.printf("%d ", p[i]);}
		});
		System.out.println();

		// create empty arrays
		int[] flightPassengers = new int[N];
		Arrays.fill(flightPassengers, -1);
		int[] waitingQueue = new int[N];
		Arrays.fill(waitingQueue, -1);
		int[] seats = new int[T];
		Arrays.fill(seats, -1);
		PassengerState[] passengersStates = new PassengerState[N];
		Arrays.fill(passengersStates, null);
		String[] passengersSituationG = new String[N];
		Arrays.fill(passengersSituationG, null);
		int[] bags = new int[N];
		Arrays.fill(bags, -1);

		// Generate shared memory entities
		GeneralRepository generalRepository = new GeneralRepository(logFile, flightPassengers, 
																	planeHoldLuggage[0].size(), 
																	null, 0, 0, null, waitingQueue, 
																	seats, passengersStates, 
																	passengersSituationG, 
																	bags, bags.clone());


		BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint(generalRepository);
		Lounge lounge = new Lounge(generalRepository, baggageCollectionPoint, N, planeHoldLuggage);
		StoreRoom storeRoom = new StoreRoom(generalRepository);
		BaggageReclaim baggageReclaim = new BaggageReclaim(generalRepository);
		ArrivalTerminalExit arrivalTerminalExit = new ArrivalTerminalExit(generalRepository, N, null, lounge, null);
		ArrivalTerminalTransfer arrivalTerminalTransfer = new ArrivalTerminalTransfer(generalRepository);
		DepartureTerminalTransfer departureTerminalTransfer = new DepartureTerminalTransfer();
		DepartureTerminalEntrance departureTerminalEntrance = new DepartureTerminalEntrance(generalRepository, arrivalTerminalExit, lounge, arrivalTerminalTransfer, N);
		arrivalTerminalExit.setArrivalTerminalTransfer(arrivalTerminalTransfer);
		arrivalTerminalExit.setDepartureTerminalEntrance(departureTerminalEntrance);
		
		// Generate porter
		Porter porter = new Porter(generalRepository, storeRoom, lounge, baggageCollectionPoint);
		porter.start();

		// Generate bus driver
		BusDriver busDriver = new BusDriver();
		busDriver.start();

		// Generate passengers
		Passenger[] passengers = new Passenger[N];
		for (int i=0; i<passengers.length; i++) {
			passengers[i]= new Passenger(i, K, luggageForPassengers[i], passengersSituation[i], lounge, 
											baggageCollectionPoint, baggageReclaim, 
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
		generalRepository.finish();
		System.exit(0);
	} 
}