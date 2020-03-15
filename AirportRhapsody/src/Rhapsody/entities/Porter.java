package Rhapsody.entities;

import Rhapsody.entities.states.PorterState;

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
	 * 
	 */
	private int bagsInPlaneHold;

	/**
	 * Porter constructor mehtod
	 */
	public Porter(){
		this.currentState=PorterState.WAITING_FOR_PLANE_TO_LAND;
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

	public void takeARest(){}
	public void noMoreBagsToCollect(){}
	public void tryToCollectABag(){}
	public void carryItToAppropriateStore(){}

}
