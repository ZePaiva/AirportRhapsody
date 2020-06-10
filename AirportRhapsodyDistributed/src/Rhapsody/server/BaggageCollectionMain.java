package Rhapsody.server;

import java.io.IOException;
import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.BaggageCollectionProxy;
import Rhapsody.server.sharedRegions.BaggageCollection;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Main class for the luggage collection point entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class BaggageCollectionMain {

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

        /**
         * Create main entity
         */
        BaggageCollection baggageCollection = new BaggageCollection(repository);

        /**
         * Create proxy
         */
        BaggageCollectionProxy baggageCollectionProxy = new BaggageCollectionProxy(baggageCollection);

        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.BaggageCollectionPort, 1000);
        serverCommunication.start();

        System.out.println("Baggage Collection started");
        while (!baggageCollectionProxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunication.accept();
                provider = new TunnelProvider(baggageCollectionProxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [BAGGAGECOLLECTION] socket timouted\n", Thread.currentThread().getName());
            }  catch (NullPointerException e) {
                System.err.println("Nothing Connected");  
            } catch (RuntimeException e) {
                System.err.println("Error on proxy");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[Baggage Collection] terminating...");
    }
}