package Rhapsody.client;

import java.util.Random;

import Rhapsody.client.entities.Passenger;
import Rhapsody.client.stubs.ArrivalExitStub;
import Rhapsody.client.stubs.ArrivalLoungeStub;
import Rhapsody.client.stubs.ArrivalQuayStub;
import Rhapsody.client.stubs.BaggageCollectionStub;
import Rhapsody.client.stubs.BaggageReclaimStub;
import Rhapsody.client.stubs.DepartureEntranceStub;
import Rhapsody.client.stubs.DepartureQuayStub;
import Rhapsody.common.RunParameters;

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
         * Passengers array
         */
        Passenger[] passengers = new Passenger[RunParameters.N];

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

        Random random = new Random();

        /**
         * Passenger generation
         */
        int[][] luggage = new int[RunParameters.N][RunParameters.K];
		String[][] situations = new String[RunParameters.N][RunParameters.K];

		for (int flights=0; flights < RunParameters.K; flights++) {
			for (int j=0; j < RunParameters.N; j++) {
				luggage[j][flights] = random.nextInt(RunParameters.M+1);
				situations[j][flights] = random.nextBoolean() ? "FDT" : "TRT";
			}
		}


        /**
         * Initialize passenger entity
         */
        for (int i = 0; i < RunParameters.N; i++){
            passengers[i] = new Passenger(i, luggage[i], situations[i], 
                                            arrivalExitStub, arrivalLoungeStub, 
                                            arrivalQuayStub, baggageCollectionStub, 
                                            baggageReclaimStub, departureEntranceStub, 
                                            departureQuayStub);
            passengers[i].start();
        }

        for (Passenger passenger: passengers) {
            try {
                passenger.join();
            } catch (InterruptedException e) {
                System.err.printf("%s interrupted\n", Thread.currentThread().getName());
                e.printStackTrace();
                System.exit(400);
            } catch (Exception e) {
                System.err.printf("%s unknown error, check logs\n", Thread.currentThread().getName());
                e.printStackTrace();
                System.exit(10);
            }
        }

        arrivalLoungeStub.closeStub();
        baggageCollectionStub.closeStub();
        baggageReclaimStub.closeStub();
        arrivalQuayStub.closeStub();
        departureEntranceStub.closeStub();
        departureQuayStub.closeStub();
    }
}