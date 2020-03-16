package Rhapsody.utils;

import Rhapsody.entities.*;
import Rhapsody.entities.states.BusDriverState;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.entities.states.PorterState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logger class
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class Logger {

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
	private int flightPassengers[];
	/**
	 * Number of bags in plane's hold
	 */
	private int bagsOnPlane;
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
	 * Logger constructor
	 * <p/>
	 * @param logFilePath (String)
	 * @param flights (int)
	 * @param flightPassengers (int [][])
	 * @param bagsOnPlane (int [])
	 * @param porterState (PorterState)
	 * @param bagsOnConveyor (int)
	 * @param bagsOnStoreroom (int)
	 * @param busDriverState (BusDriverState)
	 * @param waitingQueue (int [])
	 * @param busSeats (int [])
	 * @param passengersState (PassengerState [])
	 * @param passengersSituation (String [])
	 * @param passengersStartingBags (int [])
	 * @param passengersCurrentBags (int [])
	 */
	public Logger(String logFilePath, int flight, int[] flightPassengers, int bagsOnPlane, 
					PorterState porterState, int bagsOnConveyor, int bagsOnStoreroom, 
					BusDriverState busDriverState, int[] waitingQueue, int[] busSeats, 
					PassengerState[] passengersState, String[] passengersSituation, 
					int[] passengersStartingBags, int[] passengersCurrentBags, 
					int[] passengerFlight) {
		this.logFilePath = logFilePath;
		this.flight = flight;
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
	private synchronized void init() {
		try {
			FileWriter fileWriter = new FileWriter(logFilePath);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// regular prints
			bufferedWriter.write("AIRPORT RHAPSODY - Description of the internal state of the problem\n");
			bufferedWriter.write("PLANE\tPORTER\t\tDRIVER                       Passengers\n");
			bufferedWriter.write("FN BN\tStat CB SR\tStat");
			
			// printing occupation of wait queue
			for (int queueOccupant=1; queueOccupant <= this.waitingQueue.length; queueOccupant++) { bufferedWriter.write(String.format(" Q%d",queueOccupant)); }

			// printing bus occupation
			for (int seat=1; seat <= this.busSeats.length; seat++) { bufferedWriter.write(String.format(" S%d",seat)); }

			// printing flight passengers
			for (int passenger=1; passenger <= this.flightPassengers.length; passenger++) { bufferedWriter.write(String.format(" St%d Si%d NR%d NA%d", passenger, passenger, passenger, passenger)); }

			bufferedWriter.write("\n");
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			System.err.println(e);
			System.err.println("Error initiating logger");
			System.exit(1);
		}
	} 

	/**
	 * Logger file update method
	 * <p/>
	 * Used always when something is updated
	 */
	private synchronized void updateFileLog() {
		try {
			FileWriter fileWriter = new FileWriter(logFilePath);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			/*
			 * Logger works
			 * For each flight will print all data for that flight
			 * inside will have the queue and bus seats 
			 */
			// writing about flight stuff
			bufferedWriter.write(
				String.format(
					"%2d %2d\t%s %3d %3d\t%s ",
					this.flight, this.bagsOnPlane, this.porterState, this.bagsOnConveyor, this.lostBags, this.busDriverState  
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
			// writing about passengers
			for (int p=1; p <= this.flightPassengers.length; p++) {
				if (this.flightPassengers[p-1] == -1) {
					bufferedWriter.write("------------------ --- -- --");
				} else {
					int pId=this.flightPassengers[p-1];
					bufferedWriter.write(String.format(
						"%s %s %2d %2d ", 
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
	 */
	public synchronized void updatePorterState(PorterState newPorterState){
		this.porterState=newPorterState;
		this.updateFileLog();
	}

	/**
	 * Update Bus Driver State
	 * <p/>
	 * @param newBusDriverState
	 */
	public synchronized void updateBusDriverState(BusDriverState newBusDriverState) {
		this.busDriverState=newBusDriverState;
		this.updateFileLog();
	}

	/**
	 * Update Passenger state
	 * <p/>
	 * @param newPassengerState
	 * @param passengerId
	 */
	public synchronized void updatePassengerState(PassengerState newPassengerState, int passengerId){
		this.passengersState[passengerId]=newPassengerState;
		this.updateFileLog();
	}

	// Bus update functions
	/**
	 * Update bus waiting line with one new passenger
	 * <p/>
	 * @param passengerId
	 */
	public synchronized void addToWaitingQueue(int passengerId) {
		for (int i = 0; i < this.waitingQueue.length; i++) {
			if (this.waitingQueue[i] == -1) {
				this.waitingQueue[i] = passengerId;
				break;
			}
		}
		this.updateFileLog();
	}

	/**
	 * Update bus waiting line with one less passenger
	 * <p/>
	 * @param passengerId
	 */
	public synchronized void removeFromWaitingQueue(int passengerId) {
		for (int i = 0; i < this.waitingQueue.length; i++) {
			if (this.waitingQueue[i] == passengerId) {
				this.waitingQueue[i] = -1;
				break;
			}
		}
		this.updateFileLog();
	}

	/**
	 * Update bus seats with one new passenger
	 * <p/>
	 * @param passengerId
	 */
	public synchronized void addToBusSeat(int passengerId) {
		for (int i = 0; i < this.busSeats.length; i++) {
			if (this.busSeats[i] == -1) {
				this.busSeats[i] = passengerId;
				break;
			}
		}
		this.updateFileLog();
	}

	/**
	 * Update bus seats with one less passenger
	 * <p/>
	 * @param passengerId
	 */
	public synchronized void removeFromBusSeat(int passengerId) {
		for (int i = 0; i < this.busSeats.length; i++) {
			if (this.busSeats[i] == passengerId) {
				this.busSeats[i] = -1;
				break;
			}
		}
		this.updateFileLog();
	}

	// Flights update functions
	/**
	 * Update flight with one new passenger
	 * @param passengerId
	 */
	public synchronized void addPassengerToFlight(int passengerId) {
		for (int i = 0; i < this.flightPassengers.length; i++) {
			if (this.flightPassengers[i] == -1) {
				this.flightPassengers[i] = passengerId;
				break;
			}
		}
		this.updateFileLog();
	}

	/**
	 * Update flight with one new passenger
	 * @param passengerId
	 */
	public synchronized void removePassengerFromFlight(int passengerId) {
		for (int i = 0; i < this.flightPassengers.length; i++) {
			if (this.flightPassengers[i] == passengerId) {
				this.flightPassengers[i] = -1;
				break;
			}
		}
		this.updateFileLog();
	}

	/**
	 * Update plane luggage amount
	 * @param bagAmount
	 */
	public synchronized void updateBagsInPlane(int bagAmount) {
		this.bagsOnPlane=bagAmount;
		this.updateFileLog();
	}

	/**
	 * Updates the flightId with the new flightId
	 * @param flight
	 */
	public synchronized void updateFlight(int flight) {
		this.flight=flight;
		this.updateFileLog();
	}

	// Porter update functions
	/**
	 * Updates the amount of bags in the conveyor's belt with a 
	 * @param bagAmount
	 */
	public synchronized void updateConveyorBags(int bagAmount) {
		this.bagsOnConveyor=bagAmount;
		this.updateFileLog();
	}

	/**
	 * Updates the amount of bags in the conveyor's belt
	 * @param bagAmount
	 */
	public synchronized void updateStoreRoomBags(int bagAmount) {
		this.bagsOnStoreroom=bagAmount;
		this.updateFileLog();
	}

	// Passenger updates
	/**
	 * Updates the passenger situation (TRT or FDT)
	 * @param passengerId
	 * @param situation
	 */
	public synchronized void updateSituation(int passengerId, String situation){
		this.passengersSituation[passengerId]=situation;
		this.updateFileLog();
	}

	/**
	 * Updates the starting bags of a passenger
	 * @param passengerId
	 * @param startingBags
	 */
	public synchronized void updateStartingBags(int passengerId, int startingBags){
		this.passengersStartingBags[passengerId]=startingBags;
		this.updateFileLog();
	}
	
	/**
	 * Updates the current bags a passenger is holding
	 * @param passengerId
	 * @param bagAmount
	 */
	public synchronized void updateCurrentBags(int passengerId, int bagAmount){
		this.passengersCurrentBags[passengerId]=bagAmount;
		this.updateFileLog();
	}

	// Statistics updates
	/**
	 * Increases the FDT-type passengers with the amount given in the entry parameter
	 * @param amountIncrease
	*/
	public synchronized void updateFDTPassengers(int amountIncrease){
		this.passengersFDT+=amountIncrease;
		this.updateFileLog();
	}

	/**
	 * Increases the TRT-type passengers with the amount given in the entry parameter
	 * @param amountIncrease
	*/
	public synchronized void updateTRTPassengers(int amountIncrease){
		this.passengersTRT+=amountIncrease;
		this.updateFileLog();
	}

	/**
	 * Increses the amount of bags that should have been transported in the planes hold
	 * @param amountIncrease
	 */
	public synchronized void updatePlaneHoldBags(int amountIncrease){
		this.transportedBags+=amountIncrease;
		this.updateFileLog();
	}

	/**
	 * Increases the amount of lost bags
	 * @param amountIncrease
	 */
	public synchronized void updateLostbags(int amountIncrease) {
		this.lostBags+=amountIncrease;
		this.updateFileLog();
	}
}