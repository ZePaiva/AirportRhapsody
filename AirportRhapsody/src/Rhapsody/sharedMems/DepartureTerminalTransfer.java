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

	public static final String ANSI_BLACK = "\033[1;37m";

	public DepartureTerminalTransfer(GeneralRepository generalRepository) {
		this.generalRepository=generalRepository;
		this.busArrived=false;
	}

	/**
	 * Method to update passenger as a pessenger ready to embark in other adventures.
	 */
	public synchronized void leaveTheBus() {
		Passenger passenger = (Passenger) Thread.currentThread();
		System.out.printf(ANSI_BLACK+"[DEPTERTRA] P%d is on the bus traveling\n", passenger.getPassengerId());
		while(!this.busArrived) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		System.out.printf(ANSI_BLACK+"[DEPTERTRA] P%d travel has ended\n", passenger.getPassengerId());
		//System.out.println(this.busSeats.toString());
		while (this.busSeats.peek()!=passenger.getPassengerId()) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		System.out.printf(ANSI_BLACK+"[DEPTERTRA] P%d exited the bus\n", passenger.getPassengerId());
		passenger.setCurrentState(PassengerState.DEPARTING_TRANSFER_TERMINAL);
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.generalRepository.removeFromBusSeat(false);
		this.busSeats.poll();
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
		System.out.printf(ANSI_BLACK+"[DEPTERTRA] Bus has parked, waiting for all to leave\n");
		notifyAll();
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
		System.out.printf(ANSI_BLACK+"[DEPTERTRA] Bus leaving to ARRIVAL TERMINAL TRANSFER\n");
	}
}