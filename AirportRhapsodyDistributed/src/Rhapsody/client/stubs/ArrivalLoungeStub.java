package Rhapsody.client.stubs;

import java.util.Arrays;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.Passenger;
import Rhapsody.client.entities.Porter;
import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Arrival lounge stub, implements an interface for the clients to interact with 
 * the arrival lounge from a safe distance
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class ArrivalLoungeStub {
    
    /**
	 * Client communication channelt
	 */
	private ClientCom clientCom;

    /**
     * Prettify
     */
	public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    public ArrivalLoungeStub() {
		clientCom=null;
	}
    
	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 * @return simulationContinue
	 */
	public boolean takeARest() {

		if (clientCom==null) {
			this.clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
			this.clientCom.open();	
		}
		Porter porter = (Porter) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.PORTER_WAITING);

		this.clientCom.writeObject(pkt);
		pkt = (Message) this.clientCom.readObject();
		porter.setPorterState(pkt.getState());
		return pkt.getBool1();
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state. <p/>
	 * Disembarks passenger and notifies all other passengers
	 * @param flightId
	 */
	public void whatShouldIDo(int flightId) {
		if (clientCom==null) {
			this.clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
			this.clientCom.open();	
		}
		Passenger passenger = (Passenger) Thread.currentThread();
		Message pkt = new Message();

		pkt.setType(MessageType.PASSENGER_ARRIVED);
		pkt.setId(passenger.getPassengerId());
		pkt.setState(passenger.getCurrentState());
		clientCom.writeObject(pkt);

		passenger.setCurrentState(pkt.getState());
	}

	/**
	 * Porter method to try to collect a bag or fail and exit the bag collection loop <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM</b>  
	 * @return planeHasBags of type boolean
	 */
	public Luggage tryToCollectABag() {
		if (clientCom==null) {
			this.clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
			this.clientCom.open();	
		}
		Porter porter = (Porter) Thread.currentThread();
		Message pkt = new Message();

		pkt.setType(MessageType.PORTER_COLLECT_BAG);
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		return new Luggage(pkt.getInt1(), pkt.getBool1() ? "FDT" : "TRT");
	}

	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public void endOfWork() {
		if (clientCom==null) {
			this.clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
			this.clientCom.open();	
		}
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
	}

	/**
	 * Logs passenger in the servers
	 * 
	 * @param startingBags
	 * @param situations
	 */
	public void updateStartingBags(int[] startingBags, String[] situations) {
		if (clientCom==null) {
			this.clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
			this.clientCom.open();	
		}
		Passenger passenger = (Passenger) Thread.currentThread();
		int[] sits = Arrays.asList(situations).stream().mapToInt(s -> s.equals("FDT") ? 1 : 0).toArray();
		Message pkt = new Message();
		pkt.setType(MessageType.PASSENGER_IN);
		pkt.setId(passenger.getPassengerId());
		pkt.setIntArray1(startingBags);
		pkt.setIntArray2(sits);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
	}

	/**
	 * Close the stub
	 */
	public void closeStub() {
		clientCom.close();
	}
}