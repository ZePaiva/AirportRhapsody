package Rhapsody.server.sharedRegions;

import java.util.Queue;

import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.BusDriverInterface;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Departure terminal transfer quay entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureQuay {
    
	/**
	 * Logger for the shared memory entity
	 */
	private GeneralRepositoryStub generalRepository;

	private boolean busArrived;

	private Queue<Integer> busSeats;

	public static final String ANSI_BLACK = "\033[1;37m";
	public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PASSENGER = "\u001B[0m\u001B[32m";

	public DepartureQuay(GeneralRepositoryStub generalRepository) {
		this.generalRepository=generalRepository;
		this.busArrived=false;
		this.generalRepository.registerMem(6);
	}

	/**
	 * Method to update passenger as a pessenger ready to embark in other adventures.
	 */
	public synchronized void leaveTheBus() {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		System.out.printf(ANSI_PASSENGER+"[PASSENGER] P%d is on the bus traveling"+ANSI_RESET+"\n", passenger.getEntityID());
		while(!this.busArrived) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		System.out.printf(ANSI_PASSENGER+"[PASSENGER] P%d travel has ended"+ANSI_RESET+"\n", passenger.getEntityID());
		//System.out.println(this.busSeats.toString());
		while (this.busSeats.peek()!=passenger.getEntityID()) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		System.out.printf(ANSI_PASSENGER+"[PASSENGER] P%d exited the bus"+ANSI_RESET+"\n", passenger.getEntityID());
		passenger.setEntityState(States.DEPARTING_TRANSFER_TERMINAL);
		this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), true);
		this.generalRepository.removeFromBusSeat(false);
		this.busSeats.poll();
		notifyAll();
	}

	/**
	 * Method to signl bus parking by the BusDriver entity
	 */
	public synchronized void parkTheBusAndLetPassOff(Queue<Integer> busSeats) {
		BusDriverInterface busDriver = (TunnelProvider) Thread.currentThread();
		busDriver.setEntityState(States.PARKING_AT_THE_DEPARTURE_TERMINAL);
		System.out.println(busSeats==null);
		this.generalRepository.updateBusDriverState(busDriver.getEntityState(), false);
		this.busSeats=busSeats;
		this.busArrived=true;
		System.out.printf(ANSI_BLACK+"[BUSDRIVER] Bus has parked, waiting for all to leave"+ANSI_RESET+"\n");
		System.out.println(this.busSeats.isEmpty());
		this.busSeats.stream().forEach(System.out::println);
		notifyAll();
		while(!this.busSeats.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		System.out.printf(ANSI_BLACK+"[BUSDRIVER] Bus has parked, all left"+ANSI_RESET+"\n");
	}

	/**
	 * method that simulates go back voyage of the BusDriver
	 */
	public synchronized void goToArrivalTerminal() {
		BusDriverInterface busDriver = (TunnelProvider) Thread.currentThread();
		busDriver.setEntityState(States.DRIVING_BACKWARD);
		this.generalRepository.updateBusDriverState(busDriver.getEntityState(), false);
		this.busArrived=false;
		System.out.printf(ANSI_BLACK+"[BUSDRIVER] Bus leaving to ARRIVAL TERMINAL TRANSFER"+ANSI_RESET+"\n");
	}

}