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
     * Prettify
     */
	public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    public ArrivalLoungeStub() {}
    
	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 * @return simulationContinue
	 */
	public boolean takeARest() {

		ClientCom clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
		clientCom.open();
		Porter porter = (Porter) Thread.currentThread();
		Message pkt = new Message();
		pkt.setState(porter.getPorterState());
		pkt.setType(MessageType.PORTER_WAITING);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		porter.setPorterState(pkt.getState());
		clientCom.close();
		return pkt.getBool1();
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state. <p/>
	 * Disembarks passenger and notifies all other passengers
	 * @param flightId
	 * @param startingBags
	 * @param situation
	 */
	public void whatShouldIDo(int flightId, int sb, int sit) {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
		clientCom.open();
		
		Passenger passenger = (Passenger) Thread.currentThread();
		Message pkt = new Message();

		pkt.setType(MessageType.PASSENGER_ARRIVED);
		pkt.setId(passenger.getPassengerId());
		pkt.setState(passenger.getCurrentState());
		pkt.setInt1(sb);
		pkt.setInt2(sit);
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		passenger.setCurrentState(pkt.getState());
		clientCom.close();
	}

	/**
	 * Porter method to try to collect a bag or fail and exit the bag collection loop <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM</b>  
	 * @return planeHasBags of type boolean
	 */
	public Luggage tryToCollectABag() {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
		clientCom.open();
		Porter porter = (Porter) Thread.currentThread();
		Message pkt = new Message();

		pkt.setType(MessageType.PORTER_COLLECT_BAG);
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
		if (pkt.getValidInt1()) {
			return new Luggage(pkt.getInt1(), pkt.getBool1() ? "FDT" : "TRT");
		} else {
			return null;
		}
	}

	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public void endOfWork() {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);

		clientCom.writeObject(pkt);
		clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Logs passenger in the servers
	 * 
	 * @param startingBags
	 * @param situations
	 */
	public void updateStartingBags(int[] startingBags, String[] situations) {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
		clientCom.open();
		Passenger passenger = (Passenger) Thread.currentThread();
		int[] sits = Arrays.asList(situations).stream().mapToInt(s -> s.equals("FDT") ? 1 : 0).toArray();
		Message pkt = new Message();
		pkt.setType(MessageType.PASSENGER_IN);
		pkt.setId(passenger.getPassengerId());
		pkt.setIntArray1(startingBags);
		pkt.setIntArray2(sits);

		clientCom.writeObject(pkt);
		clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Signals simulation finish
	 */
	public void closeStub() {
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);
		clientCom.writeObject(pkt);
		clientCom.readObject();
		clientCom.close();
	}
}