package Rhapsody.server.stubs;

import java.util.Arrays;

import Rhapsody.server.communications.ClientCom;
import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Arrival lounge stub, implements an interface for the servers
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class ArrivalLoungeStub {
    
    /**
	 * Client communication channelt
	 */
	private ClientCom clientCom;

    /**
     * Prettify
     */
	public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    public ArrivalLoungeStub() {
        clientCom=null;
	}
    
	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public void endOfWork() {
		
		if (clientCom == null) {
			this.clientCom = new ClientCom(RunParameters.ArrivalLoungeHostName, RunParameters.ArrivalLoungePort);
			this.clientCom.open();		
		}

		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);

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