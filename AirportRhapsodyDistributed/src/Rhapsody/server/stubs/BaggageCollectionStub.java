package Rhapsody.server.stubs;

import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ClientCom;

/**
 * Luggage collection stub  for the servers
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class BaggageCollectionStub {
    
    /**
     * Prettify
     */
    public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    /**
     * Baggage collection stub constructor
     */
    public BaggageCollectionStub(){
    }

    /**
     * Mehtod to reset baggage collection variable by the porter thread
     */
    public void newFlight(){
        ClientCom clientCom = new ClientCom(RunParameters.BaggageCollectionHostName, RunParameters.BaggageCollectionPort);
		clientCom.open();        
        Message pkt = new Message();
		pkt.setType(MessageType.NEW_FLIGHT);
		
		clientCom.writeObject(pkt);
        pkt = (Message) clientCom.readObject();  
        clientCom.close();
    }

    /**
     * Close the stub
     */
	public void closeStub() {
	}
}