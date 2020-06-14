package Rhapsody.server.sharedRegions;

import Rhapsody.server.stubs.GeneralRepositoryStub;
import Rhapsody.common.RunParameters;
import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.stubs.DepartureEntranceStub;

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
	 * Departure terminal entrance stub
	 */
	private DepartureEntranceStub departureEntrance;

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
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_PASSENGER = "\u001B[0m\u001B[32m";

	/**
	 * Arrival Exit constructor
	 * 
	 * @param gStub
	 */
	public ArrivalExit(GeneralRepositoryStub gStub, DepartureEntranceStub dStub) {
		this.passengersTerminated = 0;
		this.generalRepository = gStub;
		this.departureEntrance = dStub;
		this.generalRepository.registerMem(1);
	}

	/**
	 * GoHome method to signal a passenger his rhapsody has ended
	 * 
	 * @param lastFlight
	 */
	public synchronized void goHome(boolean lastFlight) {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		passenger.setEntityState(States.EXIT_ARRIVAL_TERMINAL);
		this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), true);
		this.generalRepository.updateFDTPassengers(1, false);
		int departed = departureEntrance.currentBlockedPassengers();
		System.out.printf(ANSI_PASSENGER + "[PASSENGER] P%d terminating... | PT %d | P %d" + ANSI_RESET + "\n",
				passenger.getEntityID(), this.passengersTerminated + departed, RunParameters.N);
		if (!(this.passengersTerminated + departed == RunParameters.N)) {
			System.out.printf(ANSI_PASSENGER + "[PASSENGER] P%d blocking " + ANSI_RESET + "\n",
					passenger.getEntityID());
			try {
				wait();
				System.out.printf(ANSI_PASSENGER + "[PASSENGER] P%d woke " + ANSI_RESET + "\n",
						passenger.getEntityID());
			} catch (InterruptedException e) {
			}
		}
		this.passengersTerminated = 0;
		// in case it is the last flight
		if (lastFlight) {
			System.out.printf(ANSI_PASSENGER + "[PASSENGER] Simulation ended" + ANSI_RESET + "\n");
			// arrivalQuay.endOfWork();
			// arrivalLounge.endOfWork();
		}
	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public synchronized void synchBlocked() {
		System.out.printf(ANSI_YELLOW + "[DEPTERMEN] Synching" + ANSI_RESET + "\n");
		this.passengersTerminated++;
	}

	/**
	 * Getter method to obtain currentBlocked threads
	 * 
	 * @return number of waiting threads in object
	 */
	public synchronized int currentBlockedPassengers() {
		return this.passengersTerminated;
	}

	/**
	 * Method to wake all threads in object monitor
	 */
	public synchronized void wakeCurrentBlockedPassengers() {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		System.out.printf(ANSI_PASSENGER + "[DEPTERMEN] P%d waking others " + ANSI_RESET + "\n",
				passenger.getEntityID());
		notifyAll();
	}

}