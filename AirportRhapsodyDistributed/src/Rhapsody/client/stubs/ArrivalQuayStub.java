package Rhapsody.client.stubs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.BusDriver;
import Rhapsody.client.entities.Passenger;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Arrival Terminal Transfer Quay Stub for the clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalQuayStub {
    
    /**
	 * Client communication channelt
	 */
	private final ClientCom clientCom;

    /**
     * Prettify
     */
    public static final String ANSI_BLUE = "\u001B[0m\u001B[34m";

    /**
     * Arrival quay stub constructor
     */
    public ArrivalQuayStub(){
        this.clientCom = new ClientCom(RunParameters.ArrivalQuayHostName, RunParameters.ArrivalQuayPort);
		this.clientCom.open();
    }

	/**
	 * Mehtod to put passenger in the waiting line for the bus and signal the
	 * busdriver that
	 * <p/>
	 * he can start announcing the bus boarding if necessary
	 */
	public void takeABus() {
		Passenger passenger = (Passenger) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.PASSENGERS_WAITING);
		pkt.setId(passenger.getPassengerId());

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		passenger.setCurrentState(pkt.getState());
	}

	/**
	 * Method that inserts passenger in the bus seats and makes him wait for the end
	 * of the bus ride.
	 */
	public boolean enterTheBus() {
		Passenger passenger = (Passenger) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.PASSENGER_INTO_BUS);
		pkt.setId(passenger.getPassengerId());

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		passenger.setCurrentState(pkt.getState());
        return pkt.getBool1();
    }

	/**
	 * Method used to signal BusDriver that day of work has ended
	 */
	public void endOfWork() {
		Passenger passenger = (Passenger) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);
		pkt.setId(passenger.getPassengerId());

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		passenger.setCurrentState(pkt.getState());
	}

	/**
	 * Method used by the bus driver if it's day of work has ended or any passenger
	 * arrived to arrival termina ltransfer quay
	 * 
	 * @return daysWorkEnded
	 */
	public boolean hasDaysWorkEnded() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.BD_HAS_ENDED);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		busDriver.setBusDriverState(pkt.getState());

        return pkt.getBool1();
	}

	/**
	 * Method used by the BusDriver that is waiting for a full bus or starting time
	 */
	public void announcingBusBoarding() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.BD_ANNOUNCING_BOARDING);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
	}

	/**
	 * Method used by the BusDriver to signal he has arrived to the arrival terminal
	 * bus stop
	 */
	public void parkTheBus() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.BD_ARRIVING);
		pkt.setState(busDriver.getBusDriverState());

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		busDriver.setBusDriverState(pkt.getState());
	}

	/**
	 * Method to simulate the bus voyage
	 */
	public Queue<Integer> goToDepartureTerminal() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.BD_DRIVING);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();

		int[] seats = pkt.getIntArray1();
		busDriver.setBusDriverState(pkt.getState());

		Queue<Integer> q = new LinkedList<>();
		for (int i : seats) { q.add(i); }

        return q;
	}

	/**
	 * Close stub
	 */
	public void closeStub() {
		clientCom.close();
	}

}