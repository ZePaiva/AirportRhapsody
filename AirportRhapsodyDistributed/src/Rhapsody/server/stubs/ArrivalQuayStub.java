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
     * Prettify
     */
    public static final String ANSI_BLUE = "\u001B[0m\u001B[34m";

    /**
     * Arrival quay stub constructor
     */
    public ArrivalQuayStub(){
		
    }

	/**
	 * Method to signal Porter that the simulation has ended
	 */
	public void endOfWork() {
				
		ClientCom clientCom = new ClientCom(RunParameters.ArrivalQuayHostName, RunParameters.ArrivalQuayPort);
		clientCom.open();

		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
	}	

	/**
	 * Close stub
	 */
	public void closeStub() {
		
	}
}