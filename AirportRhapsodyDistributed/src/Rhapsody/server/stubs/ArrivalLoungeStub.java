package Rhapsody.server.stubs;

import Rhapsody.common.Luggage;
import Rhapsody.common.RunParameters;

/**
 * Arrival lounge stub, implements an interface for the clients to interact with 
 * the arrival lounge from a safe distance
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class ArrivalLoungeStub {
    
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
	public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    public ArrivalLoungeStub() {
        this.serverHostName=RunParameters.ArrivalLoungeHostName;
        this.serverHostPort=RunParameters.ArrivalLoungePort;
    }
    
	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 * @return simulationContinue
	 */
	public boolean takeARest() {
		return false;
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state. <p/>
	 * Disembarks passenger and notifies all other passengers
	 * @param flightId
	 */
	public void whatShouldIDo(int flightId) {
	}

	/**
	 * Porter method to try to collect a bag or fail and exit the bag collection loop <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM</b>  
	 * @return planeHasBags of type boolean
	 */
	public Luggage tryToCollectABag() {
		return null;
	}

	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public void endOfWork() {
	}
}