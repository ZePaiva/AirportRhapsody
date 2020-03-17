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
	 * Maximum amount of flights 
	 */
	private final int flights;

	/**
	 * amount of passengers ready to disembark
	 */
	private int passengersDisembarked;

	/**
	 * Informs when simulation is finished
	 */
	private boolean airportOpen;

	/**
	 * Porter variable to check if all passenger have disembarked or not
	 */
	private boolean allDisembarked;


	/**
	 * Contructor method for the Lounge class
	 * 
	 * @param logger
	 * @param maxPassengerAmount
	 * @param flights
	 */
	public Lounge(Logger logger, int maxPassengerAmount, int flights) {
		this.logger=logger;
		this.passengersPerFlight=maxPassengerAmount;
		this.passengersDisembarked=0;
		this.flights=1;
		this.airportOpen=true;
		this.allDisembarked=false;
	}

	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 * @param flightId
	 */
	public synchronized void takeARest(int flightId) {
		// get porter thread
		Porter porter = (Porter) Thread.currentThread();

		// update porter
		porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
		this.logger.updatePorterState(porter.getPorterState(), true);
		
		// waits for all passengers to arrive
		this.passengersDisembarked=0;
		this.logger.updateFlight(flightId, false);
		while(!this.allDisembarked) {
			try {
				//System.out.printf("PORTER PD: %d | PF: %d\n", this.passengersDisembarked, this.passengersPerFlight);
				wait();
			} catch (InterruptedException e) {
				System.err.print("[LOUNGE] Porter interrupted, check log\n");
				System.exit(3);
			}
		}

		// checks if it is time to end simulation
		if (flightId==this.flights) {
			this.airportOpen=false;
			notifyAll();
		}
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state. <p/>
	 * Disembarks passenger and notifies all other passengers
	 */
	public synchronized void whatShouldIDo() {
		Passenger passenger = (Passenger) Thread.currentThread();
		int pId=passenger.getPassengerId();
		passenger.setCurrentState(PassengerState.AT_DISEMBARKING_ZONE);
		this.logger.updatePassengerState(passenger.getCurrentState(), pId, false);
		// disembartks and notifies all so that passengers can start moving
		this.passengersDisembarked+=1;
		notifyAll();
		
		// waits until airport is closed or all passengers disembark
		while(this.airportOpen && this.passengersDisembarked!=this.passengersPerFlight){
			//System.out.printf("P%d WSID | PD: %d | PF: %d | AO: %s\n", passenger.getPassengerId(), this.passengersDisembarked, this.passengersPerFlight, Boolean.toString(this.airportOpen));
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.print("[LOUNGE] Passenger interrupted, check log\n");
				System.exit(3);
			}
		}
		this.allDisembarked=true;
		passenger.canFly(!this.airportOpen);
	}
}