package Rhapsody.client.entities;

import Rhapsody.client.stubs.ArrivalLoungeStub;
import Rhapsody.client.stubs.BaggageCollectionStub;
import Rhapsody.client.stubs.StorageAreaStub;
import Rhapsody.common.Luggage;
import Rhapsody.common.States;

/**
 * Porter entity data-type
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class Porter extends Thread {

    /**
     * Porter current state
     */
    private States currentState;

    /**
     * Bag that the porter is holding
     */
    private Luggage currentBag;

    /**
     * StoreRoom entity
     */
    private StorageAreaStub storeRoom;

    /**
     * Baggage collection entity
     */
    private BaggageCollectionStub baggageCollectionPoint;

    /**
     * Lounge entity
     */
    private ArrivalLoungeStub lounge;

    public static final String ANSI_CYAN = "\u001B[0m\u001B[36m";

    /**
     * Porter constructor mehtod
     * 
     * @param generalRepository
     * @param storeRoom
     * @param lounge
     * @param baggageCollectionPoint
     */
	public Porter(StorageAreaStub storeRoom, ArrivalLoungeStub lounge, 
					BaggageCollectionStub baggageCollectionPoint){
		this.currentState=States.WAITING_FOR_PLANE_TO_LAND;
		this.currentBag=null;
		this.baggageCollectionPoint=baggageCollectionPoint;
		this.storeRoom=storeRoom;
		this.lounge=lounge;
	}

	/**
	 * Returns porter current state
	 * @return porter state of type PorterState
	 */
	public States getPorterState(){
		return this.currentState;
	}

	/**
	 * Sets porter current state to the pretended state
	 * @param state
	 */
	public void setPorterState(States state) {
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
	 * Porter life-cycle
	 */
	public void run() {
		System.out.println(ANSI_CYAN+"[PORTER---] Porter is up");
		while ( lounge.takeARest() ) {
			System.out.println(ANSI_CYAN+"[PORTER---] Ready to handle bags");
			this.currentBag = lounge.tryToCollectABag();
			while( this.currentBag != null ) {
				System.out.printf(ANSI_CYAN+"[PORTER---] Has bag %s\n", this.currentBag.toString());
				if ( currentBag.getLuggageType().equals("TRT") ) {
					System.out.println(ANSI_CYAN+"[PORTER---] Storing bags in StoreRoom");
					storeRoom.carryItToAppropriateStore(this.currentBag);
				} else if ( currentBag.getLuggageType().equals("FDT") ) {
					baggageCollectionPoint.carryItToAppropriateStore(this.currentBag);
					System.out.println(ANSI_CYAN+"[PORTER---] Storing bags in BCP");
				}
				this.currentBag=null;
				this.currentBag = lounge.tryToCollectABag();
			}
			System.out.println(ANSI_CYAN+"[PORTER---] No more bags to collect");
			baggageCollectionPoint.noMoreBagsToCollect();
			System.out.println(ANSI_CYAN+"[PORTER---] Porter is sleeping");
		}
		System.out.printf(ANSI_CYAN+"[PORTER---] Porter exiting run(), joining...\n");
	}
}