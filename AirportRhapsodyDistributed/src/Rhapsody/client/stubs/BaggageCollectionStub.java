package Rhapsody.client.stubs;

import Rhapsody.client.communications.ClientCom;
import Rhapsody.client.entities.Passenger;
import Rhapsody.client.entities.Porter;
import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;

/**
 * Luggage collection stub 
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
     * Method to deposit a bag in the conveyor belt <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM</b>  
     * @param luggage
     */
    public void carryItToAppropriateStore(Luggage luggage) {
		ClientCom clientCom = new ClientCom(RunParameters.BaggageCollectionHostName, RunParameters.BaggageCollectionPort);
		while (!clientCom.open()) {
			System.out.println("Baggage Collection Point not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		Porter porter = (Porter) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.PORTER_STORE_BAG_CB);
		pkt.setInt1(luggage.getPassengerId());
        pkt.setBool1(luggage.getLuggageType().equals("FDT"));
        pkt.setState(porter.getPorterState());
		
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
        clientCom.close();
		porter.setPorterState(pkt.getState());   
    }

    /**
     * Method to signal all passengers that luggage collection has ended 
     */
    public void noMoreBagsToCollect() {
        ClientCom clientCom = new ClientCom(RunParameters.BaggageCollectionHostName, RunParameters.BaggageCollectionPort);
		while (!clientCom.open()) {
			System.out.println("Baggage Collection Point not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		Porter porter = (Porter) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.PORTER_NO_MORE_BAGS);
        
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
        clientCom.close();
		porter.setPorterState(pkt.getState());   
    }

    /**
     * Method for a passenger to try to collect the bags
     * @param startingBags
     * @return currentBags
     */
    public int goCollectABag(int startingBags){
        ClientCom clientCom = new ClientCom(RunParameters.BaggageCollectionHostName, RunParameters.BaggageCollectionPort);
		while (!clientCom.open()) {
			System.out.println("Baggage Collection Point not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		Passenger passenger = (Passenger) Thread.currentThread();
		Message pkt = new Message();
		pkt.setType(MessageType.PASSENGER_COLLECTING_BAG);
        pkt.setId(passenger.getPassengerId());
        pkt.setState(passenger.getCurrentState());
        pkt.setInt1(startingBags);

		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
        clientCom.close();
        passenger.setCurrentState(pkt.getState());
        return pkt.getInt1();
    }

    /**
     * Mehtod to reset baggage collection variable by the porter thread
     */
    public void newFlight(){
        ClientCom clientCom = new ClientCom(RunParameters.BaggageCollectionHostName, RunParameters.BaggageCollectionPort);
		while (!clientCom.open()) {
			System.out.println("Baggage Collection Point not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
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
        ClientCom clientCom = new ClientCom(RunParameters.BaggageCollectionHostName, RunParameters.BaggageCollectionPort);
		while (!clientCom.open()) {
			System.out.println("Baggage Collection Point not active yet, sleeping for 1 seccond");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		Message pkt = new Message();
		pkt.setType(MessageType.SIM_ENDED);
		
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();   
        clientCom.close();
	}
}