package Rhapsody.sharedMems;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.states.PassengerState;

/**
 * Arrival Terminal Exit ientity
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class ArrivalTerminalExit {

	/**
	 * Logger for debugging
	 */
	private GeneralRepository generalRepository;

	/**
	 * Entity where BusDriver waits for kill signal
	 */
	private ArrivalTerminalTransfer arrivalTerminalTransfer;

	/**
	 * Entity where Porter waits for kill signal
	 */
	private Lounge arrivalLounge;

	/**
	 * Departure entrance entity, used for the cross-communication channel
	 */
	private DepartureTerminalEntrance departureTerminalEntrance;

	/**
	 * Amount of passengers per flight, usefull to restart the simultion loop
	 */
	private final int passengers;

	/**
	 * Amount of passengers that have terminated their life-cycle
	 */
	private int passengersTerminated;

	public static final String ANSI_YELLOW = "\u001B[0m\u001B[33m";

	/**
	 * Constructor for arrival terminal exit entity
	 * 
	 * @param generalRepository
	 * @param passengers
	 * @param arrivalTerminalTransfer
	 * @param arrivalLounge
	 * @param departureTerminalEntrance
	 */
	public ArrivalTerminalExit(GeneralRepository generalRepository, int passengers, 
								ArrivalTerminalTransfer arrivalTerminalTransfer, Lounge arrivalLounge, 
								DepartureTerminalEntrance departureTerminalEntrance) {
		this.generalRepository=generalRepository;
		this.arrivalTerminalTransfer=arrivalTerminalTransfer;
		this.arrivalLounge=arrivalLounge;
		this.departureTerminalEntrance=departureTerminalEntrance;
		this.passengers=passengers;
		this.passengersTerminated=0;
	}

	/**
	 * GoHome method to signal a passenger his rhapsody has ended
	 * @param lastFlight
	 * @param departed
	 */
	public synchronized void goHome(boolean lastFlight, int departed) {
		Passenger passenger = (Passenger) Thread.currentThread();
		passenger.setCurrentState(PassengerState.EXIT_ARRIVAL_TERMINAL);
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.generalRepository.updateFDTPassengers(1, false);
		this.passengersTerminated++;
		System.out.printf(ANSI_YELLOW+"[ARRTERMEX] P%d terminating... | PT %d | P %d\n", passenger.getPassengerId(), this.passengersTerminated+departed, this.passengers);
		if (!(this.passengersTerminated+departed==this.passengers)) {
			System.out.printf(ANSI_YELLOW+"[ARRTERMEX] P%d blocking \n", passenger.getPassengerId());
			try {
				wait();
				System.out.printf(ANSI_YELLOW+"[ARRTERMEX] P%d woke \n", passenger.getPassengerId());
			} catch (InterruptedException e) {}
		}
		// in case it is the last flight
		if ( lastFlight ) {
			System.out.printf(ANSI_YELLOW+"[ARRTERMEX] Simulation ended\n");
			arrivalTerminalTransfer.endOfWork();
			arrivalLounge.endOfWork();
		}
	}

	/**
	 * ArrivalTerminalTransfer setter
	 * @param arrivalTerminalTransfer
	 */
	public void setArrivalTerminalTransfer(ArrivalTerminalTransfer arrivalTerminalTransfer) {
		this.arrivalTerminalTransfer = arrivalTerminalTransfer;
	}

	/**
	 * DepartureTerminalEntrance setter
	 * @param departureTerminalEntrance
	 */
	public void setDepartureTerminalEntrance(DepartureTerminalEntrance departureTerminalEntrance) {
		this.departureTerminalEntrance = departureTerminalEntrance;
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
		Passenger passenger = (Passenger) Thread.currentThread();
		System.out.printf(ANSI_YELLOW+"[ARRTERMEX] P%d waking others \n", passenger.getPassengerId());
		this.passengersTerminated=0;
		notifyAll();
	}
}