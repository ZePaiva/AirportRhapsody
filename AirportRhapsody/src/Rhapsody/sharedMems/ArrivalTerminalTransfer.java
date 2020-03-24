package Rhapsody.sharedMems;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Arrival Terminal Transfer Quay shared memory
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class ArrivalTerminalTransfer {

	/**
	 * Logger repository
	 */
	private GeneralRepository generalRepository;

	/**
	 * Variable to Signal BusDriver if day of work has finished
	 */
	private boolean dayFinished;

	/**
	 * Variable to check if the bus is at the stop
	 */
	private boolean availableBus;

	/**
	 * Queue of passengers at the bus stop
	 */
	private Queue<Integer> transferQuay;

	/**
	 * Variable to check if any passenger has arrived or not. <p/>
	 * Used to wake up the BusDriver when a passenger arrives
	 */
	private boolean passengerArrived;

	/**
	 * ArrivalTerminalTransfer constructor method
	 * @param generalRepository
	 */
	public ArrivalTerminalTransfer(GeneralRepository generalRepository) {
		this.generalRepository=generalRepository;
		this.dayFinished=false;
		this.availableBus=true;
		this.transferQuay=new ConcurrentLinkedQueue<>();
	}

	/**
	 * Mehtod to put passenger in the waiting line for the bus and signal the busdriver that <p/> 
	 * he can start announcing the bus boarding if necessary 
	 */
	public synchronized void takeABus() {
	}

	/**
	 * Method that inserts passenger in the bus seats and makes him wait for the end of the bus ride.
	 */
	public synchronized void enterTheBus() {
	}

	/**
	 * Method used to signal BusDriver that day of work has ended
	 */
	public synchronized void endOfWork() {
		this.dayFinished=true;
		notifyAll();
	}
	
	/**
	 * Method used by the bus driver if it's day of work has ended or any passenger arrived to arrival
	 * termina ltransfer quay
	 */
	public synchronized void hasDaysWorkEnded() {
	}

	/**
	 * Method used by the BusDriver that is waiting for a full bus or starting time
	 */
	public synchronized void announcingBusBoarding() {
	}

	/**
	 * Method used by the BusDriver to signal he has arrived to the arrival terminal bus stop
	 */
	public synchronized void parkTheBus() {
	}
}