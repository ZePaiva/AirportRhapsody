package Rhapsody.server.sharedRegions;

import java.util.ArrayList;
import java.util.List;

import Rhapsody.common.Luggage;
import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PorterInterface;
import Rhapsody.server.stubs.GeneralRepositoryStub;

public class StorageArea {
    
    /**
     * GeneralRepository for debugging purposes
     */
    private GeneralRepositoryStub generalRepository;

    /**
     * Luggage currently present in the storeroom
     */
    private List<Luggage> bagsInStoreRoom;
    

    /**
     * Contructor method for the StoreRoom 
     * @param generalRepository
     */
    public StorageArea(GeneralRepositoryStub generalRepository) {
        this.generalRepository=generalRepository;
        this.bagsInStoreRoom=new ArrayList<>();
    }

    /**
     * Method to deposit a bag in storeroom. <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR CONVEYOR BELT</b>  
     * @param luggage
     */
    public synchronized void carryItToAppropriateStore(Luggage luggage) {
        PorterInterface porter = (TunnelProvider) Thread.currentThread();
        porter.setEntityState(States.AT_THE_STOREROOM);
        this.generalRepository.updatePorterState(porter.getEntityState(), true);
        try {
            this.bagsInStoreRoom.add(luggage);
            this.generalRepository.updateStoreRoomBags(this.bagsInStoreRoom.size(), false);
        } catch (NullPointerException e) {
            System.err.print("[StoreRoom] Porter has no bag, reseting porter");
            porter.setEntityState(States.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentBag(null);
            this.generalRepository.updatePorterState(porter.getEntityState(), true);
            // resetting luggage on storeroom
            this.bagsInStoreRoom=new ArrayList<>();
            this.generalRepository.updateStoreRoomBags(0, false);
        }
    }
}