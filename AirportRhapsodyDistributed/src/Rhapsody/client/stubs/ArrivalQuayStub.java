package Rhapsody.client.stubs;

import java.util.Queue;

import Rhapsody.common.RunParameters;

/**
 * Arrival Terminal Transfer Quay Stub for the clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalQuayStub {
    
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
    public static final String ANSI_BLUE = "\u001B[0m\u001B[34m";

    /**
     * Arrival quay stub constructor
     */
    public ArrivalQuayStub(){
        this.serverHostName=RunParameters.ArrivalQuayHostName;
        this.serverHostPort=RunParameters.ArrivalQuayPort;
    }

	/**
	 * Mehtod to put passenger in the waiting line for the bus and signal the
	 * busdriver that
	 * <p/>
	 * he can start announcing the bus boarding if necessary
	 */
	public void takeABus() {
	}

	/**
	 * Method that inserts passenger in the bus seats and makes him wait for the end
	 * of the bus ride.
	 */
	public boolean enterTheBus() {
        return false;
    }

	/**
	 * Method used to signal BusDriver that day of work has ended
	 */
	public void endOfWork() {
		
	}

	/**
	 * Method used by the bus driver if it's day of work has ended or any passenger
	 * arrived to arrival termina ltransfer quay
	 * 
	 * @return daysWorkEnded
	 */
	public boolean hasDaysWorkEnded() {
		return false;
	}

	/**
	 * Method used by the BusDriver that is waiting for a full bus or starting time
	 */
	public void announcingBusBoarding() {
	}

	/**
	 * Method used by the BusDriver to signal he has arrived to the arrival terminal
	 * bus stop
	 */
	public void parkTheBus() {
	}

	/**
	 * Method to simulate the bus voyage
	 */
	public Queue<Integer> goToDepartureTerminal() {
		return null;
	}

}