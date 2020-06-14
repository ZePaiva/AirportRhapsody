package Rhapsody.server;

import Rhapsody.server.stubs.DepartureEntranceStub;

import java.io.IOException;
import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.ArrivalExitProxy;
import Rhapsody.server.sharedRegions.ArrivalExit;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Main class for the Arrival Terminal Exit Shared memmory Launches the Java VM
 * for this entity only
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalExitMain {

    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String[] args) {

        /**
         * Create communication utilities
         */
        ServerCom serverCommunications, serverConnections;
        TunnelProvider provider;

        /**
         * Create stubs for cross comunication
         */
        GeneralRepositoryStub repository = new GeneralRepositoryStub();
        DepartureEntranceStub departureEntrance = new DepartureEntranceStub();

        /**
         * Create main entity
         */
        ArrivalExit arrivalExit = new ArrivalExit(repository, departureEntrance);

        /**
         * Create main entity proxy
         */
        ArrivalExitProxy arrivalExitProxy = new ArrivalExitProxy(arrivalExit);

        /**
         * Start comms
         */
        serverCommunications = new ServerCom(RunParameters.ArrivalExitPort, 1000);
        serverCommunications.start();

        System.out.println("Arrival Exit started");
        while (!arrivalExitProxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunications.accept();
                provider = new TunnelProvider(arrivalExitProxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [ARRIVALEXIT] socket timouted\n", Thread.currentThread().getName());
            } catch (NullPointerException e) {
                // System.err.println("Nothing Connected");
            } catch (RuntimeException e) {
                System.err.println("Error on proxy");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        repository.closeStub();
        System.out.println("[Arrival Terminal Exit] terminating...");
    }
}