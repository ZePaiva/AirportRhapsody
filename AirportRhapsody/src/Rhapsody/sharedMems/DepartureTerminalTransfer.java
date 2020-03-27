package Rhapsody.sharedMems;

import java.util.Queue;

import Rhapsody.entities.BusDriver;
import Rhapsody.entities.Passenger;
import Rhapsody.entities.states.BusDriverState;
import Rhapsody.entities.states.PassengerState;

/**
 * Departure Terminal Transfer Quay shared memory entity
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class DepartureTerminalTransfer {

	/**
	 * Logger for the shared memory entity
	 */
	private GeneralRepository generalRepository;

	private boolean busArrived;

	private Queue<Integer> busSeats;

	public DepartureTerminalTransfer(GeneralRepository generalRepository) {
		this.generalRepository=generalRepository;
		this.busArrived=false;
	}

	/**
	 * Method to update passenger as a pessenger ready to embark in other adventures.
	 */
	public synchronized void leaveTheBus() {
		Passenger passenger = (Passenger) Thread.currentThread();
		while(!this.busArrived) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		while (!(this.busSeats.peek()!=passenger.getPassengerId())) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		passenger.setCurrentState(PassengerState.DEPARTING_TRANSFER_TERMINAL);
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.generalRepository.removeFromBusSeat(this.busSeats.poll(), false);
		notifyAll();
	}

	/**
	 * Method to signl bus parking by the BusDriver entity
	 */
	public synchronized void parkTheBusAndLetPassOff(Queue<Integer> busSeats) {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		busDriver.setBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
		this.generalRepository.updateBusDriverState(busDriver.getBusDriverState(), false);
		this.busSeats=busSeats;
		this.busArrived=true;
		while(!busSeats.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * method that simulates go back voyage of the BusDriver
	 */
	public synchronized void goToArrivalTerminal() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		busDriver.setBusDriverState(BusDriverState.DRIVING_BACKWARD);
		this.generalRepository.updateBusDriverState(busDriver.getBusDriverState(), false);
		this.busArrived=false;
	}
}