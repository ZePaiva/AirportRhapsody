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
	 * Logfile name
	 */
	public static final String logFile = "logs/log.txt";

	/**
	 * Random to help generate passengers and luggages
	 */
	public static final Random random = new Random();

	/**
	 * Max Duration of bus driver waiting time
	 */
	public static final int busSchedule = 100;

	public static final String ANSI_RESET = "\u001B[0m";

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
				//int randBags=0;
				String situation = random.nextBoolean() ? "TRT" : "FDT";
				//String situation = "FDT";
				luggageForPassengers[passengers][flights] = randBags;
				passengersSituation[passengers][flights] = situation;
				planeHoldLuggage[flights] = new LinkedList<>();
				for (int i = 0; i < randBags; i++) {
					if (random.nextInt(101) <= 99) {
						planeHoldLuggage[flights].add(new Luggage(passengers, situation));
					}
				}
			}
		}

		System.out.println(ANSI_RESET+"Starting variables:\nPlane  Hold Luggagge");
		Arrays.asList(planeHoldLuggage).stream().forEach(p-> {
			p.stream().forEach(l -> System.out.printf("%s ", l.toString()));
			System.out.println("Faux pass");
		});
		System.out.print("Passenger Luggage:");
		for (int a=0; a < luggageForPassengers.length; a++) {
			System.out.printf("\nP%d: ", a);
			for (int b=0; b < luggageForPassengers[a].length; b++) {
				System.out.printf("%d ", luggageForPassengers[a][b]);
			}
		}
		System.out.print("\nPassenger situation:");
		for (int a=0; a < passengersSituation.length; a++) {
			System.out.printf("\nP%d: ", a);
			for (int b=0; b < passengersSituation[a].length; b++) {
				System.out.printf("%s ", passengersSituation[a][b]);
			}
		}
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
		ArrivalTerminalTransfer arrivalTerminalTransfer = new ArrivalTerminalTransfer(N, T, busSchedule, generalRepository);
		DepartureTerminalTransfer departureTerminalTransfer = new DepartureTerminalTransfer(generalRepository);
		DepartureTerminalEntrance departureTerminalEntrance = new DepartureTerminalEntrance(generalRepository, arrivalTerminalExit, lounge, arrivalTerminalTransfer, N);
		arrivalTerminalExit.setArrivalTerminalTransfer(arrivalTerminalTransfer);
		arrivalTerminalExit.setDepartureTerminalEntrance(departureTerminalEntrance);
		
		// Generate porter
		Porter porter = new Porter(generalRepository, storeRoom, lounge, baggageCollectionPoint);
		porter.start();

		// Generate bus driver
		BusDriver busDriver = new BusDriver(T, 100, generalRepository, arrivalTerminalTransfer,
				departureTerminalTransfer);
		busDriver.start();

		// Generate passengers
		Passenger[] passengers = new Passenger[N];
		for (int i=0; i<passengers.length; i++) {
			passengers[i]= new Passenger(i, K, N, luggageForPassengers[i], passengersSituation[i], lounge, 
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