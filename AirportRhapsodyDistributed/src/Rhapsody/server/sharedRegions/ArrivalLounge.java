package Rhapsody.server.sharedRegions;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Rhapsody.common.Luggage;
import Rhapsody.common.RunParameters;
import Rhapsody.common.States;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.interfaces.PassengerInterface;
import Rhapsody.server.interfaces.PorterInterface;
import Rhapsody.server.stubs.BaggageCollectionStub;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Arival lounge entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalLounge {

    /**
     * Logger stub
     */
    public GeneralRepositoryStub generalRepository;

    /**
     * Luggage collection point stub
     */
    public BaggageCollectionStub baggageCollection;

    /**
     * passengers that disembarked
     */
    private int passengersDisembarked;

    /**
     * has simulation ended
     */
    private boolean simulationStatus;

    /**
     * Current luggage in the plane's hold
     */
    private Queue<Luggage>[] planeHoldLuggage;

    /**
     * Current flight ID attending
     */
    private int currentFlight;

    /**
     * Has porter logged in yet
     */
    private boolean airportOpen;

    /**
     * Passengers started to disembark
     */
    private boolean init;

    /**
     * All passengers disembarked
     */
    private boolean limit;

    /**
     * var to help if we can start simulatiuon or not, checks if all passengers have
     * already registered in the lopunge
     */
    private int allPassRegistered;

    /**
     * prettify
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PASSENGER = "\u001B[0m\u001B[32m";
    public static final String ANSI_PORTER = "\u001B[0m\u001B[36m";

    /**
     * randomizer
     */
    private Random random = new Random();

    /**
     * Arrival Lounge constructor
     * 
     * @param generalRepositoryStub
     * @param baggageCollectionStub
     */
    @SuppressWarnings("unchecked")
    public ArrivalLounge(GeneralRepositoryStub generalRepositoryStub, BaggageCollectionStub baggageCollectionStub) {
        this.planeHoldLuggage = new Queue[RunParameters.K];
        this.generalRepository = generalRepositoryStub;
        this.baggageCollection = baggageCollectionStub;
        this.passengersDisembarked = 0;
        this.simulationStatus = false;
        this.currentFlight = -1;
        this.airportOpen = false;
        this.init = true;
        this.limit = false;
        for (int i = 0; i < RunParameters.K; i++) {
            planeHoldLuggage[i] = new LinkedList<>();
        }
        this.allPassRegistered = 0;
        this.generalRepository.registerMem(0);
    }

    /**
     * Puts porter in {@link Rhapsody.common.States#WAITING_FOR_PLANE_TO_LAND}
     * 
     * @return simulationContinue
     */
    public synchronized boolean takeARest() {
        // get porter thread
        PorterInterface porter = (TunnelProvider) Thread.currentThread();

        while (this.allPassRegistered < RunParameters.N) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        this.generalRepository.updatePorterState(porter.getEntityState(), true);

        while (this.passengersDisembarked > 0 && !this.init || this.simulationStatus) {
            try {
                if (this.simulationStatus) {
                    return !this.simulationStatus;
                }
                wait();
            } catch (InterruptedException e) {
            }
        }
        this.currentFlight++;
        this.generalRepository.clearFlight(true);
        System.out.println(ANSI_PORTER + "[PORTER---] Flight cleared" + ANSI_RESET);
        this.generalRepository.updateFlight(this.currentFlight, true);
        System.out.println(ANSI_PORTER + "[PORTER---] Signaling BCP of new flight" + ANSI_RESET);
        this.baggageCollection.newFlight();

        // reset passengerxs disembarked
        this.init = false;
        this.airportOpen = true;
        this.limit = false;
        this.generalRepository.updateFlight(this.currentFlight, true);
        System.out.printf(ANSI_PORTER + "[PORTER---] Flight updated | NF %d %s\n", this.currentFlight, ANSI_RESET);
        notifyAll();

        // update porter
        porter.setEntityState(States.WAITING_FOR_PLANE_TO_LAND);
        if (this.currentFlight > 1) {
            this.generalRepository.updateBagsInPlane(planeHoldLuggage[this.currentFlight].size(), true);
        }

        // this.generalRepository.updatePorterState(porter.getEntityState(), true);

        System.out.printf(ANSI_PORTER + "[PORTER---] Porter waiting for passengers | CP: %d | Sim %s %s\n",
                this.passengersDisembarked, this.simulationStatus, ANSI_RESET);
        // waits for all passengers to arrive
        while (this.passengersDisembarked < RunParameters.N && !this.simulationStatus) {
            try {
                wait();
                System.out.printf(ANSI_PORTER + "[PORTER---] Passenger disembarked | PD: %d" + ANSI_RESET + "\n",
                        this.passengersDisembarked);
            } catch (InterruptedException e) {
                System.err.print("[LOUNGE---] Porter interrupted, check log\n");
                System.exit(3);
            }
        }
        this.airportOpen = false;
        this.limit = true;

        return !this.simulationStatus;
    }

    /**
     * Puts passenger in {@link Rhapsody.common.States#AT_DISEMBARKING_ZONE} state.
     * <p/>
     * Disembarks passenger and notifies all other passengers
     * 
     * @param flightId
     */
    public synchronized void whatShouldIDo(int flightId) {
        PassengerInterface passenger = (TunnelProvider) Thread.currentThread();
        System.out.printf(ANSI_PASSENGER + "[PASSENGER] P%d disembarked on flight %d" + ANSI_RESET + "\n",
                passenger.getEntityID(), flightId);
        // delay to allow porter and bus to setup
        if (this.passengersDisembarked > 0 && this.limit) {
            this.passengersDisembarked--;
            System.out.printf(ANSI_PASSENGER + "[PASSENGER] P%d terminated | PD %d " + ANSI_RESET + "\n",
                    passenger.getEntityID(), this.passengersDisembarked);
            notifyAll();
        }
        while (!this.airportOpen || (this.passengersDisembarked > 0 && this.limit)) {
            try {
                System.out.printf(ANSI_PASSENGER + "[PASSENGER] P%d | Porter not ready yet" + ANSI_RESET + "\n",
                        passenger.getEntityID());
                wait();
            } catch (InterruptedException e) {
            }
        }
        // updates state
        passenger.setEntityState(States.AT_DISEMBARKING_ZONE);
        this.generalRepository.burstAddPass(passenger.getEntityID(), passenger.getEntityState(),
                passenger.getSituation(), passenger.getStartingBags(), 0, false);
        // this.generalRepository.updatePassengerState(passenger.getEntityState(),
        // passenger.getEntityID(), true);
        // this.generalRepository.addPassengerToFlight(passenger.getEntityID(), true);
        // this.generalRepository.updatePlaneHoldBags(passenger.getStartingBags(),
        // true);
        //// bug start
        // this.generalRepository.updateSituation(passenger.getEntityID(),
        // passenger.getSituation(), true);
        //// bug here
        // this.generalRepository.updateStartingBags(passenger.getEntityID(),
        // passenger.getStartingBags(), true);
        // this.generalRepository.updateCurrentBags(passenger.getEntityID(), 0, false);

        // disembarks passenger
        this.passengersDisembarked++;
        notifyAll();
        return;
    }

    /**
     * Porter method to try to collect a bag or fail and exit the bag collection
     * loop
     * <p/>
     * <b>DOES NOT ALTER BAGS IN PLANE'S HOLD OR STOREROOM</b>
     * 
     * @return planeHasBags of type boolean
     */
    public synchronized Luggage tryToCollectABag() {
        PorterInterface porter = (TunnelProvider) Thread.currentThread();
        porter.setEntityState(States.AT_THE_PLANES_HOLD);

        System.out.printf(ANSI_PORTER + "[PORTER---] Porter trying to collect a bag" + ANSI_RESET + "\n");
        try {
            if (this.planeHoldLuggage[this.currentFlight].isEmpty()) {
                System.out.printf(ANSI_PORTER + "[PORTER---] Porter got 0 bags" + ANSI_RESET + "\n");
                this.generalRepository.updatePorterState(porter.getEntityState(), false);
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            this.currentFlight = this.planeHoldLuggage.length - 1;
            if (this.planeHoldLuggage[this.currentFlight].isEmpty()) {
                System.out.printf(ANSI_PORTER + "[PORTER---] Porter got 0 bags" + ANSI_RESET + "\n");
                this.generalRepository.updatePorterState(porter.getEntityState(), false);
                return null;
            }
        }

        System.out.printf(ANSI_PORTER + "[PORTER---] Porter got bags" + ANSI_RESET + "\n");
        Luggage bagToReturn = this.planeHoldLuggage[this.currentFlight].poll();
        this.generalRepository.updatePorterState(porter.getEntityState(), true);
        this.generalRepository.updateBagsInPlane(this.planeHoldLuggage[this.currentFlight].size(), false);
        return bagToReturn;
    }

    /**
     * Method to signal Porter that the simulation has ended
     */
    public synchronized void endOfWork() {
        this.simulationStatus = true;
        notifyAll();
    }

    /**
     * Method to update the starting bags of the planes
     * 
     * @param passengerID
     * @param bags
     * @param situations
     */
    public synchronized void updateStartingBags(int passengerID, int[] bags, int[] situations) {
        assert (bags.length == RunParameters.K);
        assert (situations.length == RunParameters.K);

        for (int i = 0; i < RunParameters.K; i++) {
            for (int j = 0; j < bags[i]; j++) {
                if (planeHoldLuggage[i] == null) {
                    planeHoldLuggage[i] = new LinkedList<>();
                }
                if (random.nextInt(100) <= 90) {
                    planeHoldLuggage[i].add(new Luggage(passengerID, situations[i] == 1 ? "FDT" : "TRT"));
                }
            }
        }
        this.allPassRegistered++;
        notifyAll();
        this.generalRepository.updateBagsInPlane(planeHoldLuggage[0].size(), true);
    }
}