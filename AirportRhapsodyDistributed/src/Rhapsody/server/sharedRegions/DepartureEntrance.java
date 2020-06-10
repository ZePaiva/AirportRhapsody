package Rhapsody.server.sharedRegions;

import Rhapsody.common.RunParameters;
import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.stubs.ArrivalExitStub;
import Rhapsody.server.stubs.ArrivalLoungeStub;
import Rhapsody.server.stubs.ArrivalQuayStub;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Departure terminal entrance entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureEntrance {

	/**
	 * Logger entity
	 */
	private GeneralRepositoryStub generalRepository;

	/**
	 * Entity to signal porter simulation has ended
	 */
	private ArrivalLoungeStub arrivalLounge;

	/**
	 * Entity to signal busdriver simulation has ended
	 */
	private ArrivalQuayStub arrivalTerminalTransfer;

	/**
	 * Entity to synch the exits
	 */
	private ArrivalExitStub arrivalExit;
	
	/**
	 * Variable to account for terminated passengers
	 */
	private int passengersTerminated;

	public static final String ANSI_RED = "\u001B[0m\u001B[31m";

	/**
	 * Constructor for DepartureTerminalEntrance
	 * 
	 * @param generalRepository
	 * @param arrivalTerminalExit
	 * @param lounge
	 * @param arrivalTerminalTransfer
	 * @param passengers
	 */
	public DepartureEntrance(GeneralRepositoryStub generalRepository, 
								ArrivalExitStub arrivalExitStub,
                                ArrivalLoungeStub lounge, 
                                ArrivalQuayStub arrivalTerminalTransfer) {
		this.generalRepository=generalRepository;
		this.arrivalLounge=lounge;
		this.arrivalTerminalTransfer=arrivalTerminalTransfer;
		this.passengersTerminated=0;
		this.arrivalExit=arrivalExitStub;
		this.generalRepository.registerMem(5);
	}

	/**
	 * Blocking method that signals a passenger is ready to leave the airport. <p/>
	 * Internally will communicate with ArrivalTerminalExit to coordinate passengers exiting the airport.
	 * @param lastFlight
	 * @param exited
	 */
	public synchronized void prepareNextLeg(boolean lastFlight) {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		passenger.setEntityState(States.DEPARTING);
		this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), true);
		this.generalRepository.updateTRTPassengers(1, false);
		int exited = arrivalExit.currentBlockedPassengers();
		System.out.printf(ANSI_RED+"[DEPTERMEN] P%d terminating... | PT %d | P %d\n", passenger.getEntityID(), this.passengersTerminated+exited, RunParameters.N);
		if (!(exited+this.passengersTerminated==RunParameters.N)) {
			System.out.printf(ANSI_RED+"[DEPTERMEN] P%d blocking \n", passenger.getEntityID());
			try {
				wait();
				System.out.printf(ANSI_RED+"[DEPTERMEN] P%d woke \n", passenger.getEntityID());
			} catch (InterruptedException e) {}	
		}
		this.passengersTerminated=0;
		// in case it is the last flight
		if ( lastFlight ) {
			System.out.printf(ANSI_RED+"[DEPTERMEN] Simulation ended\n");
			arrivalTerminalTransfer.endOfWork();
			arrivalLounge.endOfWork();
		}
	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public synchronized void synchBlocked() {
		this.passengersTerminated++;
	}

	/**
	 * Method to get all waiting threads in this object monitor
	 * @return waitingThreads
	 */
	public synchronized int currentBlockedPassengers() {
		return this.passengersTerminated;
	}

	/**
	 * Method to wake all waiting threads in this object monitor
	 */
	public synchronized void wakeCurrentBlockedPassengers(){
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		System.out.printf(ANSI_RED+"[DEPTERMEN] P%d waking others\n", passenger.getEntityID());
		notifyAll();
	}
}