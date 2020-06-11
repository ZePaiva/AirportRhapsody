package Rhapsody.client.stubs;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.Passenger;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Arrival Terminal exit stub for the clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalExitStub {
	
	/**
     * Prettify
     */
	public static final String ANSI_YELLOW = "\u001B[0m\u001B[33m";

	/**
	 * Arrival exit stub constructor
	 */
    public ArrivalExitStub() {}

	/**
	 * GoHome method to signal a passenger his rhapsody has ended
	 * @param lastFlight
	 * @param departed
	 */
	public void goHome(boolean lastFlight) {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalExitHostName, RunParameters.ArrivalExitPort);
		clientCom.open();
		Passenger passenger = (Passenger) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.PASSENGER_GOING_HOME);
		pkt.setId(passenger.getPassengerId());
		pkt.setState(passenger.getCurrentState());
		pkt.setBool1(lastFlight);

		
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		passenger.setCurrentState(pkt.getState());
		
		clientCom.close();
	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public void synchBlocked() {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalExitHostName, RunParameters.ArrivalExitPort);
		clientCom.open();

		Message pkt = new Message();
		pkt.setType(MessageType.DEPARTURE_SYNCH);
		
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Getter method to obtain currentBlocked threads
	 * @return number of waiting threads in object
	 */
	public int currentBlockedPassengers() {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalExitHostName, RunParameters.ArrivalExitPort);
		clientCom.open();

		Message pkt = new Message();
		pkt.setType(MessageType.DEPARTURE_REQUEST_HOWMANY);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
		return pkt.getInt1();
	}

	/**
	 * Method to wake all threads in object monitor
	 */
	public void wakeCurrentBlockedPassengers(){
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalExitHostName, RunParameters.ArrivalExitPort);
		clientCom.open();

		Message pkt = new Message();
		pkt.setType(MessageType.DEPARTURE_REQUEST_WAKEUP);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
	}
}