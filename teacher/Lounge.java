package Rhapsody.sharedMems;

import java.util.List;
import java.util.Random;
import java.util.Stack;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Luggage;

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
	 * GeneralRepository for debugging and pontuation purposes
	 */
	private GeneralRepository generalRepository;

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
	 * has simulation ended
	 */
	private boolean simulationEnded;

	/**
	 * Luggage list for the simulation, first index is the flight
	 */
	private Stack<Luggage>[] planeHoldLuggage;

	/**
	 * Signlas the flight id
	 */
	private int currentFlight;

	private boolean airportOpen;

	public static final String ANSI_WHITE = "\u001B[37m";

	/**
	 * Contructor method for the Lounge class
	 * 
	 * @param generalRepository
	 * @param maxPassengerAmount
	 * @param planeHoldLuggage
	 */
	public Lounge(GeneralRepository generalRepository, int maxPassengerAmount, 
					Stack<Luggage>[] planeHoldLuggage) {
		this.generalRepository=generalRepository;
		this.passengersPerFlight=maxPassengerAmount;
		this.planeHoldLuggage=planeHoldLuggage;
		this.currentFlight=0;
		this.passengersDisembarked=0;
		this.simulationEnded=false;
		this.airportOpen=false;
	}

	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 */
	public synchronized boolean takeARest() {
		// get porter thread
		Porter porter = (Porter) Thread.currentThread();

		// reset passengerxs disembarked
		this.passengersDisembarked=0;
		this.airportOpen=true;
		notifyAll(); 
		// update porter
		porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
		this.generalRepository.updateBagsInPlane(planeHoldLuggage[currentFlight].size(), true);
		this.generalRepository.updatePorterState(porter.getPorterState(), false);
		
		System.out.printf(ANSI_WHITE+"[LOUNGE---] Porter waiting for passengers | CP: %d | Sim %s\n", this.passengersDisembarked, this.simulationEnded);
		// waits for all passengers to arrive
		while(this.passengersDisembarked < this.passengersPerFlight && !this.simulationEnded) {
			try {
				wait();
				System.out.printf(ANSI_WHITE+"[LOUNGE---] Passenger disembarked | PD: %d\n", this.passengersDisembarked);
			} catch (InterruptedException e) {
				System.err.print("[LOUNGE---] Porter interrupted, check log\n");
				System.exit(3);
			}
		}

		return !this.simulationEnded;
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state. <p/>
	 * Disembarks passenger and notifies all other passengers
	 */
	public synchronized void whatShouldIDo() {
		Passenger passenger = (Passenger) Thread.currentThread();

		// delay to allow porter and bus to setup
		while (!this.airportOpen){
			try {
				Passenger.sleep((long) (new Random().nextInt(100)));
			} catch (InterruptedException e) {}
		}
	
		
		// updates state
		passenger.setCurrentState(PassengerState.AT_DISEMBARKING_ZONE);
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.generalRepository.addPassengerToFlight(passenger.getPassengerId(), true); 
		this.generalRepository.updatePlaneHoldBags(passenger.getStartingBags()[this.currentFlight], true);
        this.generalRepository.updateSituation(passenger.getPassengerId(), passenger.getPassengerSituation()[this.currentFlight], true);
        this.generalRepository.updateStartingBags(passenger.getPassengerId(), passenger.getStartingBags()[this.currentFlight], true);
        this.generalRepository.updateCurrentBags(passenger.getPassengerId(), 0, false);
		
		// disembarks passenger
		this.passengersDisembarked++;
		notifyAll();
	}

	/**
	 * Porter method to try to collect a bag or fail and exit the bag collection loop <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM<b/>  
	 * @return planeHasBags of type boolean
	 */
	public synchronized boolean tryToCollectABag() {
		Porter porter = (Porter) Thread.currentThread();
		porter.setPorterState(PorterState.AT_THE_PLANES_HOLD);

		if (this.planeHoldLuggage[this.currentFlight].isEmpty()) {
			this.generalRepository.updatePorterState(porter.getPorterState(), false);
			return false;
		}

		porter.setCurrentLuggage(this.planeHoldLuggage[this.currentFlight].pop());
		this.generalRepository.updatePorterState(porter.getPorterState(), true);
		this.generalRepository.updateBagsInPlane(this.planeHoldLuggage[this.currentFlight].size(), false);
		return true;

	}

	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public synchronized void endOfWork() {
		this.simulationEnded=true;
		notifyAll();
	}
}