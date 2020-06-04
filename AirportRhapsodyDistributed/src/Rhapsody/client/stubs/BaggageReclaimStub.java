package Rhapsody.client.stubs;

import Rhapsody.common.RunParameters;

/**
 * Baggage Reclaim stub for the clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class BaggageReclaimStub {
    
     /**
     * Server name of the Arrival Exit server
     */
    private String serverHostName;

    /**
     * Server port of the Arrival Exit server
     */
    private int serverHostPort;

    /**
     * Bagggage reclaim stub constructor
     */
    public BaggageReclaimStub() {
        this.serverHostName=RunParameters.BaggageReclaimHostName;
        this.serverHostPort=RunParameters.BaggageReclaimPort;
    }

    /**
	 * Method to client register his complaint about any lost luggage
	 * @param lostBags
	 */
	public void reportMissingBags(int lostBags) {
    }
}