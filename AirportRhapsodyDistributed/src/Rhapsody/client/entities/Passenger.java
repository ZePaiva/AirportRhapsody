package Rhapsody.client.entities;

import java.lang.Thread.State;

import Rhapsody.client.stubs.ArrivalExitStub;
import Rhapsody.client.stubs.ArrivalLoungeStub;
import Rhapsody.client.stubs.ArrivalQuayStub;
import Rhapsody.client.stubs.BaggageCollectionStub;
import Rhapsody.client.stubs.BaggageReclaimStub;
import Rhapsody.client.stubs.DepartureEntranceStub;
import Rhapsody.client.stubs.DepartureQuayStub;
import Rhapsody.common.RunParameters;
import Rhapsody.common.States;

/**
 * Passenger entity code
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class Passenger extends Thread {

    /**
     * Passegner ID
     */
    private int id;

    /**
     * Starting bags for the passenger, passed on at creation
     */
    private int[] startingBags;

    /**
     * Current bags the passenger is holding
     */
    private int currentBags;

    /**
     * Current flight the passenger is in
     */
    private int currentFlight;

    /**
     * Passenger situations for each flight
     */
    private String[] situations;

    /**
     * Passenger current state
     */
    private States currentState;

    /**
     * Arrival Lounge stub
     */
    private ArrivalLoungeStub arrivalLounge;

    /**
     * Arrival exit stub
     */
    private ArrivalExitStub arrivalExit;

    /**
     * Arrival terminal transfer quay stub
     */
    private ArrivalQuayStub arrivalQuay;

    /**
     * Baggage collection point stub
     */
    private BaggageCollectionStub baggageCollection;

    /**
     * Baggage reclaim office stub
     */
    private BaggageReclaimStub baggageReclaim;

    /**
     * Departure Entrance stub
     */
    private DepartureEntranceStub departureEntrance;

    /**
     * Departure terminal transfer quay stub
     */
    private DepartureQuayStub departureQuay;

    /**
     * Prettify
     */
    public static final String ANSI_GREEN = "\u001B[0m\u001B[32m";

    /**
     * Passenger entity constructor 
     * 
     * @param passengerId
     * @param startingBags
     * @param situations
     */
    public Passenger(int passengerId, int[] startingBags, String[] situations,
                    ArrivalExitStub arrivalExitStub, ArrivalLoungeStub arrivalLoungeStub, 
                    ArrivalQuayStub arrivalQuayStub, BaggageCollectionStub baggageCollectionStub,
                    BaggageReclaimStub baggageReclaimStub, DepartureEntranceStub departureEntranceStub,
                    DepartureQuayStub departureQuayStub) {
		this.id=passengerId;
        this.startingBags = startingBags;
        this.currentBags = 0;
        this.currentFlight = 0;
        this.situations = situations;
        this.currentState = States.AT_DISEMBARKING_ZONE;
        this.arrivalExit = arrivalExitStub;
        this.arrivalQuay = arrivalQuayStub;
        this.arrivalLounge = arrivalLoungeStub;
        this.baggageCollection = baggageCollectionStub;
        this.baggageReclaim = baggageReclaimStub;
        this.departureEntrance = departureEntranceStub;
        this.departureQuay = departureQuayStub;
    }

	/**
	 * Passenger state alteration function
	 * 
	 * @param state
	 */
	public void setCurrentState(States state) {
		this.currentState = state;
	}

	/**
	 * Return's passenger current state
	 * 
	 * @return passenger state of type PassengerState
	 */
	public States getCurrentState() {
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
		return this.situations;
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

		System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is up\n", this.id);
		arrivalLounge.updateStartingBags(startingBags, situations);
		for (int flight = 0; flight < RunParameters.K; flight++){
			this.currentBags=0;
			System.out.printf(ANSI_GREEN + "[PASSENGER] P%d disembarked from flight %d | SB %d | SIT %s\n", this.id, flight, this.startingBags[flight], this.situations[flight]);
			arrivalLounge.whatShouldIDo(flight, startingBags[flight], situations[flight].equals("FDT") ? 1 : 0);
			
			// Transit passengers life-cycle
			if ( this.situations[flight].equals("TRT") ){
				// transit passenger, goes to arrival transfer quay 
				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d will go to bus stop\n", this.id);
			
				// goes to arrrival terminal transfer quay and waits until it can board
				arrivalQuay.takeABus();

				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is trying to enter the bus\n", this.id);
				// enter the bus to go to the departure terminal transfer quay
				while (!arrivalQuay.enterTheBus());

				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d entered the bus\n", this.id);
				// goes from departure terminal transfer quay to the departure termianl transfer
				departureQuay.leaveTheBus();

				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is preparing next leg of journey\n", this.id);
				
				// signal that one more is waiting in the DTE
				departureEntrance.synchBlocked();
				// get blocked in ATE
				int exited=arrivalExit.currentBlockedPassengers();
				// blocks or not in the DTE, block state depends if DTE+ATE blocked passengers is equal to amount of passengers per flight
				departureEntrance.prepareNextLeg(flight==RunParameters.K-1);
				System.out.printf("[PASSENGER] P%d exiting \n", this.id);
				// wakes all passengers currently blocked in both end-entities
				departureEntrance.wakeCurrentBlockedPassengers();
				arrivalExit.wakeCurrentBlockedPassengers();

			// Final Destination passengers life-cycle
			} else if ( this.situations[flight].equals("FDT")) {
				// final destination passenger, goes to luggage collection point
				System.out.printf(ANSI_GREEN + "[PASSENGER] P%d will terminate it's journey here\n", this.id);
				// tries to collect all bags until it collects them all or it knows it lost it's bags
				if (this.startingBags[flight]!=0) {
					this.currentBags= baggageCollection.goCollectABag(this.startingBags[flight]);
					System.out.printf(ANSI_GREEN + "[PASSENGER] P%d tried to collect a bag | CB %d | SB %d | LB %s\n", 
										this.id, this.currentBags, this.startingBags[flight], this.startingBags[flight]==this.currentBags);	
				}

				// if lost any luggage
				if ( this.currentBags!= this.startingBags[flight]) {
					baggageReclaim.reportMissingBags(this.startingBags[flight]-this.currentBags);
					System.out.printf(ANSI_GREEN + "[PASSENGER] P%d is reclaiming bags\n", this.id);
				}

				// signal that one more is waiting in the DTE
				arrivalExit.synchBlocked();
				// get blocked in ATE
				//int exited=departureEntrance.currentBlockedPassengers();
				// blocks or not in the DTE, block state depends if DTE+ATE blocked passengers is equal to amount of passengers per flight
				arrivalExit.goHome(flight==RunParameters.K-1);
				System.out.printf("[PASSENGER] P%d exiting \n", this.id);
				// wakes all passengers currently blocked in both end-entities
				departureEntrance.wakeCurrentBlockedPassengers();
				arrivalExit.wakeCurrentBlockedPassengers();
			} else {
				System.err.printf(ANSI_GREEN + "[PASSENGER] Passenger %d had wrong start", this.id);
				System.exit(5);
			}
		}
		System.out.printf(ANSI_GREEN+"[PASSENGER] P%d exiting run(), joining...\n", this.id);
    }
}