package Rhapsody.server;

import Rhapsody.server.stubs.GeneralRepositoryStub;

import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.DepartureEntranceProxy;
import Rhapsody.server.sharedRegions.ArrivalLounge;
import Rhapsody.server.sharedRegions.DepartureEntrance;
import Rhapsody.server.stubs.ArrivalExitStub;
import Rhapsody.server.stubs.ArrivalLoungeStub;
import Rhapsody.server.stubs.ArrivalQuayStub;

/**
 * Departure entrance entity main
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureEntranceMain {
    
    /**
     * Main method
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
        ArrivalLoungeStub lounge = new ArrivalLoungeStub();
        ArrivalQuayStub arrivalTerminalTransfer = new ArrivalQuayStub();
        ArrivalExitStub arrivalExitStub = new ArrivalExitStub();

        /**
         * Create main entity
         */
        DepartureEntrance departureEntrance = new DepartureEntrance(repository, arrivalExitStub, lounge, arrivalTerminalTransfer);

        /**
         * Create proxy
         */
        DepartureEntranceProxy proxy = new DepartureEntranceProxy(departureEntrance);
    
        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.DepartureEntrancePort, 1000);
        serverCommunication.start();

        while (!proxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunication.accept();
                provider = new TunnelProvider(proxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.println("Socket timeout on arrival exit");
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.err.println("Error on proxy");
                e.printStackTrace();
            }     
        }
        System.out.println("[Departure Transfer] terminating...");
    }
}