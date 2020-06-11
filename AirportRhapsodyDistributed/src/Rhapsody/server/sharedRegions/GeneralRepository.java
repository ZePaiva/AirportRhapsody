package Rhapsody.server.sharedRegions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import Rhapsody.common.RunParameters;
import Rhapsody.common.States;

/**
 * Repository shared region entity code
 * Basically is a log handler
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class GeneralRepository {

    /**
     * Path to log file
     */
    private String logFilePath;

    /**
     * Flight ID
     */
    private int flight;

    /**
     * Current state of the Bus Driver
     */
    private States busDriverState;

    /**
     * Current state of the Porter
     */
    private States porterState;

    /**
     * Current state of the passengers
     */
    private States[] passengersStates;

    /**
     * Pasengers on the flight
     */
    private int[] flightPassengers;

    /**
     * Current bags on the conveyors belt
     */
    private int bagsOnConveyorBelt;

    /**
     * Current bags on the storeroom
     */
    private int bagsOnStoreRoom;

    /**
     * Bus waiting queue
     */
    private int[] waitingQueue;
    
    /**
     * Bus occupied seats
     */
    private int[] busSeats;

    /** 
     * Passengers situations (TRT or FDT) 
     */
    private String[] passengersSituation;

    /**
     * Passengers Starting Bags
     */
    private int[] startingBags;

    /**+
     * Passengers Current Bags
     */
    private int[] currentBags;

    /**
     * Total Passengers with final destination
     */
    private int passengersFDT;

    /**
     * Total passengers in transit
     */
    private int passengersTRT;

    /**
     * Total bags should have been transported on the plane hold
     */
    private int totalBagsTransported;

    /**
     * Total bags lost
     */
    private int totalBagsLost;

    /**
     * Number of bags in the plane's hold
     */
    private int bagsOnPlane;


    /**
     * General Repository constructor
     * 
     * @param logFilePath
     * @param flight
     * @param busDriverState
     * @param porterState
     * @param passengersStates
     * @param bagsOnConveyorBelt
     * @param bagsOnStoreRoom
     * @param waitingQueue
     * @param busSeats
     * @param passengersSituation
     * @param startingBags
     * @param currentBags
     * @param passengersFDT
     * @param passengersTRT
     * @param totalBagsTransported
     * @param totalBagsLost
     * @param bagsOnPlane
     */
	public GeneralRepository(String logFilePath, States busDriverState, 
			States porterState, States[] passengersStates, int[] waitingQueue, 
            int[] busSeats, String[] passengersSituation, int[] startingBags, 
            int[] currentBags) {
        this.logFilePath = logFilePath;
        this.busDriverState = busDriverState;
        this.porterState = porterState;
        this.passengersStates = passengersStates;
        this.waitingQueue = waitingQueue;
        this.busSeats = busSeats;
        this.passengersSituation = passengersSituation;
        this.startingBags = startingBags;
        this.currentBags = currentBags;
        this.bagsOnConveyorBelt = 0;
        this.bagsOnStoreRoom = 0;
        this.passengersFDT = 0;
        this.passengersTRT = 0;
        this.totalBagsTransported = 0;
        this.totalBagsLost = 0;
        this.bagsOnPlane = 0;
		this.flight = 0;
		this.flightPassengers=new int[RunParameters.N];
        this.init();
    }

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
			bufferedWriter.write("PLANE\tPORTER\t\t\t\tDRIVER\n");
			bufferedWriter.write("FN BN  Stat CB SR   Stat");
			
			// printing occupation of wait queue
			for (int queueOccupant=1; queueOccupant <= this.waitingQueue.length; queueOccupant++) { bufferedWriter.write(String.format(" Q%d",queueOccupant)); }

			bufferedWriter.write(" ");

			// printing bus occupation
			for (int seat=1; seat <= this.busSeats.length; seat++) { bufferedWriter.write(String.format(" S%d",seat)); }

			bufferedWriter.write("\n\t\t\t\t\t\tPASSENGERS\n");
			
			// printing flight passengers
			for (int passenger=0; passenger < RunParameters.K; passenger++) { bufferedWriter.write(String.format("St%d Si%d NR%d NA%d ", passenger+1, passenger+1, passenger+1, passenger+1)); }

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
					this.totalBagsTransported
				)
			);
			bufferedWriter.write(
				String.format(
					"N. of bags that were lost = %d\n", 
					this.totalBagsLost
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
					"%2d %2d  %s %2d %2d   %s ",
					this.flight+1, this.bagsOnPlane, this.porterState, this.bagsOnConveyorBelt, 
					this.bagsOnStoreRoom, this.busDriverState  
				)
			);
			// writing about waiting queue
			for (int q=1; q <= this.waitingQueue.length; q++) {
				if (this.waitingQueue[q-1] == -1) {
					bufferedWriter.write(" - ");
				} else {
					bufferedWriter.write(String.format("%2d ", this.waitingQueue[q-1]));
				}
			}
			bufferedWriter.write(" ");
			// writing about bus seats
			for (int s=1; s <= this.busSeats.length; s++) {
				if (this.busSeats[s-1] == -1) {
					bufferedWriter.write(" - ");
				} else {
					bufferedWriter.write(String.format("%2d ", this.busSeats[s-1]));
				}
			}

			bufferedWriter.write("\n");

			// writing about passengers
			for (int p=0; p < this.flightPassengers.length; p++) {
				if (this.flightPassengers[p] == -1) {
					bufferedWriter.write("--- ---  -   - ");
				} else {
					//int pId=this.flightPassengers[p];
					bufferedWriter.write(String.format(
						"%s %s %2d  %2d ", 
						this.passengersStates[p], this.passengersSituation[p], 
						this.startingBags[p], this.currentBags[p]
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
	public synchronized void updatePorterState(States newPorterState, boolean noLog){
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
	public synchronized void updateBusDriverState(States newBusDriverState, boolean noLog) {
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
	public synchronized void updatePassengerState(States newPassengerState, int passengerId, boolean noLog){
		this.passengersStates[passengerId]=newPassengerState;
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
	public synchronized void removeFromWaitingQueue(boolean noLog) {
		for (int i = 0; i < this.waitingQueue.length-1; i++) {
			this.waitingQueue[i]=this.waitingQueue[i+1];
		}
		this.waitingQueue[this.waitingQueue.length-1]=-1;
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
	public synchronized void removeFromBusSeat(boolean noLog) {
		for (int i = 0; i < this.busSeats.length-1; i++) {
			this.busSeats[i]=this.busSeats[i+1];
		}
		this.busSeats[this.busSeats.length-1]=-1;
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
		this.flightPassengers[passengerId]=1;
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
		this.flightPassengers[passengerId]=-1;
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
	 * 
	 * @param noLog
	 */
	public synchronized void clearFlight(boolean noLog){
		//System.out.println("clearing flight");
		//System.out.println(this.flightPassengers.toString());
		//System.out.println(this.flightPassengers.length);
		//System.out.println(Arrays.toString(this.flightPassengers));
		Arrays.fill(this.flightPassengers, -1);
		//System.out.println("Flight cleared");
		if (!noLog) {
			this.updateFileLog();
		}

	}

	/**
	 * Updates the amount of bags currently in the plane's hold
	 * 
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
		this.bagsOnConveyorBelt=bagAmount;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Updates the amount of bags in the conveyor's belt
	 * 
	 * @param bagAmount
	 * @param noLog
	 */
	public synchronized void updateStoreRoomBags(int bagAmount, boolean noLog) {
		this.bagsOnStoreRoom=bagAmount;
		if (!noLog){
			this.updateFileLog();
		}
	}

	// Passenger updates
	/**
	 * Updates the passenger situation (TRT or FDT)
	 * 
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
	 * 
	 * @param passengerId
	 * @param startingBags
	 * @param noLog
	 */
	public synchronized void updateStartingBags(int passengerId, int startingBags, boolean noLog){
		this.startingBags[passengerId]=startingBags;
		if (!noLog){
			this.updateFileLog();
		}
	}
	
	/**
	 * Updates the current bags a passenger is holding
	 * 
	 * @param passengerId
	 * @param bagAmount
	 * @param noLog
	 */
	public synchronized void updateCurrentBags(int passengerId, int bagAmount, boolean noLog){
		this.currentBags[passengerId]=bagAmount;
		if (!noLog){
			this.updateFileLog();
		}
	}

	// Statistics updates
	/**
	 * Increases the FDT-type passengers with the amount given in the entry parameter
	 * 
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
	 * 
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
	 * 
	 * @param amountIncrease
	 * @param noLog
	 */
	public synchronized void updatePlaneHoldBags(int amountIncrease, boolean noLog){
		this.totalBagsTransported+=amountIncrease;
		if (!noLog){
			this.updateFileLog();
		}
	}

	/**
	 * Increases the amount of lost bags
	 * 
	 * @param amountIncrease
	 * @param noLog
	 */
	public synchronized void updateLostbags(int amountIncrease, boolean noLog) {
		this.totalBagsLost+=amountIncrease;
		if (!noLog){
			this.updateFileLog();
		}
	}

}