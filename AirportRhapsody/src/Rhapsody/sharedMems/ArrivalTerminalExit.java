package Rhapsody.sharedMems;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.utils.Logger;

/**
 * Arrival Terminal Exit ientity
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class ArrivalTerminalExit {

	/**
	 * Logger for debugging
	 */
	private Logger logger;

	/**
	 * Constructor for arrival terminal exit entity
	 * @param logger
	 */
	public ArrivalTerminalExit(Logger logger) {
		this.logger=logger;
	}

	/**
	 * GoHome method to signal a passenger his rhapsody has ended
	 */
	public synchronized void goHome() {
		Passenger passenger = (Passenger) Thread.currentThread();
		passenger.setCurrentState(PassengerState.EXIT_ARRIVAL_TERMINAL);
		this.logger.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.logger.updateFDTPassengers(1, false);
	}
	
}