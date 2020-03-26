package Rhapsody.sharedMems;

import java.util.ArrayList;
import java.util.List;

import Rhapsody.entities.Passenger;
import Rhapsody.entities.Porter;
import Rhapsody.entities.states.PassengerState;
import Rhapsody.entities.states.PorterState;
import Rhapsody.utils.Luggage;

/**
 * Conveyor Belt shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BaggageCollectionPoint{

    /**
     * GeneralRepository for debugging purposes
     */
    private GeneralRepository generalRepository;

    /**
     * Amount of bags in the conveyor belt
     */
    private List<Luggage> bagsInConveyorBelt;

    /**
     * Signals if Plane Hold contains any more bags
     */
    private boolean collectedAllBags;

	public static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Constructor of Baggage collection point
     * @param generalRepository
     */
    public BaggageCollectionPoint(GeneralRepository generalRepository) {
        this.generalRepository = generalRepository;
        this.bagsInConveyorBelt = new ArrayList<>();
        this.collectedAllBags = false;
    }

    /**
     * Method to deposit a bag in the conveyor belt <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM<b/>  
     * @param luggage
     */
    public synchronized void carryItToAppropriateStore(Luggage luggage) {
        Porter porter = (Porter) Thread.currentThread();
        porter.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);
        this.generalRepository.updatePorterState(porter.getPorterState(), true);

        try {
            // adds luggage to conveyor belt, notifies the passenger and logs it
            this.bagsInConveyorBelt.add(luggage);
            System.out.printf(ANSI_WHITE+"[BAGCOLLPT] Porter stored bag in BCP\n");
            this.collectedAllBags=false;
            notifyAll();
            this.generalRepository.updateConveyorBags(this.bagsInConveyorBelt.size(), false);
       
        } catch (NullPointerException e) {
            System.err.println(ANSI_WHITE+"[BAGCOLLPT] Porter has no bag, reseting porter");
            // resetting porter
            porter.planeHasBags(false);
            porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentLuggage(null);
            this.generalRepository.updatePorterState(porter.getPorterState(), true);
            // resetting luggage on storeroom
            this.bagsInConveyorBelt=new ArrayList<>();
            this.generalRepository.updateStoreRoomBags(0, false);
        }
    }

    /**
     * Method to signal all passengers that luggage collection has ended 
     */
    public synchronized void noMoreBagsToCollect() {
        Porter porter = (Porter) Thread.currentThread();
        porter.setPorterState(PorterState.WAITING_FOR_PLANE_TO_LAND);
        this.collectedAllBags=true;
        notifyAll();
        this.generalRepository.updatePorterState(porter.getPorterState(), false);
    }

    /**
     * Method for a passenger to try to collect a bag
     */
    public synchronized void goCollectABag(int flight){
        Passenger passenger = (Passenger) Thread.currentThread();
        
        // updates passenger state if needed
        if (passenger.getCurrentState()!=PassengerState.AT_LUGGAGE_COLLECTION) {
            passenger.setCurrentState(PassengerState.AT_LUGGAGE_COLLECTION);
            this.generalRepository.updatePassengerState(passenger.getCurrentState(), passenger.getPassengerId(), false);
        }

        // if there are still bags or the porter has not collected them all
        while (!this.collectedAllBags || !this.bagsInConveyorBelt.isEmpty()) {
            try {
                System.out.printf(ANSI_WHITE+"[BAGCOLLPT] P%d will try to get his bags | CB %d\n", passenger.getPassengerId(), passenger.getCurrentBags());
                if (!this.collectedAllBags) {
                    System.out.printf(ANSI_WHITE+"[BAGCOLLPT] P%d will sleep | CB %d\n", passenger.getPassengerId(), passenger.getCurrentBags());
                    wait();
                }
                System.out.printf(ANSI_WHITE+"[BAGCOLLPT] P%d awakened | CB %d\n", passenger.getPassengerId(), passenger.getCurrentBags());
                // if there are bags try to find one of the current passenger and removes it
                this.bagsInConveyorBelt.stream().forEach(System.out::println);
                Luggage bag = this.bagsInConveyorBelt.stream()
                    .filter(p -> p.getPassengerId()==passenger.getPassengerId())
                    .findFirst()
                    .map(p -> {
                        this.bagsInConveyorBelt.remove(p);
                        return p;
                    })
                    .orElse(null);
                this.bagsInConveyorBelt.stream().forEach(System.out::println);
                if (bag != null) {
                    passenger.setCurrentBags(passenger.getCurrentBags()+1);
                    System.out.printf(ANSI_WHITE+"[BAGCOLLPT] Passenger %d has one more bag | CB: %d\n", passenger.getPassengerId(), passenger.getCurrentBags());
                    // log updates
                    this.generalRepository.updateConveyorBags(this.bagsInConveyorBelt.size(), true);
                    this.generalRepository.updateCurrentBags(passenger.getPassengerId(), passenger.getCurrentBags(), false);
                } else if (bag==null && this.bagsInConveyorBelt.isEmpty() && this.collectedAllBags) {
                    System.out.printf(ANSI_WHITE+"[BAGCOLLPT] Passenger %d found no bag & collection has ended & CB is empty \n", passenger.getPassengerId());
                    passenger.lostBags(true);
                    return;
                
                } else if (bag==null && this.collectedAllBags) {
                    System.out.printf(ANSI_WHITE+"[BAGCOLLPT] Passenger %d found no bag & collection has ended \n", passenger.getPassengerId());
                    passenger.lostBags(true);
                    return;
                }
                if ( passenger.getStartingBags()[flight]==passenger.getCurrentBags() ) {
                    return;
                }
            } catch (InterruptedException e) {}
        }
        // if there are no more bags and porter collected the mall
        passenger.lostBags(true);
    }

    /**
     * Mehtod to reset baggage collection variable by the porter thread
     */
    public synchronized void newFlight(){
        this.collectedAllBags=false;
    }
}