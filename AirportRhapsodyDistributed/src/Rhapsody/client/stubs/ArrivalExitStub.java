package Rhapsody.client.stubs;

import Rhapsody.common.RunParameters;

/**
 * Arrival Terminal exit stub for the clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalExitStub {
   
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
	public static final String ANSI_YELLOW = "\u001B[0m\u001B[33m";

	/**
	 * Arrival exit stub constructor
	 */
    public ArrivalExitStub() {
        this.serverHostName=RunParameters.ArrivalExitHostName;
        this.serverHostPort=RunParameters.ArrivalExitPort;
    }

	/**
	 * GoHome method to signal a passenger his rhapsody has ended
	 * @param lastFlight
	 * @param departed
	 */
	public void goHome(boolean lastFlight, int departed) {

	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public void synchBlocked() {
		
	}

	/**
	 * Getter method to obtain currentBlocked threads
	 * @return number of waiting threads in object
	 */
	public int currentBlockedPassengers() {
		return 0;
	}

	/**
	 * Method to wake all threads in object monitor
	 */
	public void wakeCurrentBlockedPassengers(){
		
	}
}