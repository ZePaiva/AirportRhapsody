package Rhapsody.server;

import java.io.IOException;
import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.ArrivalLoungeProxy;
import Rhapsody.server.sharedRegions.ArrivalLounge;
import Rhapsody.server.stubs.BaggageCollectionStub;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Main class for the arrival lounge entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalLoungeMain {

    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String[] args) {

        /**
         * Create communication utilities
         */
        ServerCom serverCommunication, serverConnections;
        TunnelProvider provider;

        /**
         * Create stubs
         */
        GeneralRepositoryStub repository = new GeneralRepositoryStub();
        BaggageCollectionStub baggageCollection = new BaggageCollectionStub();

        /**
         * Create main entity
         */
        ArrivalLounge arrivalLounge = new ArrivalLounge(repository, baggageCollection);

        /**
         * Create proxy
         */
        ArrivalLoungeProxy arrivalLoungeProxy = new ArrivalLoungeProxy(arrivalLounge);

        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.ArrivalLoungePort, 1000);
        serverCommunication.start();

        System.out.println("Arrival Lounge started");
        while (!arrivalLoungeProxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunication.accept();
                provider = new TunnelProvider(arrivalLoungeProxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [ARRIVALLOUNGE] socket timouted\n", Thread.currentThread().getName());
            } catch (RuntimeException e) {
                System.err.println("Error on proxy");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("[Arrival Lounge] terminating...");
    }
}