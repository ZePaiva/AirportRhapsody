package Rhapsody.server.stubs;

import Rhapsody.common.RunParameters;
import Rhapsody.common.States;

/**
 * General repository stub 
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class GeneralRepositoryStub {

    /**
     * Server name of the Arrival Exit server
     */
    private String serverHostName;

    /**
     * Server port of the Arrival Exit server
     */
    private int serverHostPort;

    /**
     *  Stub constructor
     */
    public GeneralRepositoryStub() {
        this.serverHostName=RunParameters.RepositoryHostName;
        this.serverHostPort=RunParameters.RepositoryPort;
    }

	public void clearFlight(boolean b) {
	}

	public void updateFlight(int currentFlight, boolean b) {
	}

	public void updateBagsInPlane(int size, boolean b) {
	}

	public void updatePorterState(States entityState, boolean b) {
	}

	public void updatePassengerState(States entityState, int entityID, boolean b) {
	}

	public void addPassengerToFlight(int entityID, boolean b) {
	}

	public void updatePlaneHoldBags(int startingBags, boolean b) {
	}

	public void updateCurrentBags(int entityID, int i, boolean b) {
	}

	public void updateSituation(int entityID, String situation, boolean b) {
	}

	public void updateStartingBags(int entityID, int startingBags, boolean b) {
	}

	public void addToWaitingQueue(int entityID, boolean b) {
	}

	public void removeFromWaitingQueue(boolean b) {
	}

	public void addToBusSeat(int entityID, boolean b) {
	}

	public void updateBusDriverState(States entityState, boolean b) {
	}

	public void updateConveyorBags(int size, boolean b) {
	}

	public void updateStoreRoomBags(int i, boolean b) {
	}

	public void updateLostbags(int lostBags, boolean b) {
	}

	public void updateTRTPassengers(int i, boolean b) {
	}

	public void removeFromBusSeat(boolean b) {
	}

}