package Rhapsody.sharedMems;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.states.PassengerState;

/**
 * Departure Terminal Entrance shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class DepartureTerminalEntrance {

	/**
	 * Amount of passengers in a flight
	 */
	private final int passengers;

	/**
	 * Logger entity
	 */
	private GeneralRepository generalRepository;

	/**
	 * Entity to signal porter simulation has ended
	 */
	private Lounge arrivalLounge;

	/**
	 * Entity to signal busdriver simulation has ended
	 */
	private ArrivalTerminalTransfer arrivalTerminalTransfer;
	
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
	public DepartureTerminalEntrance(GeneralRepository generalRepository, Lounge lounge, 
										ArrivalTerminalTransfer arrivalTerminalTransfer, int passengers) {
		this.generalRepository=generalRepository;
		this.arrivalLounge=lounge;
		this.arrivalTerminalTransfer=arrivalTerminalTransfer;
		this.passengers=passengers;
		this.passengersTerminated=0;
	}

	/**
	 * Blocking method that signals a passenger is ready to leave the airport. <p/>
	 * Internally will communicate with ArrivalTerminalExit to coordinate passengers exiting the airport.
	 * @param lastFlight
	 * @param exited
	 */
	public synchronized void prepareNextLeg(boolean lastFlight, int exited) {
		Passenger passenger = (Passenger) Thread.currentThread();
		passenger.setCurrentState(PassengerState.DEPARTING);
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.generalRepository.updateTRTPassengers(1, false);
		System.out.printf(ANSI_RED+"[DEPTERMEN] P%d terminating... | PT %d | P %d\n", passenger.getPassengerId(), this.passengersTerminated+exited, this.passengers);
		if (!(exited+this.passengersTerminated==this.passengers)) {
			System.out.printf(ANSI_RED+"[DEPTERMEN] P%d blocking \n", passenger.getPassengerId());
			try {
				wait();
				System.out.printf(ANSI_RED+"[DEPTERMEN] P%d woke \n", passenger.getPassengerId());
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
		Passenger passenger = (Passenger) Thread.currentThread();
		System.out.printf(ANSI_RED+"[DEPTERMEN] P%d waking others\n", passenger.getPassengerId());
		notifyAll();
	}
}