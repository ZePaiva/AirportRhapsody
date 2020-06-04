package Rhapsody.client.stubs;

import java.util.Queue;

import Rhapsody.common.RunParameters;

/**
 * Departure terminal transfer entity for clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureQuayStub {
    
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
    public static final String ANSI_BLACK = "\033[1;37m";

    /**
     * Stub constructor
     */
    public DepartureQuayStub() {
        this.serverHostName=RunParameters.DepartureQuayHostName;
        this.serverHostPort=RunParameters.DepartureQuayPort;
    }
    
	/**
	 * Method to update passenger as a pessenger ready to embark in other adventures.
	 */
	public void leaveTheBus() {

	}

	/**
	 * Method to signl bus parking by the BusDriver entity
	 */
	public void parkTheBusAndLetPassOff(Queue<Integer> busSeats) {
		
	}

	/**
	 * method that simulates go back voyage of the BusDriver
	 */
	public void goToArrivalTerminal() {
	}
}