package Rhapsody.entities;

import Rhapsody.entities.states.PorterState;
import Rhapsody.sharedMems.BaggageCollectionPoint;
import Rhapsody.sharedMems.GeneralRepository;
import Rhapsody.sharedMems.Lounge;
import Rhapsody.sharedMems.StoreRoom;
import Rhapsody.utils.Luggage;

/**
 * Porter entity data-type
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class Porter extends Thread{

	/**
	 * Porter current state
	 */
	private PorterState currentState;

	/**
	 * Bag that the porter is holding
	 */
	private Luggage currentBag;

	/**
	 * General repository of information (plane hol mostly)
	 */
	private GeneralRepository generalRepository;

	/**
	 * StoreRoom entity
	 */
	private StoreRoom storeRoom;

	/**
	 * Baggage collection entity
	 */
	private BaggageCollectionPoint baggageCollectionPoint;

	/**
	 * Lounge entity
	 */
	private Lounge lounge;

	/**
	 * While true the porter will try to get more bags
	 */
	private boolean planeHasBags;

	/**
	 * number of flights for this simulation
	 */
	private final int flights;

	public static final String ANSI_CYAN = "\u001B[36m";

	/**
	 * Porter constructor mehtod
	 * @param generalRepository
	 * @param storeRoom
	 * @param lounge
	 * @param baggageCollectionPoint
	 * @param flights
	 */
	public Porter(GeneralRepository generalRepository, StoreRoom storeRoom, Lounge lounge,  
					BaggageCollectionPoint baggageCollectionPoint, int flights){
		this.currentState=PorterState.WAITING_FOR_PLANE_TO_LAND;
		this.currentBag=null;
		this.planeHasBags=false;
		this.flights=flights;
		this.baggageCollectionPoint=baggageCollectionPoint;
		this.generalRepository=generalRepository;
		this.storeRoom=storeRoom;
		this.lounge=lounge;
	}

	/**
	 * Returns porter current state
	 * @return porter state of type PorterState
	 */
	public PorterState getPorterState(){
		return this.currentState;
	}

	/**
	 * Sets porter current state to the pretended state
	 * @param state
	 */
	public void setPorterState(PorterState state) {
		this.currentState=state;
	}

	/**
	 * Sets the porter current luggage holding
	 * @param luggage
	 */
	public void setCurrentLuggage(Luggage luggage) {
		this.currentBag=luggage;
	}

	/**
	 * Returns the bag porter is currently holding
	 * @return bag of type Luggage
	 */
	public Luggage getCurrentLuggage() {
		return this.currentBag;
	}

	/**
	 * Set if there are still any bags on the plane, else unsets
	 * @param has
	 */
	public void planeHasBags(boolean has) {
		this.planeHasBags = has;
	}


	/**
	 * Porter life-cycle
	 */
	@Override
	public void run() {
		//System.out.printf(ANSI_CYAN+"Porter with id %d is up\n", (int) this.getId() );
		for (int flights = 1; flights <= this.flights; flights++) {
			// clears previous flight data
			this.generalRepository.clearFlight(flights);
			System.out.println(ANSI_CYAN+"[PORTER---] Ready to accept passengers from FLIGHT "+flights);
			// wait for all passengers to disembark
			this.lounge.takeARest();

			System.out.printf(ANSI_CYAN+"[PORTER---] Starting to collect bags\n");

			// baggage collection and transportation
			this.generalRepository.tryToCollectABag();
			while(this.planeHasBags) {
				this.generalRepository.carryItToAppropriateStore();
				if (this.currentBag.getLuggageType().equals("TRT")) {
					this.storeRoom.carryItToAppropriateStore();
					System.out.println(ANSI_CYAN+"[PORTER---] Stored bag in STORAGE ROOM for TRT passenger");
				} else if (this.currentBag.getLuggageType().equals("FDT")) {
					this.baggageCollectionPoint.carryItToAppropriateStore();
					System.out.println(ANSI_CYAN+"[PORTER---] Stored bag in CONVEYOR BELT for FDT passenger");
				} else {
					System.err.print("Bad type of luggage, someone is trying to hack us");
				}
				this.generalRepository.tryToCollectABag();
			}

			// wakes up all passengers waiting for the luggage to be disembarkeed
			this.baggageCollectionPoint.noMoreBagsToCollect();
			System.out.println(ANSI_CYAN+"[PORTER---] No more bags to collect for FLIGHT "+flights);

			// reset thyself for the future
			this.resetSelf();

		}

		// signal that all flights have ended
		this.generalRepository.allFlightsEnded();
		System.out.print("[PORTER---] Terminating porter thread, joining...\n");
	}

	private void resetSelf() {
		this.currentBag=null;
		this.planeHasBags=true;
	}
}
