package Rhapsody.client.stubs;

import Rhapsody.common.RunParameters;

/**
 * Departure entrance stub class for the clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureEntranceStub {
    
    /**
     * Server name of the Arrival Exit server
     */
    private String serverHostName;

    /**
     * Server port of the Arrival Exit server
     */
    private int serverHostPort;

    /**
     * Prettify
     */
    public static final String ANSI_RED = "\u001B[0m\u001B[31m";

    /**
     * Departure entrance stub constructor
     */
    public DepartureEntranceStub() {
        this.serverHostName=RunParameters.DepartureEntranceHostName;
        this.serverHostPort=RunParameters.DepartureEntrancePort;
    }


	/**
	 * Blocking method that signals a passenger is ready to leave the airport. <p/>
	 * Internally will communicate with ArrivalTerminalExit to coordinate passengers exiting the airport.
	 * @param lastFlight
	 * @param exited
	 */
	public void prepareNextLeg(boolean lastFlight, int exited) {

	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public void synchBlocked() {
		
	}

	/**
	 * Method to get all waiting threads in this object monitor
	 * @return waitingThreads
	 */
	public int currentBlockedPassengers() {
		return 0;
	}

	/**
	 * Method to wake all waiting threads in this object monitor
	 */
	public void wakeCurrentBlockedPassengers(){
		
	}
}