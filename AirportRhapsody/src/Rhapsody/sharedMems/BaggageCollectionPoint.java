package Rhapsody.sharedMems;

import java.util.ArrayList;
import java.util.List;

import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Logger;
import Rhapsody.utils.Luggage;

/**
 * Conveyor Belt shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BaggageCollectionPoint{

    /**
     * Logger for debugging purposes
     */
    private Logger logger;

    /**
     * Amount of bags in the conveyor belt
     */
    private List<Luggage> bagsInConveyorBelt;

    /**
     * Constructor of Baggage collection point
     * @param logger
     */
    public BaggageCollectionPoint(Logger logger) {
        this.logger = logger;
        bagsInConveyorBelt = new ArrayList<>();
    }

    /**
     * Method to deposit a bag in the conveyor belt <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM<b/>  
     */
    public synchronized void carryItToAppropriateStore() {
        Porter porter = (Porter) Thread.currentThread();
        try {
            this.bagsInConveyorBelt.add(porter.getCurrentLuggage());
            porter.setCurrentLuggage(null);
            this.logger.updateStoreRoomBags(this.bagsInConveyorBelt.size());
        } catch (NullPointerException e) {
            System.err.print("[StoreRoom] Porter has no bag, reseting porter");
            // resetting porter
            porter.planeHasBags(false);
            porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentLuggage(null);
            this.logger.updatePorterState(porter.getPorterState());
            // resetting luggage on storeroom
            this.bagsInConveyorBelt=new ArrayList<>();
            this.logger.updateStoreRoomBags(0);
        }
    }



	
}