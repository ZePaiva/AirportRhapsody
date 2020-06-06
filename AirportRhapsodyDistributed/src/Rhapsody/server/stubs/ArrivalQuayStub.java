package Rhapsody.server.stubs;

import java.util.Queue;

import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ClientCom;

/**
 * Arrival Terminal Transfer Quay Stub for the server
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalQuayStub {
    
     /**
	 * Client communication channelt
	 */
	private ClientCom clientCom;

    /**
     * Prettify
     */
    public static final String ANSI_BLUE = "\u001B[0m\u001B[34m";

    /**
     * Arrival quay stub constructor
     */
    public ArrivalQuayStub(){
		clientCom=null;
    }

	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public void endOfWork() {
				
		if (clientCom == null) {
			this.clientCom = new ClientCom(RunParameters.ArrivalQuayHostName, RunParameters.ArrivalQuayPort);
			this.clientCom.open();	
		}

		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
	}	

	/**
	 * Close stub
	 */
	public void closeStub() {
		clientCom.close();
	}
}