package Rhapsody.client.entities;

import java.util.LinkedList;
import java.util.Queue;

import Rhapsody.client.stubs.ArrivalQuayStub;
import Rhapsody.client.stubs.DepartureQuayStub;
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
    public BusDriver(long maxWait, 
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
      while(arrivalTerminalTransfer.hasDaysWorkEnded())  {
        // starts boarding process
        System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus Driver is announcing boarding\n");
        arrivalTerminalTransfer.announcingBusBoarding();
        
        // starts voyage to departure terminal
        System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus is going to DEPARTURE TERMINAL\n");
        this.busSeats=arrivalTerminalTransfer.goToDepartureTerminal();

        // arrives at the departure terminal and waits until all passengers exit the bus
        System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus PARKED AT DEPARTURE AND WAITNG\n");
        departureTerminalTransfer.parkTheBusAndLetPassOff(this.busSeats);

        // starts voyage to the arrival terminal
        System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus going to ARRIVAL TERMINAL\n");
        departureTerminalTransfer.goToArrivalTerminal();

        // arrives to the arrival terminal and alerts that the bus has arrived to the clients
        System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus PARKED AT ARRIVAL AND WAITING\n");
        arrivalTerminalTransfer.parkTheBus();
      }
    }
}