package Rhapsody.entities;

import Rhapsody.entities.states.PassengerState;
import Rhapsody.sharedMems.ArrivalTerminalExit;
import Rhapsody.sharedMems.ArrivalTerminalTransfer;
import Rhapsody.sharedMems.BaggageCollectionPoint;
import Rhapsody.sharedMems.BaggageReclaim;
import Rhapsody.sharedMems.DepartureTerminalEntrance;
import Rhapsody.sharedMems.DepartureTerminalTransfer;
import Rhapsody.sharedMems.GeneralRepository;
import Rhapsody.sharedMems.Lounge;

/**
 * Passenger Thread Implements the life-cycle of the passenger and stores it's
 * current state.
 * <p/>
 * 
 * Passenger life-cycle as 2 possible ways
 * <ul>
 * <li>Arrival with all bags
 * <ol>
 * <li>Passengers arrives to the disembarking zone
 * <li>Passenger goes to luggage collection point (conveyor or storeroom)
 * <li>Passenger tries to collect a bag
 * <li>Repeat step 3 until passenger successfully collects all bags
 * <li>Passenger successfully collects all bags and goes Home or departs
 * </ol>
 * <li>Arrival with lost bags
 * <ol>
 * <li>Passengers arrives to the disembarking zone
 * <li>Passenger goes to luggage collection point (conveyor or storeroom)
 * <li>Passenger tries to collect a bag
 * <li>Repeat step 3 until passenger notices bag is lost
 * <li>Passenger goes to baggage reclaim office
 * <li>Passenger goes Home or departs
 * </ol>
 * <li>Departure
 * <ol>
 * <li>Passengers arrives to the Airport
 * <li>Passenger try to take a bus
 * <li>Passenger enters the bus
 * <li>Passenger exits the bus
 * <li>Passenger enter departure terminal
 * <li>Passenger prepares next leg
 * </ol>
 * </ul>
 * 
 * @author José Paiva
 * @author Andre Mourato
 */
public class Passenger extends Thread {

	/**
	 * Unique Id of the passenger
	 */
	private final int id;

	/**
	 * Amount of flights (loops)
	 */
	private final int flights;

	/**
	 * Amount of bags the passenger has for each flight  <p/>
	 * 
	 */
	private int[] startingBags;

	/**
	 * Situation of the passenger for each flight
	 */
	private final String[] situation;

	/**
	 * Arrival lounge variable
	 */
	private Lounge arrivalLounge;

	/**
	 * Baggage collection point shared memory variable
	 */
	private BaggageCollectionPoint baggageCollectionPoint;

	/**
	 * Baggage reclaim point shared memmory variable
	 */
	private BaggageReclaim baggageReclaim;

	/**
	 * Arrival terminal exit where passenger declares as FDT type
	 */
	private ArrivalTerminalExit arrivalTerminalExit;

	/**
	 * Arrival terminal transfer quay
	 */
	private ArrivalTerminalTransfer arrivalTerminalTransfer;

	/**
	 * Departure terminal transfer quay
	 */
	private DepartureTerminalTransfer departureTerminalTransfer;

	/**
	 * Departure terminal entrance where passenger declares as TRT type
	 */
	private DepartureTerminalEntrance departureTerminalEntrance;

	/**
	 * General Repository of information
	 */
	private GeneralRepository generalRepository;

	/**
	 * State for the Passenger thread
	 */
	private PassengerState currentState;

	/**
	 * Current bags the passenger is holding
	 */
	private int currentBags;

	/**
	 * Total passengers on flight
	 */
	private int passengers;

	public static final String ANSI_GREEN = "\u001B[32m";


