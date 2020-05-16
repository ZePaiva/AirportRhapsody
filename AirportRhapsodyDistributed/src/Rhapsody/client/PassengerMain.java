package Rhapsody.client;

import Rhapsody.client.entities.Passenger;
import Rhapsody.client.stubs.ArrivalExitStub;
import Rhapsody.client.stubs.ArrivalLoungeStub;
import Rhapsody.client.stubs.ArrivalQuayStub;
import Rhapsody.client.stubs.BaggageCollectionStub;
import Rhapsody.client.stubs.BaggageReclaimStub;
import Rhapsody.client.stubs.DepartureEntranceStub;
import Rhapsody.client.stubs.DepartureQuayStub;
import Rhapsody.client.stubs.RepositoryStub;

/**
 * Passenger Thread Implements the life-cycle of the passenger and stores it's
 * current state.
 * <p/>
 * 
 * Passenger life-cycle as 2 possible ways
 * <ul>
 * <li>Arrival with all bags
 * <ol>
 * <li>Passengers arrives to the disembarking zone
 * <li>Passenger goes to luggage collection point (conveyor or storeroom)
 * <li>Passenger tries to collect a bag
 * <li>Repeat step 3 until passenger successfully collects all bags
 * <li>Passenger successfully collects all bags and goes Home or departs
 * </ol>
 * <li>Arrival with lost bags
 * <ol>
 * <li>Passengers arrives to the disembarking zone
 * <li>Passenger goes to luggage collection point (conveyor or storeroom)
 * <li>Passenger tries to collect a bag
 * <li>Repeat step 3 until passenger notices bag is lost
 * <li>Passenger goes to baggage reclaim office
 * <li>Passenger goes Home or departs
 * </ol>
 * <li>Departure
 * <ol>
 * <li>Passengers arrives to the Airport
 * <li>Passenger try to take a bus
 * <li>Passenger enters the bus
 * <li>Passenger exits the bus
 * <li>Passenger enter departure terminal
 * <li>Passenger prepares next leg
 * </ol>
 * </ul>
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class PassengerMain {
    
    /**
     * Passenger main
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        /**
         * Initialize stubs for communication
         */
        ArrivalLoungeStub arrivalLoungeStub = new ArrivalLoungeStub();
        BaggageCollectionStub baggageCollectionStub = new BaggageCollectionStub();
        BaggageReclaimStub baggageReclaimStub = new BaggageReclaimStub();
        ArrivalExitStub arrivalExitStub = new ArrivalExitStub();
        ArrivalQuayStub arrivalQuayStub = new ArrivalQuayStub();
        DepartureEntranceStub departureEntranceStub = new DepartureEntranceStub();
        DepartureQuayStub departureQuayStub = new DepartureQuayStub();
        RepositoryStub repositoryStub = new RepositoryStub();

        /**
         * Initialize passenger entity
         */
        Passenger passenger = new Passenger();

        

    }
}