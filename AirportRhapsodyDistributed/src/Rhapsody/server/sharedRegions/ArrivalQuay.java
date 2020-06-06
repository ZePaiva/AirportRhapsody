package Rhapsody.server.sharedRegions;

import java.util.LinkedList;
import java.util.Queue;

import Rhapsody.common.RunParameters;
import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.BusDriverInterface;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Arrival terminal transfer quay entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalQuay {
    
    /**
     * Logger
     */
    private GeneralRepositoryStub generalRepository;
    
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
	 * Bus max waiting time
	 */
	private final long busSchedule;

	/**
	 * Has anyone arrived to the bus station?
	 */
	private boolean announcingBoarding;

	public static final String ANSI_BLUE = "\u001B[0m\u001B[34m";

	/**
	 * ArrivalTerminalTransfer constructor method
	 * 
	 * @param maxQueueSize
	 * @param busSeats
	 * @param busSchedule
	 * @param generalRepository
	 */
	public ArrivalQuay(long busSchedule,
			GeneralRepositoryStub generalRepository) {
		this.generalRepository = generalRepository;
		this.dayFinished = false;
		this.availableBus = false;
		this.announcingBoarding = false;
		this.transferQuay = new LinkedList<>();
		this.busSeats = new LinkedList<>();
		this.busSchedule = busSchedule;
	}

	/**
	 * Mehtod to put passenger in the waiting line for the bus and signal the
	 * busdriver that
	 * <p/>
	 * he can start announcing the bus boarding if necessary
	 */
	public synchronized void takeABus() {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] P%d wants to take a bus\n", passenger.getEntityID());
		passenger.setEntityState(States.ARRIVING_TRANSFER_TERMINAL);
		while (!this.availableBus) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		this.transferQuay.add(passenger.getEntityID());
		this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), true);
		this.generalRepository.addToWaitingQueue(passenger.getEntityID(), false);
		if (this.transferQuay.size()==RunParameters.T) {
			notifyAll();
		} else if (this.transferQuay.size()==1) {
			notifyAll();
		}
	}

	/**
	 * Method that inserts passenger in the bus seats and makes him wait for the end
	 * of the bus ride.
	 */
	public synchronized boolean enterTheBus() {
		PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] P%d entering the bus\n", passenger.getEntityID());
		// if this passenger is not the head of waiting line queue it must wait until it
		//System.out.println(this.transferQuay.toString());
		//System.out.println(this.transferQuay.peek());
		while (!this.announcingBoarding || this.transferQuay.peek()!=passenger.getEntityID() ||
				this.busSeats.size() == RunParameters.T) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		

		System.out.printf(ANSI_BLUE+"[ARRTERTRA] P%d entered the bus\n", passenger.getEntityID());
		passenger.setEntityState(States.TERMINAL_TRANSFER);
		this.busSeats.add(this.transferQuay.poll());
		this.generalRepository.removeFromWaitingQueue(true);
		this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), true);
		this.generalRepository.addToBusSeat(passenger.getEntityID(), false);
		notifyAll();
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
		BusDriverInterface busDriver = (TunnelProvider) Thread.currentThread();
		busDriver.setEntityState(States.PARKING_AT_THE_ARRIVAL_LOUNGE);
		this.generalRepository.updateBusDriverState(busDriver.getEntityState(), false);
		this.availableBus = true;
		notifyAll();
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver is waiting passengers or sim to end\n");
		while (!this.dayFinished && this.transferQuay.size()==0) {
			try {
				wait();
				System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver woke | Sim %s | PA %s\n", this.dayFinished, this.transferQuay.size()!=0);
				System.out.println(!this.dayFinished && this.transferQuay.size()==0);
				System.out.println(this.transferQuay.toString());
			} catch (InterruptedException e) {
			}
		}
		if (!this.dayFinished) {
			System.out.printf(ANSI_BLUE+"[ARRTERTRA] First passenger arrived to ATT\n");
			try {
				wait(this.busSchedule);
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
		notifyAll();
		while (!this.transferQuay.isEmpty() && this.busSeats.size()!=RunParameters.T) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver boarded all passengers\n");
	}

	/**
	 * Method used by the BusDriver to signal he has arrived to the arrival terminal
	 * bus stop
	 */
	public synchronized void parkTheBus() {
		BusDriverInterface busDriver = (TunnelProvider) Thread.currentThread();
		busDriver.setEntityState(States.PARKING_AT_THE_ARRIVAL_LOUNGE);
		this.generalRepository.updateBusDriverState(busDriver.getEntityState(), false);
		this.availableBus = true;
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver arrived to AT\n");
	}

	/**
	 * Method to simulate the bus voyage
	 */
	public synchronized int[] goToDepartureTerminal() {
		BusDriverInterface busDriver = (TunnelProvider) Thread.currentThread();
		busDriver.setEntityState(States.DRIVING_FORWARD);
		this.generalRepository.updateBusDriverState(busDriver.getEntityState(), false);
		this.availableBus = false;
		this.announcingBoarding=false;
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver is starting FORWARD voyage\n");
		try {
			Thread.sleep(this.busSchedule);
		} catch (InterruptedException e) {}
		System.out.printf(ANSI_BLUE+"[ARRTERTRA] BusDriver ended FORWARD voyage\n");
		int[] seats = this.busSeats.stream().mapToInt(i -> i).toArray();
		return seats;
	}
}