	/**
	 * Passenger constructor method
	 * @param id
	 * @param flights
	 * @param startingBags
	 * @param type
	 * @param lookAtCellPhone
	 * @param arrivalLounge
	 * @param baggageCollectionPoint
	 * @param baggageReclaim
	 * @param arrivalTerminalExit
	 * @param arrivalTerminalTransfer
	 * @param departureTerminalTransfer
	 * @param departureTerminalEntrance
	 * @param generalRepository
	 */
	public Passenger(int id, int flights, int passengers, int[] startingBags, String[] type, Lounge arrivalLounge, 
						BaggageCollectionPoint baggageCollectionPoint, BaggageReclaim baggageReclaim, 
						ArrivalTerminalExit arrivalTerminalExit, 
						ArrivalTerminalTransfer arrivalTerminalTransfer, 
						DepartureTerminalTransfer departureTerminalTransfer, 
						DepartureTerminalEntrance departureTerminalEntrance,
						GeneralRepository generalRepository) {
		this.id = id;
		this.flights=flights;
		this.passengers=passengers;
		this.startingBags = startingBags;
		this.situation=type;
		this.arrivalLounge = arrivalLounge;
		this.baggageCollectionPoint = baggageCollectionPoint;
		this.baggageReclaim = baggageReclaim;
		this.arrivalTerminalExit = arrivalTerminalExit;
		this.arrivalTerminalTransfer = arrivalTerminalTransfer;
		this.departureTerminalTransfer = departureTerminalTransfer;
		this.departureTerminalEntrance = departureTerminalEntrance;
		this.generalRepository = generalRepository;
		this.currentBags = 0;
		this.currentState = PassengerState.AT_DISEMBARKING_ZONE;
		this.startingBags = startingBags;
	}

	/**
	 * Passenger state alteration function
	 * 
	 * @param state
	 */
	public void setCurrentState(PassengerState state) {
		this.currentState = state;
	}

	/**
	 * Return's passenger current state
	 * 
	 * @return passenger state of type PassengerState
	 */
	public PassengerState getCurrentState() {
		return this.currentState;
	}

	/**
	 * Return's passenger id
	 * 
	 * @return passenger id of type int
	 */
	public int getPassengerId() {
		return this.id;
	}

	/**
	 * Get passenger starting bags for all flights
	 * @return starting Bags of type int[]
	 */
	public int[] getStartingBags() {
		return this.startingBags;
	}

	/**
	 * Get passenger situation for all flights
	 * @return situation of type String[]
	 */
	public String[] getPassengerSituation() {
		return this.situation;
	}

	/**
	 * Return's pasenger current bags
	 * 
	 * @return passenger current bags of type int
	 */
	public int getCurrentBags() {
		return this.currentBags;
	}

	/**
	 * Sets passenger current bags
	 */
	public void setCurrentBags(int currentBags) {
		this.currentBags = currentBags;
	}

