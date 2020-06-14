package Rhapsody.server.sharedRegions;

import Rhapsody.common.RunParameters;
import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.stubs.ArrivalExitStub;
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
	 * Entity to synch the exits
	 */
	private ArrivalExitStub arrivalExit;

	/**
	 * Variable to account for terminated passengers
	 */
	private int passengersTerminated;

	public static final String ANSI_RED = "\u001B[0m\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLUE = "\u001B[0m\u001B[34m";

	/**
	 * Constructor for Departure Terminal Entrance
	 * 
	 * @param generalRepository
	 * @param arrivalExitStub
	 * @param lounge
	 * @param arrivalTerminalTransfer
	 */
	public DepartureEntrance(GeneralRepositoryStub generalRepository, ArrivalExitStub arrivalExitStub) {
		this.generalRepository = generalRepository;
		this.passengersTerminated = 0;
		this.arrivalExit = arrivalExitStub;
		this.generalRepository.registerMem(5);
	}

	/**
	 * Blocking method that signals a passenger is ready to leave the airport.
	 * <p/>
	 * Internally will communicate with ArrivalTerminalExit to coordinate passengers
	 * exiting the airport.
	 * 
	 * @param lastFlight
	 */
	public synchronized void prepareNextLeg(boolean lastFlight) {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		passenger.setEntityState(States.DEPARTING);
		this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), true);
		this.generalRepository.updateTRTPassengers(1, false);
		int exited = arrivalExit.currentBlockedPassengers();
		System.out.printf(ANSI_RED + "[PASSENGER] P%d terminating... | PT %d | P %d" + ANSI_RESET + "\n",
				passenger.getEntityID(), this.passengersTerminated + exited, RunParameters.N);
		if (!(exited + this.passengersTerminated == RunParameters.N)) {
			System.out.printf(ANSI_RED + "[PASSENGER] P%d blocking \n" + ANSI_RESET + "", passenger.getEntityID());
			try {
				wait();
				System.out.printf(ANSI_RED + "[PASSENGER] P%d woke" + ANSI_RESET + "\n", passenger.getEntityID());
			} catch (InterruptedException e) {
			}
		}
		this.passengersTerminated = 0;
		// in case it is the last flight
		if (lastFlight) {
			System.out.printf(ANSI_RED + "[PASSENGER] Simulation ended" + ANSI_RESET + "\n");
			// arrivalTerminalTransfer.endOfWork();
			// arrivalLounge.endOfWork();
		}
	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public synchronized void synchBlocked() {
		System.out.printf(ANSI_BLUE + "[ARRIVALTERM] Synching " + ANSI_RESET + "\n");
		this.passengersTerminated++;
	}

	/**
	 * Method to get all waiting threads in this object monitor
	 * 
	 * @return waitingThreads
	 */
	public synchronized int currentBlockedPassengers() {
		return this.passengersTerminated;
	}

	/**
	 * Method to wake all waiting threads in this object monitor
	 */
	public synchronized void wakeCurrentBlockedPassengers() {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		System.out.printf(ANSI_RED + "[PASSENGER]  P%d waking others" + ANSI_RESET + "\n", passenger.getEntityID());
		notifyAll();
	}
}