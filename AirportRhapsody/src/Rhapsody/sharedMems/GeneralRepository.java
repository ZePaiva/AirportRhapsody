package Rhapsody.sharedMems;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import Rhapsody.entities.states.BusDriverState;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Luggage;

/**
 * General repository of information for the rhapsody
 * <p/>
 * Here lies the data that is general to all and will be logged, like flightId, luggage on plane, 
 * passenger type and the real age fo the earth according to the holy bible.
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class GeneralRepository {
    
    // Logging data
	/**
	 * Path to log file
	 */
	private String logFilePath;
	/**
	 * Number of flights in the airport
	 */
	private int flight;
	/**
	 * Passengers id in the flight
	 */
	private int[] flightPassengers;
	/**
	 * State of the porter
	 */
	private PorterState porterState;
	/**
	 * Number of bags in the conveyor's belt
	 */
	private int bagsOnConveyor;
	/**
	 * Number of bags in the storeroom
	 */
	private int bagsOnStoreroom;
	/**
	 * State of the bus driver
	 */
	private BusDriverState busDriverState;
	/**
	 * Occupation state of the waiting for bus queue (passengerId or empy)
	 */
	private int[] waitingQueue;
	/**
	 * Occupation state of the bus seats (passengerId or empty)
	 */
	private int[] busSeats;
	/**
	 * State of each passenger
	 * <p/>
	 * Index is passengerId
	 */
	private PassengerState[] passengersState;
	/**
	 * Situation of each passenger, can be TRT (in transit) or FDT (final destination)
	 * <p/>
	 * Index is passengerId
	 */
	private String[] passengersSituation;
	
	/**
	 * Number of bags each passenger has started it's journey
	 * <p/>
	 * Index is passengerId
	 */
	private int[] passengersStartingBags;
	
	/**
	 * Number of bags each passenger currently has
	 * <p/>
	 * Index is passengerId
	 */
	private int[] passengersCurrentBags;

	//Statistics data
	/**
	 * Passengers using this airport as their final destination
	 */
	private int passengersFDT;
	/**
	 * Passengers using this airport as their starting destination
	 */
	private int passengersTRT;
	/**
	 * Number of bags that should have been transported in the planes hold
	 */
	private int transportedBags;
	/**
	 * Number of lost bags
	 */
	private int lostBags;

	/**
	 * Number of bags in plane's hold
	 */
	private int bagsOnPlane;
	

	/**
	 * General Repository constructor
	 * 
	 * @param logFilePath
	 * @param flight
	 * @param flightPassengers
	 * @param bagsOnPlane
	 * @param porterState
	 * @param bagsOnConveyor
	 * @param bagsOnStoreroom
	 * @param busDriverState
	 * @param waitingQueue
	 * @param busSeats
	 * @param passengersState
	 * @param passengersSituation
	 * @param passengersStartingBags
	 * @param passengersCurrentBags
	 */
	public GeneralRepository(String logFilePath, int[] flightPassengers, 
								int bagsOnPlane, PorterState porterState, 
								int bagsOnConveyor, int bagsOnStoreroom, 
								BusDriverState busDriverState, int[] waitingQueue, int[] busSeats, 
								PassengerState[] passengersState, String[] passengersSituation, 
								int[] passengersStartingBags, int[] passengersCurrentBags) {
		this.logFilePath = logFilePath;
		this.flight = 0;
		this.flightPassengers = flightPassengers;
		this.bagsOnPlane = bagsOnPlane;
		this.porterState = porterState;
		this.bagsOnConveyor = bagsOnConveyor;
		this.bagsOnStoreroom = bagsOnStoreroom;
		this.busDriverState = busDriverState;
		this.waitingQueue = waitingQueue;
		this.busSeats = busSeats;
		this.passengersState = passengersState;
		this.passengersSituation = passengersSituation;
		this.passengersStartingBags = passengersStartingBags;
		this.passengersCurrentBags = passengersCurrentBags;
		this.passengersFDT = 0;
		this.passengersTRT = 0;
		this.transportedBags = 0;
		this.lostBags = 0;
		this.init();
	}

	// Basic logging function utilities

	/**
	 * Logger initialization method
	 * <p/>
	 * Must always close buffers before exiting method
	 */
	private void init() {
		try {
			FileWriter fileWriter = new FileWriter(logFilePath);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// regular prints
			bufferedWriter.write("\t\t\tAIRPORT RHAPSODY - Description of the internal state of the problem\n");
			bufferedWriter.write("PLANE\tPORTER\t\tDRIVER\n");
			bufferedWriter.write("FN BN Stat CB SR\tStat");
			
			// printing occupation of wait queue
			for (int queueOccupant=1; queueOccupant <= this.waitingQueue.length; queueOccupant++) { bufferedWriter.write(String.format(" Q%d",queueOccupant)); }

			// printing bus occupation
			for (int seat=1; seat <= this.busSeats.length; seat++) { bufferedWriter.write(String.format(" S%d",seat)); }

			bufferedWriter.write("\n\t\t\t\t\t\tPASSENGERS\n");
			
			// printing flight passengers
			for (int passenger=0; passenger < this.flightPassengers.length; passenger++) { bufferedWriter.write(String.format("St%d Si%d NR%d NA%d ", passenger+1, passenger+1, passenger+1, passenger+1)); }

			bufferedWriter.write("\n");
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(e);
			System.err.println("Error initiating logger");
			e.printStackTrace();
			System.exit(1);
		}
	} 

    /**
	 * Logger finalization method
	 * <p/>
	 * Must always close buffers before exiting method
	 */
	public void finish() {
		try {
			FileWriter fileWriter = new FileWriter(logFilePath, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write("Final report\n");
			bufferedWriter.write(
				String.format(
					"N. of passengers which have this airport as their final destination = %d\n", 
					this.passengersFDT
				)
			);
			bufferedWriter.write(
				String.format(
					"N. of passengers in transit = %d\n", 
					this.passengersTRT
				)
			);
			bufferedWriter.write(
				String.format(
					"N. of bags that should have been transported in the planes hold = %d\n", 
					this.transportedBags
				)
			);
			bufferedWriter.write(
				String.format(
					"N. of bags that were lost = %d\n", 
					this.lostBags
				)
			);

			bufferedWriter.write("\n");
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(e);
			System.err.println("Error initiating logger");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Logger file update method
	 * <p/>
	 * Used always when something is updated
	 */
	private void updateFileLog() {
		try {
			FileWriter fileWriter = new FileWriter(logFilePath, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			/*
			 * Logger works
			 * For each flight will print all data for that flight
			 * inside will have the queue and bus seats 
			 */
			// writing about flight stuff
			bufferedWriter.write(
				String.format(
					"%2d %2d %s %2d %2d %s ",
					this.flight+1, this.bagsOnPlane, this.porterState, this.bagsOnConveyor, 
					this.bagsOnStoreroom, this.busDriverState  
				)
			);
			// writing about waiting queue
			for (int q=1; q <= this.waitingQueue.length; q++) {
				if (this.waitingQueue[q-1] == -1) {
					bufferedWriter.write("-- ");
				} else {
					bufferedWriter.write(String.format("%2d ", this.waitingQueue[q-1]));
				}
			}
			// writing about bus seats
			for (int s=1; s <= this.busSeats.length; s++) {
				if (this.busSeats[s-1] == -1) {
					bufferedWriter.write("-- ");
				} else {
					bufferedWriter.write(String.format("%2d ", this.busSeats[s-1]));
				}
			}

			bufferedWriter.write("\n");

			// writing about passengers
			for (int p=0; p < this.flightPassengers.length; p++) {
				if (this.flightPassengers[p] == -1) {
					bufferedWriter.write("--- --- --- --- ");
				} else {
					int pId=this.flightPassengers[p];
					bufferedWriter.write(String.format(
						"%s %s %3d %3d ", 
						this.passengersState[pId], this.passengersSituation[pId], 
						this.passengersStartingBags[pId], this.passengersCurrentBags[pId]
						)
					);
				}
			}
			// flush to new line
			bufferedWriter.write("\n");
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println("Error updating file log");
			System.exit(2);
		}
	}

	// States update functions
	/**
	 * Update Porter State
	 * <p/>
	 * @param newPorterState
	 * @param noLog
	 */
	public synchronized void updatePorterState(PorterState newPorterState, boolean noLog){
		this.porterState=newPorterState;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Update Bus Driver State
	 * <p/>
	 * @param newBusDriverState
	 * @param noLog
	 */
	public synchronized void updateBusDriverState(BusDriverState newBusDriverState, boolean noLog) {
		this.busDriverState=newBusDriverState;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Update Passenger state
	 * <p/>
	 * @param newPassengerState
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void updatePassengerState(PassengerState newPassengerState, int passengerId, boolean noLog){
		this.passengersState[passengerId]=newPassengerState;
		if (!noLog){
			this.updateFileLog();
		}
	}

	// Bus update functions
	/**
	 * Update bus waiting line with one new passenger
	 * <p/>
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void addToWaitingQueue(int passengerId, boolean noLog) {
		for (int i = 0; i < this.waitingQueue.length; i++) {
			if (this.waitingQueue[i] == -1) {
				this.waitingQueue[i] = passengerId;
				break;
			}
		}
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Update bus waiting line with one less passenger
	 * <p/>
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void removeFromWaitingQueue(int passengerId, boolean noLog) {
		for (int i = 0; i < this.waitingQueue.length; i++) {
			if (this.waitingQueue[i] == passengerId) {
				this.waitingQueue[i] = -1;
				break;
			}
		}
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Update bus seats with one new passenger
	 * <p/>
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void addToBusSeat(int passengerId, boolean noLog) {
		for (int i = 0; i < this.busSeats.length; i++) {
			if (this.busSeats[i] == -1) {
				this.busSeats[i] = passengerId;
				break;
			}
		}
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Update bus seats with one less passenger
	 * <p/>
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void removeFromBusSeat(int passengerId, boolean noLog) {
		for (int i = 0; i < this.busSeats.length; i++) {
			if (this.busSeats[i] == passengerId) {
				this.busSeats[i] = -1;
				break;
			}
		}
		if (!noLog){
			this.updateFileLog();
		}
	}

	// Flights update functions
	/**
	 * Update flight with one new passenger
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void addPassengerToFlight(int passengerId, boolean noLog) {
		for (int i = 0; i < this.flightPassengers.length; i++) {
			if (this.flightPassengers[i] == -1) {
				this.flightPassengers[i] = passengerId;
				break;
			}
		}
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Update flight with one new passenger
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void removePassengerFromFlight(int passengerId, boolean noLog) {
		for (int i = 0; i < this.flightPassengers.length; i++) {
			if (this.flightPassengers[i] == passengerId) {
				this.flightPassengers[i] = -1;
				break;
			}
		}
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Updates the flightId with the new flightId
	 * @param flight
	 * @param noLog
	 */
	public synchronized void updateFlight(int flight, boolean noLog) {
		this.flight=flight;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Method to clear all data relative to the flight
	 * @param noLog
	 */
	public synchronized void clearFlight(boolean noLog){
		Arrays.fill(this.flightPassengers, -1);
		if (!noLog) {
			this.updateFileLog();
		}

	}

	/**
	 * Updates the amount of bags currently in the plane's hold
	 * @param bagsInPlane
	 * @param noLog
	 */
	public synchronized void updateBagsInPlane(int bagsInPlane, boolean noLog){
		this.bagsOnPlane=bagsInPlane;
		if (!noLog){
			this.updateFileLog();
		}
	}

	// Porter update functions
	/**
	 * Updates the amount of bags in the conveyor's belt with a 
	 * @param bagAmount
	 * @param noLog
	 */
	public synchronized void updateConveyorBags(int bagAmount, boolean noLog) {
		this.bagsOnConveyor=bagAmount;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Updates the amount of bags in the conveyor's belt
	 * @param bagAmount
	 * @param noLog
	 */
	public synchronized void updateStoreRoomBags(int bagAmount, boolean noLog) {
		this.bagsOnStoreroom=bagAmount;
		if (!noLog){
			this.updateFileLog();
		}
	}

	// Passenger updates
	/**
	 * Updates the passenger situation (TRT or FDT)
	 * @param passengerId
	 * @param situation
	 * @param noLog
	 */
	public synchronized void updateSituation(int passengerId, String situation, boolean noLog){
		this.passengersSituation[passengerId]=situation;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Updates the starting bags of a passenger
	 * @param passengerId
	 * @param startingBags
	 * @param noLog
	 */
	public synchronized void updateStartingBags(int passengerId, int startingBags, boolean noLog){
		this.passengersStartingBags[passengerId]=startingBags;
		if (!noLog){
			this.updateFileLog();
		}
	}
	
	/**
	 * Updates the current bags a passenger is holding
	 * @param passengerId
	 * @param bagAmount
	 * @param noLog
	 */
	public synchronized void updateCurrentBags(int passengerId, int bagAmount, boolean noLog){
		this.passengersCurrentBags[passengerId]=bagAmount;
		if (!noLog){
			this.updateFileLog();
		}
	}

	// Statistics updates
	/**
	 * Increases the FDT-type passengers with the amount given in the entry parameter
	 * @param amountIncrease
	 * @param noLog
	*/
	public synchronized void updateFDTPassengers(int amountIncrease, boolean noLog){
		this.passengersFDT+=amountIncrease;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Increases the TRT-type passengers with the amount given in the entry parameter
	 * @param amountIncrease
	 * @param noLog
	*/
	public synchronized void updateTRTPassengers(int amountIncrease, boolean noLog){
		this.passengersTRT+=amountIncrease;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Increses the amount of bags that should have been transported in the planes hold
	 * @param amountIncrease
	 * @param noLog
	 */
	public synchronized void updatePlaneHoldBags(int amountIncrease, boolean noLog){
		this.transportedBags+=amountIncrease;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Increases the amount of lost bags
	 * @param amountIncrease
	 * @param noLog
	 */
	public synchronized void updateLostbags(int amountIncrease, boolean noLog) {
		this.lostBags+=amountIncrease;
		if (!noLog){
			this.updateFileLog();
		}
	}
}