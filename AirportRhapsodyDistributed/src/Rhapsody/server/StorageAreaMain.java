package Rhapsody.server;

import java.io.IOException;
import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.StorageAreaProxy;
import Rhapsody.server.sharedRegions.StorageArea;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Storage area main
 * 
 * @author José Paiva
 * @author Andŕe Mourato
 */
public class StorageAreaMain {

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
        StorageArea storageArea = new StorageArea(repository);

        /**
         * Create proxy
         */
        StorageAreaProxy proxy = new StorageAreaProxy(storageArea);

        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.StorageAreaPort, 1000);
        serverCommunication.start();

        while (!proxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunication.accept();
                provider = new TunnelProvider(proxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [STORAGEAREA] socket timouted\n", Thread.currentThread().getName());
            } catch (RuntimeException e) {
                System.err.println("Error on proxy");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("[Baggage Reclaim] terminating...");
    }
}