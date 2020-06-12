package Rhapsody.server.proxies;

import java.security.Provider;

import Rhapsody.common.Luggage;
import Rhapsody.common.Message;
import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.sharedRegions.ArrivalLounge;

/**
 * Arrival lounge proxy to process and reply to messages
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalLoungeProxy implements SharedMemoryProxy {

    /**
     * Entity that this proxy will handle
     */
    private ArrivalLounge arrivalLounge;

    /**
     * Amount of flights, used when checking if it has ended or not
     */
    private int finished;

    /**
     * Arrival lounge proxy constructor
     * 
     * @param arrivalLounge
     */
    public ArrivalLoungeProxy(ArrivalLounge arrivalLounge) {
        this.arrivalLounge = arrivalLounge;
        this.finished = 0;
    }

    /**
     * Process message and generate reply
     */
    public Message proccesPacket(Message pkt) {
        Message reply = new Message();
        TunnelProvider provider = (TunnelProvider) Thread.currentThread();
        System.out.println("Got message of type " + pkt.getType() + " from " + provider.getName());

        switch (pkt.getType()) {
            // Porter login (takeARest)
            case PORTER_WAITING:
                provider.setEntityState(pkt.getState());
                reply.setBool1(this.arrivalLounge.takeARest());
                reply.setState(provider.getEntityState());
                break;
            // passenger login (whatShouldIDo), must allow passengers to add starting bags
            case PASSENGER_ARRIVED:
                //this.arrivalLounge.updateStartingBags(pkt.getId(), pkt.getIntArray1(), pkt.getIntArray2());
                System.out.println(pkt.toString());
                provider.setEntityID(pkt.getId());
                provider.setEntityState(pkt.getState());
                provider.setStartingBags(pkt.getInt1());
                provider.setSituation(pkt.getInt2());
                System.out.println("entering WSID");
                this.arrivalLounge.whatShouldIDo(provider.getEntityID()); //induces blocking state
                System.out.println("exit WSID");
                reply.setState(provider.getEntityState());
                break;
            // Porter collecting bags from plane hold, will parse to a boolean and a int instead of a luggage
            case PORTER_COLLECT_BAG:
                Luggage bag = this.arrivalLounge.tryToCollectABag();
                if (bag!=null) {
                    reply.setInt1(bag.getPassengerId());
                    reply.setBool1(bag.getLuggageType().equals("FDT"));
                }
                break;
            // in case the passenger is logging into the lounge for the first time, useful to register it's situations and starting bags on the plane
            case PASSENGER_IN:
                this.arrivalLounge.updateStartingBags(pkt.getId(), pkt.getIntArray1(), pkt.getIntArray2());
                break;
            // simulation has ended
            case SIM_ENDED:
                this.finished++;
                System.out.println(finished);
                if (this.finished==1) { arrivalLounge.endOfWork(); System.out.println("clear");}
                break;
            default:
                throw new RuntimeException("Wrong operation in message: " + pkt.getType());
        }
        return reply;
    }

    /**
     * Check simulation status
     */
    public boolean hasSimEnded() {
        return this.finished==2;
    }
}