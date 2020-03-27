package Rhapsody.sharedMems;

import java.util.List;
import java.util.Queue;
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

	private BaggageCollectionPoint baggageCollectionPoint;


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
	private Queue<Luggage>[] planeHoldLuggage;

	/**
	 * Signals the flight id
	 */
	private int currentFlight;

	private boolean airportOpen;
	private boolean init;
	private boolean limit;

	public static final String ANSI_WHITE = "\u001B[37m";

	/**
	 * Contructor method for the Lounge class
	 * 
	 * @param generalRepository
	 * @param maxPassengerAmount
	 * @param planeHoldLuggage
	 */
	public Lounge(GeneralRepository generalRepository, BaggageCollectionPoint baggageCollectionPoint,
					 int maxPassengerAmount, Queue<Luggage>[] planeHoldLuggage) {
		this.generalRepository=generalRepository;
		this.passengersPerFlight=maxPassengerAmount;
		this.planeHoldLuggage=planeHoldLuggage;
		this.baggageCollectionPoint=baggageCollectionPoint;
		this.currentFlight=-1;
		this.passengersDisembarked=0;
		this.simulationEnded=false;
		this.airportOpen=false;
		this.init=true;
		this.limit=false;
	}
	
	/**
	 * Puts porter in {@link Rhapsody.entities.states.PorterState#WAITING_FOR_PLANE_TO_LAND} state
	 * @return simulationContinue
	 */
	public synchronized boolean takeARest() {
		// get porter thread
		Porter porter = (Porter) Thread.currentThread();

		//System.out.println("Barks+"+(this.passengersDisembarked>0 && !this.init));
		//System.out.println("Sim+"+this.simulationEnded);
		
		while (this.passengersDisembarked>0 && !this.init || this.simulationEnded) {
			try {
				if (this.simulationEnded) {
					return !this.simulationEnded;
				}
				wait();
				//System.out.println("Porter+"+this.passengersDisembarked);
			} catch (InterruptedException e) {}
		}
		this.currentFlight++;
		System.out.println(ANSI_WHITE+"[LOUNGE---] Flight cleared");
		this.generalRepository.clearFlight(true);
		this.generalRepository.updateFlight(this.currentFlight, true);
		this.baggageCollectionPoint.newFlight();

		// reset passengerxs disembarked
		this.init=false;
		this.airportOpen=true;	
		this.limit=false;	
		System.out.printf(ANSI_WHITE+"[LOUNGE---] Flight updated | NF %d\n", this.currentFlight);
		this.generalRepository.updateFlight(this.currentFlight, true);
		notifyAll(); 

		// update porter
		porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
		this.generalRepository.updateBagsInPlane(planeHoldLuggage[this.currentFlight].size(), true);
		this.generalRepository.updatePorterState(porter.getPorterState(), true);
		
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
		this.airportOpen=false;
		this.limit=true;

		return !this.simulationEnded;
	}

	/**
	 * Puts passenger in {@link Rhapsody.entities.states.PassengerState#AT_DISEMBARKING_ZONE} state. <p/>
	 * Disembarks passenger and notifies all other passengers
	 * @param flightId
	 */
	public synchronized void whatShouldIDo(int flightId) {
		Passenger passenger = (Passenger) Thread.currentThread();

		// delay to allow porter and bus to setup
		if (this.passengersDisembarked>0 && this.limit) {
			this.passengersDisembarked--;
			System.out.printf(ANSI_WHITE+"[LOUNGE---] P%d terminated | PD %d\n", passenger.getPassengerId(), this.passengersDisembarked);
			notifyAll();
		}
		while (!this.airportOpen || (this.passengersDisembarked>0 && this.limit)) {
			try {
				System.out.printf(ANSI_WHITE+"[LOUNGE---] P%d | Porter not ready yet\n", passenger.getPassengerId());
				wait();
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
		return;
	}

	/**
	 * Porter method to try to collect a bag or fail and exit the bag collection loop <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM<b/>  
	 * @return planeHasBags of type boolean
	 */
	public synchronized Luggage tryToCollectABag() {
		Porter porter = (Porter) Thread.currentThread();
		porter.setPorterState(PorterState.AT_THE_PLANES_HOLD);

		try {
			if (this.planeHoldLuggage[this.currentFlight].isEmpty()) {
				System.out.printf(ANSI_WHITE+"[LOUNGE---] Porter got 0 bags\n");
				this.generalRepository.updatePorterState(porter.getPorterState(), false);
				return null;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			this.currentFlight=this.planeHoldLuggage.length-1;
			if (this.planeHoldLuggage[this.currentFlight].isEmpty()) {
				System.out.printf(ANSI_WHITE+"[LOUNGE---] Porter got 0 bags\n");
				this.generalRepository.updatePorterState(porter.getPorterState(), false);
				return null;
			}
		}

		System.out.printf(ANSI_WHITE+"[LOUNGE---] Porter got bags\n");
		Luggage bagToReturn = this.planeHoldLuggage[this.currentFlight].poll();
		this.generalRepository.updatePorterState(porter.getPorterState(), true);
		this.generalRepository.updateBagsInPlane(this.planeHoldLuggage[this.currentFlight].size(), false);
		return bagToReturn;

	}

	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public synchronized void endOfWork() {
		this.simulationEnded=true;
		notifyAll();
	}
}