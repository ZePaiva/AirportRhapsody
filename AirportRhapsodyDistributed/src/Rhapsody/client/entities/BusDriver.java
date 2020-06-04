package Rhapsody.client.entities;

import java.util.LinkedList;
import java.util.Queue;

import Rhapsody.client.stubs.ArrivalQuayStub;
import Rhapsody.client.stubs.DepartureQuayStub;
import Rhapsody.client.stubs.GeneralRepositoryStub;
import Rhapsody.common.States;

/**
 * BusDriver entity code
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 *  @version 1.0
 */
public class BusDriver extends Thread{

    /**+
     * Bus Driver current state
     */
    private States currentState;

    /**
     * Arrival terminal transfer quay
     */
    private ArrivalQuayStub arrivalTerminalTransfer;

    /**
     * Departure terminal transfer quay 
     */
    private DepartureQuayStub departureTerminalTransfer;

    /**
     * Seats on bus, might not be occupied
     */
    private Queue<Integer> busSeats;

    /**
     * Pretty stuff
     */
    public static final String ANSI_PURPLE = "\u001B[0m\u001B[35m";

    /**
     * BusDriver constructor
     * 
     * @param busSeats
     * @param maxWait
     * @param generalRepository
     * @param arrivalTerminalTransfer
     * @param departureTerminalTransfer
     */
    public BusDriver(int busSeats, long maxWait,
            GeneralRepositoryStub generalRepository, 
						ArrivalQuayStub arrivalTerminalTransfer, 
						DepartureQuayStub departureTerminalTransfer) {
		this.currentState=States.PARKING_AT_THE_ARRIVAL_LOUNGE;
		this.arrivalTerminalTransfer=arrivalTerminalTransfer;
		this.departureTerminalTransfer=departureTerminalTransfer;
		this.busSeats=new LinkedList<>();
	}

    /**
     * Gets current BusDriver State
     * 
     * @return BusDriverCurrentState
     */
	public States getBusDriverState(){
		return this.currentState;
	}

    /**
     * Sets BusDriver state to new state
     * 
     * @param busDriverState
     */
	public void setBusDriverState(States busDriverState) {
		this.currentState=busDriverState;
	}

	@Override
	public void run() {
        System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus PARKED AT ARRIVAL AND WAITING\n");
	}
}