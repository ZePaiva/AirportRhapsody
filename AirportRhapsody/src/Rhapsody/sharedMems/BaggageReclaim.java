package Rhapsody.sharedMems;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.utils.Logger;

/**
 * Luggage reclaim office shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BaggageReclaim {

	/**
	 * Logger for debugging
	 */
	private Logger logger;

	/**
	 * BaggageReclaim constructor
	 * @param logger
	 */
	public BaggageReclaim(Logger logger) {
		this.logger=logger;
	}

	public synchronized void reportMissingBags() {
		Passenger passenger = (Passenger) Thread.currentThread();
		passenger.setCurrentState(PassengerState.AT_LUGGAGE_RECLAIM);
		this.logger.updateLostbags(passenger.getStartingBags()-passenger.getCurrentBags(), true);
		this.logger.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), false);
	}
	
}