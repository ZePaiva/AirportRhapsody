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
        porter.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
        this.logger.updatePorterState(porter.getPorterState(), true);
        // warns that it has not picked all bags
        this.collectedAllBags=false;
        try {
            System.out.printf("Collecting luggage\n");
            // adds luggage to conveyor belt and logs it
            this.bagsInConveyorBelt.add(porter.getCurrentLuggage());
            porter.setCurrentLuggage(null);
            this.logger.updateConveyorBags(this.bagsInConveyorBelt.size(), false);
        } catch (NullPointerException e) {
            System.err.print("[StoreRoom] Porter has no bag, reseting porter");
            // resetting porter
            porter.planeHasBags(false);
            porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentLuggage(null);
            this.logger.updatePorterState(porter.getPorterState(), true);
            // resetting luggage on storeroom
            this.bagsInConveyorBelt=new ArrayList<>();
            this.logger.updateStoreRoomBags(0, false);
        }
    }

    /**
     * Method to call all passengers waiting for luggage 
     */
    public synchronized void noMoreBagsToCollect() {
        Porter porter = (Porter) Thread.currentThread();
        porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
        this.collectedAllBags=true;
        this.logger.updatePorterState(porter.getPorterState(), false);
        System.out.println("All Luggage collected");
    }

    /**
     * Method for a passenger to try to collect a bag
     */
    public synchronized void goCollectABag(){
        Passenger passenger = (Passenger) Thread.currentThread();
        
        // updates passenger state if needed
        System.out.printf("callbags %s\n", this.collectedAllBags);
        if (passenger.getCurrentState()!=PassengerState.AT_LUGGAGE_COLLECTION) {
            passenger.setCurrentState(PassengerState.AT_LUGGAGE_COLLECTION);
            this.logger.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), false);
        }

        // if there are bags try to find one of the current passenger and removes it
        Luggage bag = this.bagsInConveyorBelt.stream()
                .filter(p -> p.getPassengerId()==passenger.getPassengerId())
                .findFirst()
                .map(p -> {
                    this.bagsInConveyorBelt.remove(p);
                    return p;
                })
                .orElse(null);

        // if stack is empty && collection has finished 
        if (this.bagsInConveyorBelt.isEmpty() && this.collectedAllBags) {
            if (bag == null){
                System.out.printf("ERROR2: P%d | L: %s\n", passenger.getPassengerId(), bag==null ? "null" : bag.toString());
                passenger.lostBags(true);
                return;
            }
        }
        // if stack is NOT empty && collection has finished
        else if (!this.bagsInConveyorBelt.isEmpty() && this.collectedAllBags) {
            if (bag == null){
                System.out.printf("ERROR2: P%d | L: %s\n", passenger.getPassengerId(), bag==null ? "null" : bag.toString());
                passenger.lostBags(true);
                return;
            }
        }

        System.out.printf("NO ERROR: P%d | L: %s | StSz %d \n", passenger.getPassengerId(), bag==null ? "null" : bag.toString(), this.bagsInConveyorBelt.size() );
        if (bag != null) {
            passenger.setCurrentBags(passenger.getCurrentBags()+1);
            // log updates
            this.logger.updateConveyorBags(this.bagsInConveyorBelt.size(), true);
            this.logger.updateCurrentBags(passenger.getPassengerId(), passenger.getCurrentBags(), false);
        } else {
            return;
        }
    }
}