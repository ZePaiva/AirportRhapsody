package Rhapsody.sharedMems;

import java.util.ArrayList;
import java.util.List;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Logger;
import Rhapsody.utils.Luggage;

/**
 * Storeroom shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class StoreRoom {

    /**
     * Logger for debugging purposes
     */
    private Logger logger;

    /**
     * Luggage currently present in the storeroom
     */
    private List<Luggage> bagsInStoreRoom;
    

    /**
     * Contructor method for the StoreRoom 
     * @param logger
     */
    public StoreRoom(Logger logger) {
        this.logger=logger;
        this.bagsInStoreRoom=new ArrayList<>();
    }

    /**
     * Method to deposit a bag in storeroom. <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR CONVEYOR BELT<b/>  
     */
    public synchronized void carryItToAppropriateStore() {
        Porter porter = (Porter) Thread.currentThread();
        try {
            this.bagsInStoreRoom.add(porter.getCurrentLuggage());
            porter.setCurrentLuggage(null);
            this.logger.updateStoreRoomBags(this.bagsInStoreRoom.size());
        } catch (NullPointerException e) {
            System.err.print("[StoreRoom] Porter has no bag, reseting porter");
            // resetting porter
            porter.planeHasBags(false);
            porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentLuggage(null);
            this.logger.updatePorterState(porter.getPorterState());
            // resetting luggage on storeroom
            this.bagsInStoreRoom=new ArrayList<>();
            this.logger.updateStoreRoomBags(0);
        }
    }
    
}