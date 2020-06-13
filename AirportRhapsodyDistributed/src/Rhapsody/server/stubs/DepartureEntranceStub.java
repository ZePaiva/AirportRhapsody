package Rhapsody.server.stubs;

import Rhapsody.server.communications.ClientCom;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Departure entrance stub class for the server
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureEntranceStub {
    
    /**
     * Prettify
     */
    public static final String ANSI_RED = "\u001B[0m\u001B[31m";

    /**
     * Departure entrance stub constructor
     */
    public DepartureEntranceStub() {
    }

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public void synchBlocked() {
		ClientCom clientCom = new ClientCom(RunParameters.DepartureEntranceHostName, RunParameters.DepartureEntrancePort);
		while (!clientCom.open()) {
			System.out.println("Departure Entrance not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		Message pkt = new Message();
		pkt.setType(MessageType.ATE_SYNCH);
		
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Method to get all waiting threads in this object monitor
	 * @return waitingThreads
	 */
	public int currentBlockedPassengers() {
		ClientCom clientCom = new ClientCom(RunParameters.DepartureEntranceHostName, RunParameters.DepartureEntrancePort);
		while (!clientCom.open()) {
			System.out.println("Departure Entrance not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		
		Message pkt = new Message();
		pkt.setType(MessageType.ATE_REQUEST_HOWMANY);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
		return pkt.getInt1();
	}

	/**
	 * Method to wake all waiting threads in this object monitor
	 */
	public void wakeCurrentBlockedPassengers(){
		ClientCom clientCom = new ClientCom(RunParameters.DepartureEntranceHostName, RunParameters.DepartureEntrancePort);
		while (!clientCom.open()) {
			System.out.println("Departure Entrance not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		
		Message pkt = new Message();
		pkt.setType(MessageType.ATE_REQUEST_WAKEUP);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Close the stub
	 */
	public void closeStub() {
		ClientCom clientCom = new ClientCom(RunParameters.DepartureEntranceHostName, RunParameters.DepartureEntrancePort);
		while (!clientCom.open()) {
			System.out.println("Departure Entrance not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);
		clientCom.writeObject(pkt);
		clientCom.readObject();
		clientCom.close();
	}
}