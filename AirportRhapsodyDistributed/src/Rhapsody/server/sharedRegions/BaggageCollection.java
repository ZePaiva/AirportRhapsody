package Rhapsody.server.sharedRegions;

import java.util.ArrayList;
import java.util.List;

import Rhapsody.common.Luggage;
import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.interfaces.PorterInterface;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Luggage collection point entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class BaggageCollection {
    /**
     * GeneralRepository for debugging purposes
     */
    private GeneralRepositoryStub generalRepository;

    /**
     * Amount of bags in the conveyor belt
     */
    private List<Luggage> bagsInConveyorBelt;

    /**
     * Signals if Plane Hold contains any more bags
     */
    private boolean collectedAllBags;

    public static final String ANSI_WHITE = "\u001B[0m\u001B[37m";

    /**
     * Constructor of Baggage collection point
     * 
     * @param generalRepository
     */
    public BaggageCollection(GeneralRepositoryStub generalRepository) {
        this.generalRepository = generalRepository;
        this.bagsInConveyorBelt = new ArrayList<>();
        this.collectedAllBags = false;
        this.generalRepository.registerMem(3);
    }

    /**
     * Method to deposit a bag in the conveyor belt
     * <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM</b>
     * 
     * @param luggage
     */
    public synchronized void carryItToAppropriateStore(Luggage luggage) {
        PorterInterface porter = (TunnelProvider) Thread.currentThread();
        porter.setEntityState(States.AT_THE_LUGGAGE_BELT_CONVEYOR);
        this.generalRepository.updatePorterState(porter.getEntityState(), true);

        try {
            // adds luggage to conveyor belt, notifies the passenger and logs it
            this.bagsInConveyorBelt.add(luggage);
            System.out.printf(ANSI_WHITE + "[BAGCOLLPT] Porter stored bag in BCP\n");
            this.collectedAllBags = false;
            notifyAll();
            this.generalRepository.updateConveyorBags(this.bagsInConveyorBelt.size(), false);

        } catch (NullPointerException e) {
            System.err.println(ANSI_WHITE + "[BAGCOLLPT] Porter has no bag, reseting porter");
            porter.setEntityState(States.WAITING_FOR_PLANE_TO_LAND);
            porter.setCurrentBag(null);
            this.generalRepository.updatePorterState(porter.getEntityState(), true);
            // resetting luggage on storeroom
            this.bagsInConveyorBelt = new ArrayList<>();
            this.generalRepository.updateStoreRoomBags(0, false);
        }
    }

    /**
     * Method to signal all passengers that luggage collection has ended
     */
    public synchronized void noMoreBagsToCollect() {
        PorterInterface porter = (TunnelProvider) Thread.currentThread();
        porter.setEntityState(States.WAITING_FOR_PLANE_TO_LAND);
        this.collectedAllBags = true;
        notifyAll();
        this.generalRepository.updatePorterState(porter.getEntityState(), false);
    }

    /**
     * Method for a passenger to try to collect the bags
     * 
     * @param startingBags
     * @return currentBags
     */
    public synchronized int goCollectABag(int startingBags) {
        PassengerInterface passenger = (TunnelProvider) Thread.currentThread();

        // updates passenger state if needed
        if (passenger.getEntityState() != States.AT_LUGGAGE_COLLECTION) {
            passenger.setEntityState(States.AT_LUGGAGE_COLLECTION);
            this.generalRepository.updatePassengerState(passenger.getEntityState(), passenger.getEntityID(), false);
        }

        int bagsCollected = 0;

        // if there are still bags or the porter has not collected them all
        while (!this.collectedAllBags || !this.bagsInConveyorBelt.isEmpty()) {
            try {
                System.out.printf(ANSI_WHITE + "[PASSENGER] P%d will try to get his bags | CB %d\n",
                        passenger.getEntityID(), bagsCollected);
                if (!this.collectedAllBags) {
                    System.out.printf(ANSI_WHITE + "[PASSENGER] P%d will sleep | CB %d\n", passenger.getEntityID(),
                            bagsCollected);
                    wait();
                }
                System.out.printf(ANSI_WHITE + "[PASSENGER] P%d awakened | CB %d\n", passenger.getEntityID(),
                        bagsCollected);
                // if there are bags try to find one of the current passenger and removes it
                // this.bagsInConveyorBelt.stream().forEach(System.out::println);
                Luggage bag = this.bagsInConveyorBelt.stream()
                        .filter(p -> p.getPassengerId() == passenger.getEntityID()).findFirst().map(p -> {
                            this.bagsInConveyorBelt.remove(p);
                            return p;
                        }).orElse(null);
                // this.bagsInConveyorBelt.stream().forEach(System.out::println);
                if (bag != null) {
                    // passenger.setCurrentBags(passenger.getCurrentBags()+1);
                    bagsCollected++;
                    System.out.printf(ANSI_WHITE + "[PASSENGER] Passenger %d has one more bag | CB: %d\n",
                            passenger.getEntityID(), bagsCollected);
                    // log updates
                    this.generalRepository.updateCurrentBags(passenger.getEntityID(), bagsCollected, false);
                    this.generalRepository.updateConveyorBags(this.bagsInConveyorBelt.size(), true);
                } else if (bag == null && this.bagsInConveyorBelt.isEmpty() && this.collectedAllBags) {
                    System.out.printf(
                            ANSI_WHITE
                                    + "[PASSENGER] Passenger %d found no bag & collection has ended & CB is empty \n",
                            passenger.getEntityID());
                    // passenger.lostBags(true);
                    return bagsCollected;

                } else if (bag == null && this.collectedAllBags) {
                    System.out.printf(ANSI_WHITE + "[PASSENGER] Passenger %d found no bag & collection has ended \n",
                            passenger.getEntityID());
                    // passenger.lostBags(true);
                    return bagsCollected;
                }
                if (startingBags == bagsCollected) {
                    return bagsCollected;
                }
            } catch (InterruptedException e) {
            }
        }
        // if there are no more bags and porter collected the mall
        // passenger.lostBags(true);
        return bagsCollected;
    }

    /**
     * Mehtod to reset baggage collection variable by the porter thread
     */
    public synchronized void newFlight() {
        this.collectedAllBags = false;
    }
}