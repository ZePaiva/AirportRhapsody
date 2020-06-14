package Rhapsody.server.stubs;

import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ClientCom;

/**
 * Arrival Terminal exit stub for the cross comms
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
	public ArrivalExitStub() {

	}

	/**
	 * Getter method to obtain currentBlocked threads
	 * 
	 * @return number of waiting threads in object
	 */
	public int currentBlockedPassengers() {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalExitHostName, RunParameters.ArrivalExitPort);
		while (!clientCom.open()) {
			System.out.println("Arrival Exit not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		;

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
	public void wakeCurrentBlockedPassengers() {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalExitHostName, RunParameters.ArrivalExitPort);
		while (!clientCom.open()) {
			System.out.println("Arrival Exit not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		;

		Message pkt = new Message();
		pkt.setType(MessageType.DEPARTURE_REQUEST_WAKEUP);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
	}
}