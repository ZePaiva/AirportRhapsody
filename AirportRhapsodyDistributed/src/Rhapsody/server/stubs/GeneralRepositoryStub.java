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
     *  Stub constructor
     */
    public GeneralRepositoryStub() {

    }

	/**
	 * Clear flight log method
	 * @param b noLog
	 */
	public void clearFlight(boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_FLIGHT_CLEAR);
		pkt.setBool1(b);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Update flight with new id
	 * 
	 * @param currentFlight
	 * @param b
	 */
	public void updateFlight(int currentFlight, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_FLIGHT);
		pkt.setBool1(b);
		pkt.setInt1(currentFlight);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Update bags in the plane hold
	 * @param size
	 * @param b
	 */
	public void updateBagsInPlane(int size, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_PLANE);
		pkt.setBool1(b);
		pkt.setInt1(size);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Update porter state
	 * @param entityState
	 * @param b
	 */
	public void updatePorterState(States entityState, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PORTER_STATE);
		pkt.setBool1(b);
		pkt.setState(entityState);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update passenger # state
	 * @param entityState
	 * @param entityID
	 * @param b
	 */
	public void updatePassengerState(States entityState, int entityID, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_STATE);
		pkt.setBool1(b);
		pkt.setState(entityState);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * disembark passenger
	 * @param entityID
	 * @param b
	 */
	public void addPassengerToFlight(int entityID, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_IN_FLIGHT);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update bags supposed to be transported
	 * @param startingBags
	 * @param b
	 */
	public void updatePlaneHoldBags(int startingBags, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_PH);
		pkt.setBool1(b);
		pkt.setInt1(startingBags);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update passenger # current bags
	 * @param entityID
	 * @param i
	 * @param b
	 */
	public void updateCurrentBags(int entityID, int i, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_CB);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		pkt.setInt2(i);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update passenger # situation
	 * @param entityID
	 * @param situation
	 * @param b
	 */
	public void updateSituation(int entityID, String situation, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_SIT);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		pkt.setBool1(situation.equals("FDT"));
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Update passenger # starting bags
	 * @param entityID
	 * @param startingBags
	 * @param b
	 */
	public void updateStartingBags(int entityID, int startingBags, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_SB);
		pkt.setBool1(b);
		pkt.setId(entityID);
		pkt.setInt2(startingBags);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * passenger # arrives to arrival terminal tranfer quay 
	 * @param entityID
	 * @param b
	 */
	public void addToWaitingQueue(int entityID, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_IN_WAIT);
		pkt.setBool1(b);
		pkt.setId(entityID);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * passenger # goes inside bus
	 * @param b
	 */
	public void removeFromWaitingQueue(boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_OUT_WAIT);
		pkt.setBool1(b);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * passenger # seats on bus
	 * @param entityID
	 * @param b
	 */
	public void addToBusSeat(int entityID, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_IN_BUS);
		pkt.setBool1(b);
		pkt.setInt1(entityID);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update bus driver state
	 * @param entityState
	 * @param b
	 */
	public void updateBusDriverState(States entityState, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BD_STATE);
		pkt.setBool1(b);
		pkt.setState(entityState);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update bags in conveyors belt
	 * @param size
	 * @param b
	 */
	public void updateConveyorBags(int size, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_CB);
		pkt.setBool1(b);
		pkt.setInt1(size);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update bags in storage area
	 * @param i
	 * @param b
	 */
	public void updateStoreRoomBags(int i, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_SR);
		pkt.setBool1(b);
		pkt.setInt1(i);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update bags reported as lost
	 * 
	 * @param lostBags
	 * @param b
	 */
	public void updateLostbags(int lostBags, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_BAG_L);
		pkt.setBool1(b);
		pkt.setId(lostBags);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * update passengers passed as TRT type
	 * @param i
	 * @param b
	 */
	public void updateTRTPassengers(int i, boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_TRT);
		pkt.setBool1(b);
		pkt.setInt1(i);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * passenger # gets out of bus
	 * @param b
	 */
	public void removeFromBusSeat(boolean b) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.UP_PASS_OUT_BUS);
		pkt.setBool1(b);
		clientCom.writeObject(pkt);
		pkt=(Message) clientCom.readObject();
		clientCom.close();
	}

	/**
	 * Shared memory registering in logger
	 * @param i
	 */
	public void registerMem(int i) {
		ClientCom clientCom = new ClientCom(RunParameters.RepositoryHostName, RunParameters.RepositoryPort);
		clientCom.open();
		Message pkt = new Message();
		pkt.setType(MessageType.LOG_MEM);
		pkt.setInt1(i);
		System.out.println("Registering shared memory in the repository");
		clientCom.writeObject(pkt);
		pkt = (Message) clientCom.readObject();
		clientCom.close();
	}
}