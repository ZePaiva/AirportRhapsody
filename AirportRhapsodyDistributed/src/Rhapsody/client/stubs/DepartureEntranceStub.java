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
	 * Client communication channelt
	 */
	private final ClientCom clientCom;

    /**
     * Prettify
     */
    public static final String ANSI_RED = "\u001B[0m\u001B[31m";

    /**
     * Departure entrance stub constructor
     */
    public DepartureEntranceStub() {
		clientCom = new ClientCom(RunParameters.DepartureEntranceHostName, RunParameters.DepartureEntrancePort);
		clientCom.open();
    }


	/**
	 * Blocking method that signals a passenger is ready to leave the airport. <p/>
	 * Internally will communicate with ArrivalTerminalExit to coordinate passengers exiting the airport.
	 * @param lastFlight
	 * @param exited
	 */
	public void prepareNextLeg(boolean lastFlight) {
		Passenger passenger = (Passenger) Thread.currentThread();
        Message pkt = new Message();
        pkt.setType(MessageType.PASSENGER_NEXT_FLIGHT);
        pkt.setId(passenger.getPassengerId());
        pkt.setBool1(lastFlight);

        clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		
		passenger.setCurrentState(pkt.getState());
	}

	/**
	 * Method to increment the number of passengers that terminated in this monitor
	 */
	public void synchBlocked() {
		Message pkt = new Message();
		pkt.setType(MessageType.ATE_SYNCH);
		
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
	}

	/**
	 * Method to get all waiting threads in this object monitor
	 * @return waitingThreads
	 */
	public int currentBlockedPassengers() {
		Message pkt = new Message();
		pkt.setType(MessageType.ATE_REQUEST_HOWMANY);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		return pkt.getInt1();
	}

	/**
	 * Method to wake all waiting threads in this object monitor
	 */
	public void wakeCurrentBlockedPassengers(){
		Message pkt = new Message();
		pkt.setType(MessageType.ATE_REQUEST_WAKEUP);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		
	}

	/**
	 * Close the stub
	 */
	public void closeStub() {
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);
		clientCom.writeObject(pkt);
		clientCom.close();
	}
}