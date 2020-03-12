package Rhapsody.interfaces;

import Rhapsody.entities.*;
import Rhapsody.entities.states.BusDriverState;
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

    // General Data
    /**
     * Path to Log File
     */
    private String fileLogPath;
    /**
     * Number of planes (flights)
     */
    private int planes;
    /**
     * Number of Total passengers
     */
    private int passengers;
    /**
     * Number of seats in the airport bus
     */
    private int busSeats;
    /**
     * Occupation state for the waiting queue 
     */
    private int queueOccupation;

    // Statistics Data
    /**
     * Number of pasengers with this airport as their final destination
     */
    private int finalDestinationPassengers;
    /**
     * Number of passengers in transit
     */
    private int passengersInTransit;
    /**
     * NUmber of bags that should have been trasnported in the planes hold
     */
    private int predicetdTransportedBags;
    /**
     * Number of bags that were lost
     */
    private int lostBags;

    // Classification Data
    /**
     * Flight of each passenger
     */
    private int [] passengerFlight;

    public Logger (String fileLogPath, int planes, int passengers, int seats) {
        this.fileLogPath=fileLogPath;
        this.planes=planes;
        this.passengers=passengers;
        this.busSeats=seats;
        this.finalDestinationPassengers=0;
        this.passengersInTransit=0;
        this.predicetdTransportedBags=0;
        this.lostBags=0;
        this.init();
    }

    private synchronized void init() {
        try {
            FileWriter fileWriter = new FileWriter(fileLogPath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // regular prints
            bufferedWriter.write("AIRPORT RHAPSODY - Description of the internal state of the problem\n");
            bufferedWriter.write("PLANE\tPORTER\t\t\tDRIVER\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPassengers\n");
            bufferedWriter.write("FN BN\tStat CB SR\tStat");
            
            // printing occupation of wait queue
            for (int queueOccupant=1; queueOccupant <= this.queueOccupation; queueOccupant++) { bufferedWriter.write(String.format(" Q%d",queueOccupant)); }

            // printing bus occupation
            for (int seat=1; seat <= this.busSeats; seat++) { bufferedWriter.write(String.format(" S%d",seat)); }

            // printing flight passengers
            for (int passenger=1; passenger <= this.passengers; passenger++) { bufferedWriter.write(String.format(" St%d Si%d NR%d NA%d", passenger, passenger, passenger, passenger)); }

            bufferedWriter.write("\n");
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error initiating logger");
            System.exit(1);
        }
    } 

    private synchronized void updateFileLog() {
        try {
            FileWriter fileWriter = new FileWriter(fileLogPath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // stuff to log

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error updating file log");
            System.exit(2);
        }

    }
}