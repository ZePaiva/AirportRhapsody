package Rhapsody.sharedMems;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.states.PassengerState;

/**
 * Luggage reclaim office shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BaggageReclaim {

	/**
	 * GeneralRepository for debugging
	 */
	private GeneralRepository generalRepository;

	/**
	 * BaggageReclaim constructor
	 * @param generalRepository
	 */
	public BaggageReclaim(GeneralRepository generalRepository) {
		this.generalRepository=generalRepository;
	}

	/**
	 * Method to client register his complaint about any lost luggage
	 * @param lostBags
	 */
	public synchronized void reportMissingBags(int lostBags) {
		Passenger passenger = (Passenger) Thread.currentThread();
		passenger.setCurrentState(PassengerState.AT_LUGGAGE_RECLAIM);
		this.generalRepository.updateLostbags(lostBags, true);
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), false);
	}
	
}