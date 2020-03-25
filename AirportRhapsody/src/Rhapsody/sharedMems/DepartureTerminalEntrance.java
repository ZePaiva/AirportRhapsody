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
	 * Cross comunication entity
	 */
	private ArrivalTerminalExit arrivalTerminalExit;

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

	/**
	 * Constructor for DepartureTerminalEntrance
	 * 
	 * @param generalRepository
	 * @param arrivalTerminalExit
	 * @param lounge
	 * @param arrivalTerminalTransfer
	 * @param passengers
	 */
	public DepartureTerminalEntrance(GeneralRepository generalRepository, 
										ArrivalTerminalExit arrivalTerminalExit, Lounge lounge, 
										ArrivalTerminalTransfer arrivalTerminalTransfer, int passengers) {
		this.generalRepository=generalRepository;
		this.arrivalTerminalExit=arrivalTerminalExit;
		this.arrivalLounge=lounge;
		this.arrivalTerminalTransfer=arrivalTerminalTransfer;
		this.passengers=passengers;
		this.passengersTerminated=0;
	}

	/**
	 * Blocking method that signals a passenger is ready to leave the airport. <p/>
	 * Internally will communicate with ArrivalTerminalExit to coordinate passengers exiting the airport.
	 */
	public synchronized void prepareNextLeg(boolean lastFlight) {
		Passenger passenger = (Passenger) Thread.currentThread();
		passenger.setCurrentState(PassengerState.DEPARTING);
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.generalRepository.updateFDTPassengers(1, false);
		this.passengersTerminated++;
		this.arrivalTerminalExit.incrementTerminatedPassengers();
		while(this.passengersTerminated < this.passengers) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		// in case it is the last flight
		if ( lastFlight ) {
			arrivalTerminalTransfer.endOfWork();
			arrivalLounge.endOfWork();
		}
	}

	/**
	 * Method to allow Arrival Termina Exit to alert when a passenger termianted
	 */
	public synchronized void incrementTerminatedPassengers() {
		this.passengersTerminated++;
		notifyAll();

	}
}