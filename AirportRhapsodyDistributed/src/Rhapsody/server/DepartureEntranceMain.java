package Rhapsody.server;

import Rhapsody.server.stubs.GeneralRepositoryStub;

import java.io.IOException;
import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.DepartureEntranceProxy;
import Rhapsody.server.sharedRegions.DepartureEntrance;
import Rhapsody.server.stubs.ArrivalExitStub;

/**
 * Departure entrance entity main
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureEntranceMain {

    /**
     * Main method
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        /**
         * Create communication utilities
         */
        ServerCom serverCommunication, serverConnections;
        TunnelProvider provider;

        /**
         * Create stubs
         */
        GeneralRepositoryStub repository = new GeneralRepositoryStub();
        ArrivalExitStub arrivalExitStub = new ArrivalExitStub();

        /**
         * Create main entity
         */
        DepartureEntrance departureEntrance = new DepartureEntrance(repository, arrivalExitStub);

        /**
         * Create proxy
         */
        DepartureEntranceProxy proxy = new DepartureEntranceProxy(departureEntrance);

        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.DepartureEntrancePort, 1000);
        serverCommunication.start();

        System.out.println("Departure entrance started");
        while (!proxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunication.accept();
                provider = new TunnelProvider(proxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [DEPARTUREENTRANCE] socket timouted\n", Thread.currentThread().getName());
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
        System.out.println("[Departure Transfer] terminating...");
    }
}