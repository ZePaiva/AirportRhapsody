package Rhapsody.client.stubs;

import Rhapsody.common.Luggage;
import Rhapsody.common.RunParameters;

/**
 * Storage area stub 
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class StorageAreaStub {
    
    /**
     * Server name of the Arrival Exit server
     */
    private String serverHostName;

    /**
     * Server port of the Arrival Exit server
     */
    private int serverHostPort;

    /**
     * Stub constructor
     */
    public StorageAreaStub() {
        this.serverHostName=RunParameters.StorageAreaHostName;
        this.serverHostPort=RunParameters.StorageAreaPort;
    }

    /**
     * Method to deposit a bag in storeroom. <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR CONVEYOR BELT</b>  
     * @param luggage
     */
    public void carryItToAppropriateStore(Luggage luggage) {
        
    }
}