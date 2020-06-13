package Rhapsody.client.stubs;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.Porter;
import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Storage area stub 
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class StorageAreaStub {
    
    /**
     * Stub constructor
     */
    public StorageAreaStub() {
    }

    /**
     * Method to deposit a bag in storeroom. <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR CONVEYOR BELT</b>  
     * @param luggage
     */
    public void carryItToAppropriateStore(Luggage luggage) {
		ClientCom clientCom = new ClientCom(RunParameters.StorageAreaHostName, RunParameters.StorageAreaPort);
        while (!clientCom.open()) {
			System.out.println("Storeage Area not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
        Porter porter = (Porter) Thread.currentThread();
        Message pkt = new Message();
        pkt.setType(MessageType.PORTER_STORE_BAG_SR);
        pkt.setInt1(luggage.getPassengerId());
        pkt.setBool1(luggage.getLuggageType().equals("FDT"));

        clientCom.writeObject(pkt);
        pkt = (Message) clientCom.readObject();
        clientCom.close();
        porter.setPorterState(pkt.getState());
    }

    /**
     * Close the stub
     */
	public void closeStub() {
        ClientCom clientCom = new ClientCom(RunParameters.StorageAreaHostName, RunParameters.StorageAreaPort);
        while (!clientCom.open()) {
			System.out.println("Storeage Area not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
        Message pkt = new Message();
        pkt.setType(MessageType.SIM_ENDED);
        clientCom.writeObject(pkt);
        clientCom.readObject();
        clientCom.close();
	}
}