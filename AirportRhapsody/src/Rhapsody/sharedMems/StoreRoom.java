package Rhapsody.sharedMems;

import java.util.ArrayList;
import java.util.List;

import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Luggage;

/**
 * Storeroom shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class StoreRoom {

    /**
     * GeneralRepository for debugging purposes
     */
    private GeneralRepository generalRepository;

    /**
     * Luggage currently present in the storeroom
     */
    private List<Luggage> bagsInStoreRoom;
    

    /**
     * Contructor method for the StoreRoom 
     * @param generalRepository
     */
    public StoreRoom(GeneralRepository generalRepository) {
        this.generalRepository=generalRepository;
        this.bagsInStoreRoom=new ArrayList<>();
    }

    /**
     * Method to deposit a bag in storeroom. <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR CONVEYOR BELT<b/>  
     * @param luggage
     */
    public synchronized void carryItToAppropriateStore(Luggage luggage) {
        Porter porter = (Porter) Thread.currentThread();
        porter.setPorterState(PorterState.AT_THE_STOREROOM);
        this.generalRepository.updatePorterState(porter.getPorterState(), true);
        try {
            this.bagsInStoreRoom.add(luggage);
            this.generalRepository.updateStoreRoomBags(this.bagsInStoreRoom.size(), false);
        } catch (NullPointerException e) {
            System.err.print("[StoreRoom] Porter has no bag, reseting porter");
            porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentLuggage(null);
            this.generalRepository.updatePorterState(porter.getPorterState(), true);
            // resetting luggage on storeroom
            this.bagsInStoreRoom=new ArrayList<>();
            this.generalRepository.updateStoreRoomBags(0, false);
        }
    }
    
}