package Rhapsody.common;

import java.util.Date;

/**
 * Simulation parameters
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */

public class RunParameters {

    /**
     * Number of planes landing
     */
    public static final int K = 5;

    /**
     * Number of passengers arriving in each plane
     */
    public static final int N = 1;

    /**
     * Number of maximumum luggage each passenger has
     */
    public static final int M = 2;

    /**
     * Number of seats on the bus
     */
    public static final int T = 1;

    /**
     * Bus scheduled timout
     */
    public static final int busSchedule = 100;

    /**
     * Log filename
     */
    public static final String logFile = "log_"+ new Date().toString().replace(' ', '_') +".txt";


    /**
     * Arrival Lounge Port
     */
    public static final int ArrivalLoungePort = 22120;

    /** 
     * Arrival Lounge Hostname
     */
    public static final String ArrivalLoungeHostName = "localhost";

    /**
     * Arrival Exit Port
     */
    public static final int ArrivalExitPort = 22121;

    /**
     * Arrival Exit Hostname
     */
    public static final String ArrivalExitHostName = "localhost";

    /**
     * Arrival Terminal Transfer Quay Port
     */
    public static final int ArrivalQuayPort = 22122;

    /**
     * Arrival Terminal Transfer Quay Hostname
     */
    public static final String ArrivalQuayHostName = "localhost";

    /**
     * Baggage Collection Point Port
     */
    public static final int BaggageCollectionPort = 22123;
     
    /**
     * Baggage Collection Point Hostname
     */
    public static final String BaggageCollectionHostName = "localhost";

    /**
     * Baggage Reclaim Office Port
     */
    public static final int BaggageReclaimPort = 22124;

    /**
     * Baggage Reclaim Office Port
     */
    public static final String BaggageReclaimHostName = "localhost";

    /**
     * Departure Terminal Entrance Port
     */
    public static final int DepartureEntrancePort = 22125;

    /**
     * Departure Terminal Entrance Hostname
     */
    public static final String DepartureEntranceHostName = "localhost";

    /**
     * Departure Terminal Transfer Quay Port
     */
    public static final int DepartureQuayPort = 22126;

    /** 
     * Departure Terminal Transfer Quay Hostname
     */
    public static final String DepartureQuayHostName = "localhost";

    /**
     * Storage Area Port
     */
    public static final int StorageAreaPort = 22127;

    /**
     * Storage Area Port
     */
    public static final String StorageAreaHostName = "localhost";

    /**
     * Repository Port
     */
    public static final int RepositoryPort = 22128;

    /**
     * Repository Hostname
     */
    public static final String RepositoryHostName = "localhost";
    
}