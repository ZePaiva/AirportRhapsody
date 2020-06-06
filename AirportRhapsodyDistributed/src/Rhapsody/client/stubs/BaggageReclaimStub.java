package Rhapsody.client.stubs;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.Passenger;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Baggage Reclaim stub for the clients
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class BaggageReclaimStub {
    
    /**
	 * Client communication channelt
	 */
	private final ClientCom clientCom;

    /**
     * Bagggage reclaim stub constructor
     */
    public BaggageReclaimStub() {
        clientCom = new ClientCom(RunParameters.BaggageReclaimHostName, RunParameters.BaggageReclaimPort);
		clientCom.open();
    }

    /**
	 * Method to client register his complaint about any lost luggage
	 * @param lostBags
	 */
	public void reportMissingBags(int lostBags) {
        Passenger passenger = (Passenger) Thread.currentThread();
        Message pkt = new Message();
        pkt.setType(MessageType.PASSENGER_COMPLAINT);
        pkt.setId(passenger.getPassengerId());
        pkt.setInt1(lostBags);

        clientCom.writeObject(pkt);
        pkt = (Message) clientCom.readObject();
    }

    /**
     * Close the stub connection
     */
	public void closeStub() {
        Message pkt = new Message();
        pkt.setType(MessageType.SIM_ENDED);
        clientCom.writeObject(pkt);
        clientCom.close();
	}
}