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
     * Signals if Plane Hold contains any more bags
     */
    private boolean collectedAllBags;

    /**
     * Constructor of Baggage collection point
     * @param logger
     */
    public BaggageCollectionPoint(Logger logger) {
        this.logger = logger;
        this.bagsInConveyorBelt = new ArrayList<>();
        this.collectedAllBags = false;
    }

    /**
     * Method to deposit a bag in the conveyor belt <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM<b/>  
     */
    public synchronized void carryItToAppropriateStore() {
        Porter porter = (Porter) Thread.currentThread();
        this.collectedAllBags=false;
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

    /**
     * Method to call all passengers waiting for luggage 
     */
    public synchronized void noMoreBagsToCollect() {
        Porter porter = (Porter) Thread.currentThread();
        porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
        this.collectedAllBags=true;
        this.logger.updatePorterState(porter.getPorterState());
        notifyAll();
    }

    /**
     * Method for a passenger to try to collect a bag
     */
    public synchronized void goCollectABag(){
        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setCurrentState(PassengerState.AT_LUGGAGE_COLLECTION);

        // wait until all luggage has been collected
        while(!this.collectedAllBags){
            try{
                wait();
            } catch (InterruptedException e) {
                System.err.printf("[BaggageCollectionPoint] Passenger %d interrupted for some reason", passenger.getPassengerId());
                System.exit(3);
            }
        }

        // if there ar no more bags in the conveyor belt
        if (this.bagsInConveyorBelt.isEmpty()) {
            passenger.lostBags(true);
        } else {
            // if there are bags try to find one of the current passenger and removes it
            Luggage bag = this.bagsInConveyorBelt.stream()
                            .filter(p -> p.getPassengerId()==passenger.getPassengerId())
                            .findFirst()
                            .map(p -> {
                                this.bagsInConveyorBelt.remove(p);
                                return p;
                            })
                            .orElse(null);
            if (bag.equals(null)) {
                passenger.lostBags(true);
            } else {
                passenger.setCurrentBags(passenger.getCurrentBags()+1);
            }
        }

        // log updates
        this.logger.updateConveyorBags(this.bagsInConveyorBelt.size());
        this.logger.updateCurrentBags(passenger.getPassengerId(), passenger.getCurrentBags());
    }
	
}