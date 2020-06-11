package Rhapsody.client.stubs;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.Passenger;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Departure entrance stub class for the clients
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
	 * Blocking method that signals a passenger is ready to leave the airport. <p/>
	 * Internally will communicate with ArrivalTerminalExit to coordinate passengers exiting the airport.
	 * @param lastFlight
	 * @param exited
	 */
	public void prepareNextLeg(boolean lastFlight) {
		ClientCom clientCom = new ClientCom(RunParameters.DepartureEntranceHostName, RunParameters.DepartureEntrancePort);
		clientCom.open();
		Passenger passenger = (Passenger) Thread.currentThread();
        Message pkt = new Message();
        pkt.setType(MessageType.PASSENGER_NEXT_FLIGHT);
        pkt.setId(passenger.getPassengerId());
        pkt.setBool1(lastFlight);

        clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();		
		passenger.setCurrentState(pkt.getState());
	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public void synchBlocked() {
		ClientCom clientCom = new ClientCom(RunParameters.DepartureEntranceHostName, RunParameters.DepartureEntrancePort);
		clientCom.open();
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
		clientCom.open();
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
		clientCom.open();
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
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);
		clientCom.writeObject(pkt);
		clientCom.readObject();
		clientCom.close();
	}
}