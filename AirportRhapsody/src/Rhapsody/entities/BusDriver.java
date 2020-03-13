package Rhapsody.entities;

import Rhapsody.entities.states.BusDriverState;

/**
 * Bus Driver entity data-type
 * 
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BusDriver extends Thread {
	
	private BusDriverState currentState;

	public BusDriver(){
		this.currentState=BusDriverState.PARKING_AT_THE_ARRIVAL_LOUNGE;
	}

	public BusDriverState getBusDriverState(){
		return this.currentState;
	}

	@Override
	public void run() {}
	
	public void hasDaysWorkEnded(){}
	public void announcingBusBoarding(){}
	public void goToDepartureTerminal(){}
	public void goToArrivalTerminal(){}
	public void parkTheBusAndLetPassOff(){}
	public void parkTheBus(){}
}
