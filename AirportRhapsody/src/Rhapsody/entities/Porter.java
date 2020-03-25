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

	public static final String ANSI_CYAN = "\u001B[36m";

	/**
	 * Porter constructor mehtod
	 * @param generalRepository
	 * @param storeRoom
	 * @param lounge
	 * @param baggageCollectionPoint
	 */
	public Porter(GeneralRepository generalRepository, StoreRoom storeRoom, Lounge lounge,  
					BaggageCollectionPoint baggageCollectionPoint){
		this.currentState=PorterState.WAITING_FOR_PLANE_TO_LAND;
		this.currentBag=null;
		this.planeHasBags=false;
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
		while ( this.lounge.takeARest() ) {
			System.out.println(ANSI_CYAN+"[PORTER---] Ready to accept passengers");
			while( this.lounge.tryToCollectABag() ) {
				System.out.println(ANSI_CYAN+"[PORTER---] Getting a Bag");
				if ( this.currentBag.getLuggageType().equals("TRT") ) {
					System.out.println(ANSI_CYAN+"[PORTER---] Storing bags in StoreRoom");
					this.storeRoom.carryItToAppropriateStore();
				} else if ( this.currentBag.getLuggageType().equals("FDT") ) {
					this.baggageCollectionPoint.carryItToAppropriateStore();
					System.out.println(ANSI_CYAN+"[PORTER---] Storing bags in BCP");
				}
			}
			System.out.println(ANSI_CYAN+"[PORTER---] No more bags to collect");
			this.baggageCollectionPoint.noMoreBagsToCollect();
		}
		System.out.printf(ANSI_CYAN+"[PORTER---] Porter exiting run(), joining...\n");
	}
}
