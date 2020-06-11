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
     * Bagggage reclaim stub constructor
     */
    public BaggageReclaimStub() {
    }

    /**
	 * Method to client register his complaint about any lost luggage
	 * @param lostBags
	 */
	public void reportMissingBags(int lostBags) {
		ClientCom clientCom = new ClientCom(RunParameters.BaggageReclaimHostName, RunParameters.BaggageReclaimPort);
		clientCom.open();
        Passenger passenger = (Passenger) Thread.currentThread();
        Message pkt = new Message();
        pkt.setType(MessageType.PASSENGER_COMPLAINT);
        pkt.setId(passenger.getPassengerId());
        pkt.setInt1(lostBags);

        clientCom.writeObject(pkt);
        pkt = (Message) clientCom.readObject();
        clientCom.close();
    }

    /**
     * Close the stub connection
     */
	public void closeStub() {
        ClientCom clientCom = new ClientCom(RunParameters.BaggageReclaimHostName, RunParameters.BaggageReclaimPort);
		clientCom.open();
        Message pkt = new Message();
        pkt.setType(MessageType.SIM_ENDED);
        clientCom.writeObject(pkt);
        clientCom.readObject();
        clientCom.close();
	}
}