	/**
	 * Passenger life-cycle Passenger arrives and informs of it's arival to the
	 * lounge [Lounge.java] {AT_DISEMBARKING_ZONE} The Lounge puts the passenger on
	 * hold until it can go to the baggage collection point (porter might be busy
	 * with another plane) The Lounge checks if this passenger is the last of it's
	 * flight, if it is awakes the porter, if it's not allows the porter to sleep
	 * more When the porter awakes goes to the baggage collection point
	 * [BaggageCollectionPoint.java] {AT_LUGGAGE_COLLECTION} In collection point
	 * tries to collect all bags If successfull will go back to lounge or goHome
	 * (50/50 chance) {AT_DISEMBARKING_ZONE, EXIT_ARRIVAL_TERMINAL} If porter
	 * signals end of bags before it has collected all bags it will go to baggage
	 * reclaim office {AT_LUGGAGE_RECLAIM} Baggage Reclaim office will answer all at
	 * the same time(?) [BaggageReclaim.java] After claiming all luggage it will try
	 * to enter the bus waiting line [ArrivalTerminalTransfer]
	 * {ARRIVING_TRANSFER_TERMINAL} Waits for the bus to arrive Try to get in the
	 * bus or wait if bus is full {TERMINAL_TRANSFER} Arrives at departure terminal
	 * transfer quay [DepartureTerminalTransfer.java] {DEPARTING_TRANSFER_TERMINAL}
	 * Enters the departure terminal to prepare next leg of journey
	 * [DepartureTerminalEntrance.java] {DEPARTING}
	 * 
	 * Notes: [] -> shared memory region which will control {} -> state it will
	 * enter () -> doubts/extra info a-zA-Z0-9 -> specification
	 * 
	 */
	public void run() {
		// run
		System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is up\n", this.id);
		for (int flight = 0; flight < this.flights; flight++){
			this.currentBags=0;
			arrivalLounge.whatShouldIDo(flight);
			System.out.printf(ANSI_GREEN + "[PASSENGER] P%d disembarked from flight %d | SB %d\n", this.id, flight, this.startingBags[flight]);
			
			// Transit passengers life-cycle
			if ( this.situation[flight].equals("TRT") ){
				// transit passenger, goes to arrival transfer quay 
				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d will go to bus stop\n", this.id);
			
				// goes to arrrival terminal transfer quay and waits until it can board
				arrivalTerminalTransfer.takeABus();

				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is trying to enter the bus\n", this.id);
				// enter the bus to go to the departure terminal transfer quay
				while (!arrivalTerminalTransfer.enterTheBus());

				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d entered the bus\n", this.id);
				// goes from departure terminal transfer quay to the departure termianl transfer
				departureTerminalTransfer.leaveTheBus();

				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is preparing next leg of journey\n", this.id);
				// blocking state that will make passenger wait until all other passengers are ready
				int exited=arrivalTerminalExit.currentBlockedPassengers();
				departureTerminalEntrance.prepareNextLeg(flight==this.flights-1, arrivalTerminalExit.currentBlockedPassengers());
				int departed=departureTerminalEntrance.currentBlockedPassengers();
				System.out.printf("[PASSENGER] P%d exiting | PT %d\n", this.id, departed+exited);
				departureTerminalEntrance.wakeCurrentBlockedPassengers();
				arrivalTerminalExit.wakeCurrentBlockedPassengers();

			// Final Destination passengers life-cycle
			} else if ( this.situation[flight].equals("FDT")) {
				// final destination passenger, goes to luggage collection point
				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d will terminate it's journey here\n", this.id);
				// tries to collect all bags until it collects them all or it knows it lost it's bags
				if (this.startingBags[flight]!=0) {
					this.currentBags= baggageCollectionPoint.goCollectABag(this.startingBags[flight]);
					System.out.printf(ANSI_GREEN + "[PASSENGER] P%d tried to collect a bag | CB %d | SB %d | LB %s\n", 
										this.id, this.currentBags, this.startingBags[flight], this.startingBags[flight]==this.currentBags);	
				}

				// if lost any luggage
				if ( this.currentBags!= this.startingBags[flight]) {
					baggageReclaim.reportMissingBags(this.startingBags[flight]-this.currentBags);
					System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is reclaiming bags\n", this.id);
				}

				// blocking goHome method, must 
				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is going home\n", this.id);
				int departed=departureTerminalEntrance.currentBlockedPassengers();
				arrivalTerminalExit.goHome(flight==this.flights-1, departureTerminalEntrance.currentBlockedPassengers());
				int exited=arrivalTerminalExit.currentBlockedPassengers();
				System.out.printf("[PASSENGER] P%d exiting | PT %d\n", this.id, departed+exited);
				departureTerminalEntrance.wakeCurrentBlockedPassengers();
				arrivalTerminalExit.wakeCurrentBlockedPassengers();
			} else {
				System.err.printf(ANSI_GREEN + "[PASSENGER] Passenger %d had wrong start", this.id);
				System.exit(5);
			}
		}
		System.out.printf(ANSI_GREEN+"[PASSENGER] P%d exiting run(), joining...\n", this.id);
	}
}
