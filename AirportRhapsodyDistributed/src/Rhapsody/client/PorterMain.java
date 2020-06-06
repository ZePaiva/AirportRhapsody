package Rhapsody.client;

import Rhapsody.client.entities.Porter;
import Rhapsody.client.stubs.StorageAreaStub;
import Rhapsody.client.stubs.ArrivalLoungeStub;
import Rhapsody.client.stubs.BaggageCollectionStub;

/**
 * Porter main
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class PorterMain {

    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String[] args) {

        StorageAreaStub storageArea = new StorageAreaStub();
        ArrivalLoungeStub lounge = new ArrivalLoungeStub();
        BaggageCollectionStub baggageCollectionPoint = new BaggageCollectionStub();

        Porter porter = new Porter(storageArea, lounge, baggageCollectionPoint);

        porter.start();
        try {
            porter.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        storageArea.closeStub();
        lounge.closeStub();
        baggageCollectionPoint.closeStub();

    }
}