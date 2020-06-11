package Rhapsody.client.stubs;

import java.util.Queue;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.BusDriver;
import Rhapsody.client.entities.Passenger;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Departure terminal transfer entity for clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureQuayStub {
    
    /**
     * Prettify
     */
    public static final String ANSI_BLACK = "\033[1;37m";

    /**
     * Stub constructor
     */
    public DepartureQuayStub() {
    }
    
	/**
	 * Method to update passenger as a pessenger ready to embark in other adventures.
	 */
	public void leaveTheBus() {
        ClientCom clientCom = new ClientCom(RunParameters.DepartureQuayHostName, RunParameters.DepartureQuayPort);
		clientCom.open();
        Passenger passenger = (Passenger) Thread.currentThread();
        Message pkt = new Message();
        pkt.setType(MessageType.PASSENGER_EXITING_BUS);
        pkt.setId(passenger.getPassengerId());

        clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
		passenger.setCurrentState(pkt.getState());
	}

	/**
	 * Method to signl bus parking by the BusDriver entity
	 */
	public void parkTheBusAndLetPassOff(Queue<Integer> busSeats) {
        ClientCom clientCom = new ClientCom(RunParameters.DepartureQuayHostName, RunParameters.DepartureQuayPort);
        clientCom.open();
        BusDriver busDriver = (BusDriver) Thread.currentThread();
        Message pkt = new Message();
        
        int[] seats = busSeats.stream().mapToInt(i -> i).toArray();
        
        pkt.setType(MessageType.BD_PARKING);
        pkt.setIntArray1(seats);
        
        clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
		busDriver.setBusDriverState(pkt.getState());
	}

	/**
	 * method that simulates go back voyage of the BusDriver
	 */
	public void goToArrivalTerminal() {
        ClientCom clientCom = new ClientCom(RunParameters.DepartureQuayHostName, RunParameters.DepartureQuayPort);
        clientCom.open();
        BusDriver busDriver = (BusDriver) Thread.currentThread();
        Message pkt = new Message();
        
        pkt.setType(MessageType.BD_DRIVING);
        
        clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
		busDriver.setBusDriverState(pkt.getState());
	}

    /**
     * Close the stub
     */
	public void closeStub() {
		ClientCom clientCom = new ClientCom(RunParameters.DepartureQuayHostName, RunParameters.DepartureQuayPort);
        clientCom.open();
        Message pkt = new Message();
        pkt.setType(MessageType.SIM_ENDED);
        clientCom.writeObject(pkt);
        clientCom.readObject();
        clientCom.close();
	}
}