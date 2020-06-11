package Rhapsody.server.proxies;

import Rhapsody.common.Message;
import Rhapsody.common.RunParameters;
import Rhapsody.server.sharedRegions.GeneralRepository;

/**
 * Repository proxy data type
 */
public class GeneralRepositoryProxy implements SharedMemoryProxy {

    /**
     * Repository
     * 
     * @serialField repository
     */
    private final GeneralRepository repository;

    /**
     * Simulation status
     */
    private boolean finished;

    /**
     * RepositoryProxy Construtor method
     * 
     * @param repository
     */
    public GeneralRepositoryProxy(GeneralRepository repository) {
        this.repository = repository;
        this.finished=false;
    }

    /**
     * Message processor
     * @param packet message from clients
     * @return reply to message
     */
    public Message proccesPacket(Message pkt) {

        Message reply = new Message();

        System.out.println("Got message of type " + pkt.getType());
        System.out.println("Message: " +pkt.toString());

        switch(pkt.getType()) {
            case UP_PASS_STATE:
                repository.updatePassengerState(pkt.getState(), pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_BD_STATE:
                repository.updateBusDriverState(pkt.getState(), pkt.getBool1());
                break;
            
            case UP_PORTER_STATE:
                repository.updatePorterState(pkt.getState(), pkt.getBool1());
                break;
            
            case UP_PASS_IN_WAIT:
                repository.addToWaitingQueue(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_PASS_OUT_WAIT:
                repository.removeFromWaitingQueue(pkt.getBool1());
                break;
            
            case UP_PASS_IN_BUS:
                repository.addToBusSeat(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_PASS_OUT_BUS:
                repository.removeFromBusSeat(pkt.getBool1());
                break;
            
            case UP_PASS_IN_FLIGHT:
                repository.addPassengerToFlight(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_PASS_OUT_FLIGHT:
                repository.removePassengerFromFlight(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_FLIGHT:
                repository.updateFlight(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_FLIGHT_CLEAR:
                repository.clearFlight(pkt.getBool1());
                break;
            
            case UP_BAG_PLANE:
                repository.updateBagsInPlane(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_BAG_CB:
                repository.updateConveyorBags(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_BAG_SR:
                repository.updateStoreRoomBags(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_PASS_SIT:
                repository.updateSituation(pkt.getInt1(), pkt.getBool1() ? "FDT" : "TRT" , pkt.getBool2());
                break;
            
            case UP_PASS_SB:
                repository.updateStartingBags(pkt.getInt1(), pkt.getInt2(), pkt.getBool1());
                break;
            
            case UP_PASS_CB:
                repository.updateCurrentBags(pkt.getInt1(), pkt.getInt2(), pkt.getBool1());
                break;
            
            case UP_FDT:
                repository.updateFDTPassengers(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_TRT:
                repository.updateTRTPassengers(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_BAG_PH:
                repository.updatePlaneHoldBags(pkt.getInt1(), pkt.getBool1());
                break;
            
            case UP_BAG_L:
                repository.updateLostbags(pkt.getInt1(), pkt.getBool1());
                break;
            
            case SIM_ENDED:
                this.finished=true;
                break;
            
            case LOG_MEM:
                switch (pkt.getInt1()) {
                    case 0:
                        System.out.println("ALG registered");
                        break;
            
                    case 1:
                        System.out.println("ATE connected");
                        break;
            
                    case 2:
                        System.out.println("AQA connected");
                        break;
            
                    case 3:
                        System.out.println("BCP connected");
                        break;
            
                    case 4:
                        System.out.println("BRP connected");
                        break;
            
                    case 5:
                        System.out.println("DTE connected");
                        break;
            
                    case 6:
                        System.out.println("DTT connected");
                        break;
            
                    case 7:
                        System.out.println("SRM connected");
                        break;
            
                    default:
                        break;
                }
                break;
            
            default:
                throw new RuntimeException("Wrong operation in message: " + pkt.getType());
        }
        System.out.println("Returning");
        return reply;
    }

    public boolean hasSimEnded() {
        return this.finished;
    }
}