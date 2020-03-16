package Rhapsody.entities;

import Rhapsody.entities.states.PorterState;
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
	 * While true the porter will try to get more bags
	 */
	private boolean planeHasBags;

	/**
	 * Porter constructor mehtod
	 */
	public Porter(){
		this.currentState=PorterState.WAITING_FOR_PLANE_TO_LAND;
		this.currentBag=null;
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

	@Override
	public void run() {
		System.out.printf("Porter with id %l is up", this.getId() );
	}
}
