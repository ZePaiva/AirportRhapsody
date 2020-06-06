package Rhapsody.server.stubs;

import Rhapsody.common.Message;
import Rhapsody.common.MessageType;
import Rhapsody.common.RunParameters;
import Rhapsody.common.States;
import Rhapsody.server.communications.ClientCom;

/**
 * General repository stub 
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class GeneralRepositoryStub {

    /**
	 * Client communication channelt
	 */
	private final ClientCom clientCom;

    /**
     *  Stub constructor
     */
    public GeneralRepositoryStub() {
        clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
    }

	public void clearFlight(boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_FLIGHT_CLEAR);
		pkt.setBool1(b);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateFlight(int currentFlight, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_FLIGHT);
		pkt.setBool1(b);
		pkt.setInt1(currentFlight);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateBagsInPlane(int size, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_PLANE);
		pkt.setBool1(b);
		pkt.setInt1(size);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updatePorterState(States entityState, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PORTER_STATE);
		pkt.setBool1(b);
		pkt.setState(entityState);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updatePassengerState(States entityState, int entityID, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_STATE);
		pkt.setBool1(b);
		pkt.setState(entityState);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void addPassengerToFlight(int entityID, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_IN_FLIGHT);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updatePlaneHoldBags(int startingBags, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_PH);
		pkt.setBool1(b);
		pkt.setInt1(startingBags);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateCurrentBags(int entityID, int i, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_CB);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		pkt.setInt2(i);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateSituation(int entityID, String situation, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_SIT);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		pkt.setBool1(situation.equals("FDT"));
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateStartingBags(int entityID, int startingBags, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_SB);
		pkt.setBool1(b);
		pkt.setId(entityID);
		pkt.setInt2(startingBags);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void addToWaitingQueue(int entityID, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_IN_WAIT);
		pkt.setBool1(b);
		pkt.setId(entityID);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void removeFromWaitingQueue(boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_OUT_WAIT);
		pkt.setBool1(b);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void addToBusSeat(int entityID, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_IN_BUS);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateBusDriverState(States entityState, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BD_STATE);
		pkt.setBool1(b);
		pkt.setState(entityState);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateConveyorBags(int size, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_CB);
		pkt.setBool1(b);
		pkt.setInt1(size);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateStoreRoomBags(int i, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_SR);
		pkt.setBool1(b);
		pkt.setInt1(i);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateLostbags(int lostBags, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_L);
		pkt.setBool1(b);
		pkt.setId(lostBags);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void updateTRTPassengers(int i, boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_TRT);
		pkt.setBool1(b);
		pkt.setInt1(i);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}

	public void removeFromBusSeat(boolean b) {
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_OUT_BUS);
		pkt.setBool1(b);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
	}
}