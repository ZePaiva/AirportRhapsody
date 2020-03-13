package Rhapsody.entities;

import Rhapsody.entities.states.PorterState;

/**
 * Porter entity data-type
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class Porter extends Thread{

	private PorterState currentState;

	public Porter(){
		this.currentState=PorterState.WAITING_FOR_PLANE_TO_LAND;
	}

	public PorterState getPorterState(){
		return this.currentState;
	}

	public void takeARest(){}
	public void noMoreBagsToCollect(){}
	public void tryToCollectABag(){}
	public void carryItToAppropriateStore(){}

}
