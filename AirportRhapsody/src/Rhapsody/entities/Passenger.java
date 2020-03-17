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
import Rhapsody.utils.Logger;

/**
 * Passenger Thread Implements the life-cycle of the passenger and stores it's
 * current state. <p/>
 * 
 * Passenger life-cycle as 2 possible ways
 * <ul>
 * <li> Arrival with all bags
 * <ol>
 * <li> Passengers arrives to the disembarking zone
 * <li> Passenger goes to luggage collection point (conveyor or storeroom)
 * <li> Passenger tries to collect a bag
 * <li> Repeat step 3 until passenger successfully collects all bags
 * <li> Passenger successfully collects all bags and goes Home or departs
 * </ol>
 * <li> Arrival with lost bags
 * <ol>
 * <li> Passengers arrives to the disembarking zone
 * <li> Passenger goes to luggage collection point (conveyor or storeroom)
 * <li> Passenger tries to collect a bag
 * <li> Repeat step 3 until passenger notices bag is lost
 * <li> Passenger goes to baggage reclaim office
 * <li> Passenger goes Home or departs
 * </ol>
 * <li> Departure
 * <ol>
 * <li> Passengers arrives to the Airport
 * <li> Passenger try to take a bus
 * <li> Passenger enters the bus
 * <li> Passenger exits the bus
 * <li> Passenger enter departure terminal
 * <li> Passenger prepares next leg
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
	 * Logger 
	 */
	private Logger logger;

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
	 * Amount of bags the passenger has started
	 */
	private int startingBags;


	/**
	 * State for the Passenger thread
	 */
	private PassengerState currentState;

	/**
	 * Current bags the passenger is holding
	 */
	private int currentBags;

	/**
	 * Amount of lost bags
	 */
	private boolean lostBags;

	/**
	 * Type of passenger <p/>
	 * Can be TRT or FDT
	 */
	private String type;

	/**
	 * Boolean to check if the airport has closed
	 */
	private boolean canFly;
	
	/**
	 * Last Flight id
	 */
	private int flight;

	/**
	 * Passenger constructor method
	 * 
	 * @param id
	 * @param logger
	 * @param arrivalLounge
	 * @param baggageCollectionPoint
	 * @param baggageReclaim
	 * @param arrivalTerminalExit
	 * @param arrivalTerminalTransfer
	 * @param departureTerminalTransfer
	 * @param departureTerminalEntrance
	 * @param currentState
	 */
	public Passenger(int id, Logger logger, Lounge arrivalLounge, 
						BaggageCollectionPoint baggageCollectionPoint, BaggageReclaim baggageReclaim, 
						ArrivalTerminalExit arrivalTerminalExit, 
						ArrivalTerminalTransfer arrivalTerminalTransfer, 
						DepartureTerminalTransfer departureTerminalTransfer, 
						DepartureTerminalEntrance departureTerminalEntrance, 
						GeneralRepository generalRepository) {
		this.id = id;
		this.logger = logger;
		this.arrivalLounge = arrivalLounge;
		this.baggageCollectionPoint = baggageCollectionPoint;
		this.baggageReclaim = baggageReclaim;
		this.arrivalTerminalExit = arrivalTerminalExit;
		this.arrivalTerminalTransfer = arrivalTerminalTransfer;
		this.departureTerminalTransfer = departureTerminalTransfer;
		this.departureTerminalEntrance = departureTerminalEntrance;
		this.generalRepository = generalRepository;
		this.lostBags=false;
		this.currentBags=0;
		this.currentState=PassengerState.AT_DISEMBARKING_ZONE;
		this.startingBags=0;
		this.canFly=true;
		this.flight=-1;
	}

	/**
	 * Get passenger last flightID
	 * @return
	 */
	public int getFlight() {
		return this.flight;
	}

	/**
	 * Set passenger Last flightID
	 * @param flight
	 */
	public void setFlight(int flight) {
		this.flight = flight;
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
	 * Set the starting amount of bags for this passenger
	 * 
	 * @param startingBags
	 */
	public void setStartingBags(int startingBags) {
		this.startingBags = startingBags;
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
	 * @return passenger id of type int
	 */
	public int getPassengerId() {
		return this.id;
	}

	/**
	 * Return's passenger starting bags
	 * @return passenger starting bags of type int
	 */
	public int getStartingBags() {
		return this.startingBags;
	}

	/**
	 * Return's pasenger current bags
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
	 * Returns passenger type
	 * @return passenger type of type String
	 */
	public String getPassengerType() {
		return this.type;
	}

	/**
	 * Sets passenger type
	 * @param type
	 */
	public void setPassengerType(String type) {
		this.type=type;
	}

	/**
	 * Sets if passenger has lsot at least 1 bag, unsets otherwise
	 * @param lost
	 */
	public void lostBags(boolean lost) {
		this.lostBags=lost;
	}

	/**
	 * Sets if airport is open or not
	 */
	public void canFly(boolean canFly) {
		this.canFly = canFly;
	}

	/**
	 * Passenger life-cycle
	 * Passenger arrives and informs of it's arival to the lounge [Lounge.java] {AT_DISEMBARKING_ZONE}
	 * The Lounge puts the passenger on hold until it can go to the baggage collection point (porter might be busy with another plane)
	 * The Lounge checks if this passenger is the last of it's flight, if it is awakes the porter, if it's not allows the porter to sleep more
	 * When the porter awakes goes to the baggage collection point [BaggageCollectionPoint.java] {AT_LUGGAGE_COLLECTION}
	 * In collection point tries to collect all bags
	 * If successfull will go back to lounge or goHome (50/50 chance) {AT_DISEMBARKING_ZONE, EXIT_ARRIVAL_TERMINAL}
	 * If porter signals end of bags before it has collected all bags it will go to baggage reclaim office {AT_LUGGAGE_RECLAIM}
	 * Baggage Reclaim office will answer all at the same time(?) [BaggageReclaim.java]
	 * After claiming all luggage it will try to enter the bus waiting line [ArrivalTerminalTransfer] {ARRIVING_TRANSFER_TERMINAL}
	 * Waits for the bus to arrive
	 * Try to get in the bus or wait if bus is full {TERMINAL_TRANSFER}
	 * Arrives at departure terminal transfer quay [DepartureTerminalTransfer.java] {DEPARTING_TRANSFER_TERMINAL}
	 * Enters the departure terminal to prepare next leg of journey [DepartureTerminalEntrance.java] {DEPARTING}
	 * 
	 * Notes:
	 * [] -> shared memory region which will control
	 * {} -> state it will enter
	 * () -> doubts/extra info
	 * a-zA-Z0-9 -> specification
	 * 
	 */
	public void run() {
		// run
		/** 
		 */
		//System.out.printf("Passenger %d is up\n", this.getPassengerId());
		while(this.canFly) {
			generalRepository.generatePassenger();
			arrivalLounge.whatShouldIDo();

			if (this.type.equals("FDT")) { 			  	// in case this passenger is of Final Destination type
				// check if it has bags
				if (this.startingBags!=0) {
					// get bags
					while(!this.lostBags && this.currentBags!=this.startingBags) {
						this.baggageCollectionPoint.goCollectABag();
					}
					if (this.lostBags) {
						// reclaim bag
						this.baggageReclaim.reportMissingBags(); 
					}
				}
				this.arrivalTerminalExit.goHome();
				if (this.canFly)
					this.generalRepository.goHome();
				System.out.println(this.canFly);
			} else if (this.type.equals("TRT")) {     	// in case it's a transit type passenger
				// go take a bus
				this.arrivalTerminalExit.goHome();
				if (this.canFly)
					this.generalRepository.goHome();
				System.out.println(this.canFly);
			} else { 									// in case it failed upon starting
				System.err.printf("[PASSENGER] Passenger %d had wrong start", this.id);
				System.exit(5);
			}
		}
	}
}
