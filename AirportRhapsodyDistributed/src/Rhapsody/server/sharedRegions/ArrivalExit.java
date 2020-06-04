package Rhapsody.server.sharedRegions;

import Rhapsody.client.stubs.GeneralRepositoryStub;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PassengerInterface;

/**
 * Arrival Exit entity 
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalExit {

    /**
     * General Repository stub for logging
     * 
     * @serialField generalRepository
     */
    private GeneralRepositoryStub generalRepository;

    /**
     * Passengers that finished flight
     * 
     * @serialField passengersTerminated
     */
    private int passengersTerminated;

    /**
     * Prettify
     */
    public static final String ANSI_YELLOW = "\u001B[0m\u001B[33m";
    
    /**
     * Arrival Exit constructor
     * @param gStub
     */
    public ArrivalExit(GeneralRepositoryStub gStub) {
        this.passengersTerminated=0;
        this.generalRepository=gStub;
    }

	/**
	 * GoHome method to signal a passenger his rhapsody has ended
	 * @param lastFlight
	 * @param departed
	 */
	public synchronized void goHome(boolean lastFlight, int departed) {
		
	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public synchronized void synchBlocked() {
		this.passengersTerminated++;
	}

	/**
	 * Getter method to obtain currentBlocked threads
	 * @return number of waiting threads in object
	 */
	public synchronized int currentBlockedPassengers() {
		return this.passengersTerminated;
	}

	/**
	 * Method to wake all threads in object monitor
	 */
	public synchronized void wakeCurrentBlockedPassengers(){
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		System.out.printf(ANSI_YELLOW+"[ARRTERMEX] P%d waking others \n", passenger.getEntityID());
		notifyAll();
	}




}