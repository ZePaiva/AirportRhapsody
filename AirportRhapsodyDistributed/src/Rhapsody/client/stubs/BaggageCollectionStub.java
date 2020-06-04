package Rhapsody.client.stubs;

import Rhapsody.common.Luggage;
import Rhapsody.common.RunParameters;

/**
 * Luggage collection stub 
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class BaggageCollectionStub {
    
    /**
     * Server name of the Arrival Exit server
     */
    private String serverHostName;

    /**
     * Server port of the Arrival Exit server
     */
    private int serverHostPort;

    /**
     * Prettify
     */
    public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    /**
     * Baggage collection stub constructor
     */
    public BaggageCollectionStub(){
        this.serverHostName=RunParameters.BaggageCollectionHostName;
        this.serverHostPort=RunParameters.BaggageCollectionPort;
    }

    /**
     * Method to deposit a bag in the conveyor belt <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM</b>  
     * @param luggage
     */
    public void carryItToAppropriateStore(Luggage luggage) {
        
    }

    /**
     * Method to signal all passengers that luggage collection has ended 
     */
    public void noMoreBagsToCollect() {
    
    }

    /**
     * Method for a passenger to try to collect the bags
     * @param startingBags
     * @return currentBags
     */
    public int goCollectABag(int startingBags){
        return 0;
    }

    /**
     * Mehtod to reset baggage collection variable by the porter thread
     */
    public void newFlight(){
    }
}