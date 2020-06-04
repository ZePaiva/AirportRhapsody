package Rhapsody.client.stubs;

import Rhapsody.common.RunParameters;

/**
 * General repository stub 
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class GeneralRepositoryStub {

    /**
     * Server name of the Arrival Exit server
     */
    private String serverHostName;

    /**
     * Server port of the Arrival Exit server
     */
    private int serverHostPort;

    /**
     *  Stub constructor
     */
    public GeneralRepositoryStub() {
        this.serverHostName=RunParameters.RepositoryHostName;
        this.serverHostPort=RunParameters.RepositoryPort;
    }

    /**
	 * Update flight with one new passenger
	 * @param passengerId
	 * @param noLog
	 */
	public synchronized void addPassenger(int passengerId, boolean noLog) {

	}
}