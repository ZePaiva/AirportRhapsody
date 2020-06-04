package Rhapsody.server.sharedRegions;

import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Baggage reclaim office entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class BaggageReclaim {
    
    /**
	 * GeneralRepository for debugging
	 */
	private GeneralRepositoryStub generalRepository;

	/**
	 * BaggageReclaim constructor
	 * @param generalRepository
	 */
	public BaggageReclaim(GeneralRepositoryStub generalRepository) {
		this.generalRepository=generalRepository;
	}

	/**
	 * Method to client register his complaint about any lost luggage
	 * @param lostBags
	 */
	public synchronized void reportMissingBags(int lostBags) {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		passenger.setEntityState(States.AT_LUGGAGE_RECLAIM);
		this.generalRepository.updateLostbags(lostBags, true);
		this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), false);
	}
}