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
     * Passenger ID
     */
    private int passengerId;

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
    private ArrivalLoungeStub loungeStub;

    /**
     * Arrival exit stub
     */
    private ArrivalExitStub arrivalExitStub;

    /**
     * Arrival terminal transfer quay stub
     */
    private ArrivalQuayStub arrivalQuayStub;

    /**
     * Baggage collection point stub
     */
    private BaggageCollectionStub baggageCollectionStub;

    /**
     * Baggage reclaim office stub
     */
    private BaggageReclaimStub baggageReclaimStub;

    /**
     * Departure Entrance stub
     */
    private DepartureEntranceStub departureEntranceStub;

    /**
     * Departure terminal transfer quay stub
     */
    private DepartureQuayStub departureQuayStub;

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
        this.startingBags = startingBags;
        this.currentBags = 0;
        this.currentFlight = 0;
        this.passengerId = passengerId;
        this.situations = situations;
        this.currentState = States.AT_DISEMBARKING_ZONE;
        this.arrivalExitStub = arrivalExitStub;
        this.arrivalQuayStub = arrivalQuayStub;
        this.loungeStub = arrivalLoungeStub;
        this.baggageCollectionStub = baggageCollectionStub;
        this.baggageReclaimStub = baggageReclaimStub;
        this.departureEntranceStub = departureEntranceStub;
        this.departureQuayStub = departureQuayStub;
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
		return this.passengerId;
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

        for (int i = 0; i < RunParameters.K; i++) {
            this.currentBags = 0;
            // need to implement at least life-cycles in the stubs

        }
    }
}