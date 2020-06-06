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
	 * Client communication channelt
	 */
    private final ClientCom clientCom;
    
    /**
     * Stub constructor
     */
    public StorageAreaStub() {
        clientCom = new ClientCom(RunParameters.StorageAreaHostName, RunParameters.StorageAreaPort);
		clientCom.open();
    }

    /**
     * Method to deposit a bag in storeroom. <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR CONVEYOR BELT</b>  
     * @param luggage
     */
    public void carryItToAppropriateStore(Luggage luggage) {
        Porter porter = (Porter) Thread.currentThread();
        Message pkt = new Message();
        pkt.setType(MessageType.PORTER_STORE_BAG_SR);
        pkt.setInt1(luggage.getPassengerId());
        pkt.setBool1(luggage.getLuggageType().equals("FDT"));

        clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		
		porter.setPorterState(pkt.getState());
    }

    /**
     * Close the stub
     */
	public void closeStub() {
        Message pkt = new Message();
        pkt.setType(MessageType.SIM_ENDED);
        clientCom.writeObject(pkt);
        clientCom.close();
	}
}