package Rhapsody.sharedMems;

import java.util.LinkedList;
import java.util.Queue;

import Rhapsody.entities.BusDriver;
import Rhapsody.entities.Passenger;
import Rhapsody.entities.states.BusDriverState;
import Rhapsody.entities.states.PassengerState;

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
	 * Queue of passengers inside the bus
	 */
	private Queue<Integer> busSeats;

	/**
	 * Variable to check if any passenger has arrived or not.
	 * <p/>
	 * Used to wake up the BusDriver when a passenger arrives
	 */
	private boolean passengerArrived;

	/**
	 * maximum size of the bus
	 */
	private final int maxBusSeats;

	/**
	 * maximum size of passenger waiting in line
	 */
	private final int maxWaitingPass;

	/**
	 * Bus max waiting time
	 */
	private final long busSchedule;

	/**
	 * Has anyone arrived to the bus station?
	 */
	private boolean announcingBoarding;

	public static final String ANSI_BLUE = "\u001B[34m";

	/**
	 * ArrivalTerminalTransfer constructor method
	 * 
	 * @param maxQueueSize
	 * @param busSeats
	 * @param busSchedule
	 * @param generalRepository
	 */
	public ArrivalTerminalTransfer(int maxQueueSize, int busSeats, long busSchedule,
			GeneralRepository generalRepository) {
		this.generalRepository = generalRepository;
		this.dayFinished = false;
		this.availableBus = false;
		this.passengerArrived = false;
		this.announcingBoarding = false;
		this.maxWaitingPass = maxQueueSize;
		this.transferQuay = new LinkedList<>();
		this.busSeats = new LinkedList<>();
		this.busSchedule = busSchedule;
		this.maxBusSeats = busSeats;
	}

	/**
	 * Mehtod to put passenger in the waiting line for the bus and signal the
	 * busdriver that
	 * <p/>
	 * he can start announcing the bus boarding if necessary
	 */
	public synchronized void takeABus() {
		Passenger passenger = (Passenger) Thread.currentThread();
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] P%d wants to take a bus\n", passenger.getPassengerId());
		passenger.setCurrentState(PassengerState.ARRIVING_TRANSFER_TERMINAL);
		while (!this.availableBus) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		this.transferQuay.add(passenger.getPassengerId());
		this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), true);
		this.generalRepository.addToWaitingQueue(passenger.getPassengerId(), false);
		if (!this.passengerArrived) {
			this.passengerArrived=true;
			notifyAll();
		}
	}

	/**
	 * Method that inserts passenger in the bus seats and makes him wait for the end
	 * of the bus ride.
	 */
	public synchronized boolean enterTheBus() {
		Passenger passenger = (Passenger) Thread.currentThread();
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] P%d entering the bus\n", passenger.getPassengerId());
		// if this passenger is not the head of waiting line queue it must wait until it
		System.out.println(this.transferQuay.toString());
		System.out.println(this.transferQuay.peek());
		if (this.transferQuay.peek() != passenger.getPassengerId() || !this.announcingBoarding
				|| this.busSeats.size() == this.maxBusSeats) {
			return false;
		}
		

		System.out.printf(ANSI_BLUE+"[ARRTERTRA] P%d entered the bus\n", passenger.getPassengerId());
		passenger.setCurrentState(PassengerState.TERMINAL_TRANSFER);
		this.busSeats.add(this.transferQuay.poll());
		this.generalRepository.removeFromWaitingQueue(passenger.getPassengerId(), true);
		this.generalRepository.addToBusSeat(passenger.getPassengerId(), false);
		if (this.busSeats.size() == this.maxBusSeats) {
			notifyAll();
		}
		return true;
	}

	/**
	 * Method used to signal BusDriver that day of work has ended
	 */
	public synchronized void endOfWork() {
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] Simulation ended\n");
		this.dayFinished = true;
		notifyAll();
	}

	/**
	 * Method used by the bus driver if it's day of work has ended or any passenger
	 * arrived to arrival termina ltransfer quay
	 * 
	 * @return daysWorkEnded
	 */
	public synchronized boolean hasDaysWorkEnded() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		busDriver.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_LOUNGE);
		this.generalRepository.updateBusDriverState(busDriver.getBusDriverState(), false);
		this.availableBus = true;
		notifyAll();
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver is starting to carrry passengers\n");
		while (!this.dayFinished && !this.passengerArrived) {
			try {
				wait();
				System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver woke | Sim %s | PA %s\n", this.dayFinished, this.passengerArrived);
			} catch (InterruptedException e) {
			}
		}
		return !this.dayFinished;
	}

	/**
	 * Method used by the BusDriver that is waiting for a full bus or starting time
	 */
	public synchronized void announcingBusBoarding() {
		this.announcingBoarding = true;
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver announcing boarding\n");
		try {
			wait(this.busSchedule);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Method used by the BusDriver to signal he has arrived to the arrival terminal
	 * bus stop
	 */
	public synchronized void parkTheBus() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		busDriver.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_LOUNGE);
		this.generalRepository.updateBusDriverState(busDriver.getBusDriverState(), false);
		this.availableBus = true;
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver arrived to AT\n");
	}

	/**
	 * Method to simulate the bus voyage
	 */
	public synchronized Queue<Integer> goToDepartureTerminal() {
		BusDriver busDriver = (BusDriver) Thread.currentThread();
		busDriver.setBusDriverState(BusDriverState.DRIVING_FORWARD);
		this.generalRepository.updateBusDriverState(busDriver.getBusDriverState(), false);
		this.availableBus = false;
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver is starting FORWARD voyage\n");
		try {
			Thread.sleep(this.busSchedule);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver ended FORWARD voyage\n");
		return this.busSeats;
	}
}