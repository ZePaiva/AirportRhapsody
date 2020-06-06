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
	 * Client communication channelt
	 */
	private final ClientCom clientCom;

    /**
     * Prettify
     */
    public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    /**
     * Baggage collection stub constructor
     */
    public BaggageCollectionStub(){
        clientCom = new ClientCom(RunParameters.BaggageCollectionHostName, RunParameters.BaggageCollectionPort);
		clientCom.open();
    }

    /**
     * Mehtod to reset baggage collection variable by the porter thread
     */
    public void newFlight(){
        Message pkt = new Message();
		pkt.setType(MessageType.NEW_FLIGHT);
		
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();   
    }

    /**
     * Close the stub
     */
	public void closeStub() {
        clientCom.close();
	}
}