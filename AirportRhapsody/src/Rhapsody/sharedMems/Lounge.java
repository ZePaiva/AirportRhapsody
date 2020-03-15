package Rhapsody.sharedMems;

import java.util.Arrays;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Logger;

/**
 * Lounge datatype implements the Arrival Lounge shared memory region.
 * <p/>
 * This is the starting region of all passengers.
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class Lounge {

	/**
	 * Logger for debugging and pontuation purposes
	 */
	private Logger logger;

	/**
	 * Information about amount of passengers of each flight. <p/>
	 * Starts with -1 in all, 0 means all passengers disembarked.
	 */
	private int passengersPerFlight;

	/**
	 * ID's of the passengers in the current arriving flight
	 */
	private int[] passengersInFlight;
	
	/**
	 * Current arriving flight Id
	 */
	private int flight;

	/**
	 * Contructor method for the Lounge class
	 * 
	 * @param logger
	 * @param maxPassengerAmount
	 */
	public Lounge(Logger logger, int maxPassengerAmount) {
		this.logger=logger;
		this.passengersPerFlight=-1;
		this.flight=-1;
		this.passengersInFlight=new int[maxPassengerAmount];
		Arrays.fill(this.passengersInFlight, -1);
	}

	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 */
	public synchronized void takeARest() {
		Porter porter = (Porter) Thread.currentThread();
		porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
		logger.updatePorterState(porter.getPorterState());
	}

	/**
	 * Porter methdo to wait for all passengers of a new flight
	 * @param newFlight
	 */
	public synchronized void waitForAllPassengers(int newFlight) {
		this.passengersPerFlight=this.passengersInFlight.length;
		this.flight=newFlight;
		this.logger.updateFlight(this.flight);
		while(this.passengersPerFlight>0) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.print("[PORTER] Interruption @ Lounge#waitForAllPassengers");
				System.exit(3);
			}
		}
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state
	 */
	public synchronized void whatShouldIDo() {
		Passenger passenger = (Passenger) Thread.currentThread();
		int pId=passenger.getPassengerId();
		passenger.setCurrentState(PassengerState.AT_DISEMBARKING_ZONE);
		this.logger.updatePassengerState(passenger.getCurrentState(), pId);
	}
}