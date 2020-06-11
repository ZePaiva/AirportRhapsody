package Rhapsody.client;

import Rhapsody.client.entities.BusDriver;
import Rhapsody.client.stubs.ArrivalQuayStub;
import Rhapsody.client.stubs.DepartureQuayStub;
import Rhapsody.common.States;

/**
 * Bus Driver main
 * 
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BusDriverMain {

    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String[] args) {

        ArrivalQuayStub arrivalQuay = new ArrivalQuayStub();
        DepartureQuayStub departureQuay = new DepartureQuayStub();

        BusDriver busDriver = new BusDriver(100, arrivalQuay, departureQuay);

        busDriver.start();
        try {
            busDriver.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        arrivalQuay.closeStub();
        departureQuay.closeStub();
    }
}