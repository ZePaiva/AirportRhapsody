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

	public static final String ANSI_PURPLE = "\u001B[35m";
	public BusDriver(){
		this.currentState=BusDriverState.PARKING_AT_THE_ARRIVAL_LOUNGE;
	}

	public BusDriverState getBusDriverState(){
		return this.currentState;
	}

	@Override
	public void run() {
		System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus Driver is up\n");
	}
}
