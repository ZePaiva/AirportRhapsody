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
	private final int passengersPerFlight;

	/**
	 * amount of passengers ready to disembark
	 */
	private int passengersDisembarked;

	/**
	 * Contructor method for the Lounge class
	 * 
	 * @param logger
	 * @param maxPassengerAmount
	 */
	public Lounge(Logger logger, int maxPassengerAmount) {
		this.logger=logger;
		this.passengersPerFlight=maxPassengerAmount;
		this.passengersDisembarked=0;
	}

	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 * @param flightId
	 */
	public synchronized void takeARest() {
		// get porter thread
		Porter porter = (Porter) Thread.currentThread();

		// update porter
		porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
		this.logger.updatePorterState(porter.getPorterState(), true);
		
		// waits for all passengers to arrive
		while(this.passengersDisembarked < this.passengersPerFlight) {
			try {
				System.out.printf("PORTER %d | PD: %d | PF: %d | F\n", porter.getId(), this.passengersDisembarked, this.passengersPerFlight);
				wait();
			} catch (InterruptedException e) {
				System.err.print("[LOUNGE] Porter interrupted, check log\n");
				System.exit(3);
			}
		}
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state. <p/>
	 * Disembarks passenger and notifies all other passengers
	 */
	public synchronized void whatShouldIDo() {
		Passenger passenger = (Passenger) Thread.currentThread();

		// updates state
		passenger.setCurrentState(PassengerState.AT_DISEMBARKING_ZONE);
		this.logger.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.logger.addPassengerToFlight(passenger.getPassengerId(), false);
		System.out.printf("P%d disembarked\n", passenger.getPassengerId());

		// disembarks passenger
		this.passengersDisembarked++;

		// waits until all passengers disembarked
		while (this.passengersDisembarked<this.passengersPerFlight) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("[LOUNGE] Passenger interrupted for some reason");
				System.exit(3);
			}
		}
		notifyAll();
	}

	/**
	 * Signals passenger life-cylce has terminated
	 */
	public synchronized void resetFlight() {
		this.passengersDisembarked--;
		notifyAll();
	}
}