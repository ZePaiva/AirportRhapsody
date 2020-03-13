package Rhapsody.entities;

import Rhapsody.entities.states.PassengerState;

/**
 * Passenger Thread Implements the life-cycle of the passenger and stores it's
 * current state. <p/>
 * 
 * Passenger life-cycle as 3 possible ways
 * <ul>
 * <li> Arrival with all bags
 * <ol>
 * <li> Passengers arrives to the disembarking zone
 * <li> Passenger goes to luggage collection point
 * <li> Passenger tries to collect a bag
 * <li> Repeat step 3 until passenger successfully collects all bags
 * <li> Passenger successfully collects all bags and goes Home
 * </ol>
 * <li> Arrival with lost bags
 * <ol>
 * <li> Passengers arrives to the disembarking zone
 * <li> Passenger goes to luggage collection point
 * <li> Passenger tries to collect a bag
 * <li> Repeat step 3 until passenger notices bag is lost
 * <li> Passenger goes to baggage reclaim office
 * <li> Passenger goes Home
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
	 * Amount of bags the passenger has started
	 */
	private final int startingBags;

	/**
	 * Flight id of the passenger
	 */
	private final int passengerFlight;

	/**
	 * State for the Passenger thread
	 */
	private PassengerState currentState;

	/**
	 * Current bags the passenger is holding
	 */
	private int currentBags;

	/**
	 * Situation of the passenger (is TRT or FDT)
	 */
	private String situation;

	/**
	 * Constructor for passengger class
	 * 
	 * @param id
	 * @param bags
	 */
	public Passenger(int id, int bags, int flight) {
		this.id = id;
		this.startingBags = bags;
		this.passengerFlight = flight;
		this.currentBags = 0;
		this.currentState=currentState.AT_DISEMBARKING_ZONE;
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
	 * Return's passenger flight id
	 * @return passenger flight id of type int
	 */
	public int getFlight() {
		return this.passengerFlight;
	}

	/**
	 * Return's pasenger current bags
	 * @return passenger current bags of type int
	 */
	public int getCurrentBags() {
		return this.currentBags;
	}

	/**
	 * Sets pasenger current bags
	 */
	public void setCurrentBags(int currentBags) {
		this.currentBags = currentBags;
	}

	/**
	 * Gets passenger current situation
	 * @return situation of type string, varies between FDT & TRT & --- 
	 */
	public String getSituation() {
		return this.situation;
	}

	/**
	 * Sets the passenger current situation
	 * @param situation
	 */
	public void setSituation(String situation) {
		this.situation = situation;
	}

	/**
	 * Passenger life-cycle
	 */
	public void run() {
		// run
		/**
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
		 *  
		 */
	}
	
	public void whatShouldIDo(){}
	public void goCollectABag(){}
	public void reportMissingBags(){}
	public void goHome(){}
	public void takeABus() {}
	public void enterTheBus(){}
	public void leaveTheBus(){}
	public void prepareNextLeg(){}
	
}
