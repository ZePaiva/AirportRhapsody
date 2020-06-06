package Rhapsody.server;

import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.DepartureQuayProxy;
import Rhapsody.server.sharedRegions.DepartureQuay;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Departure quay main entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class DepartureQuayMain {
    
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
        

        /**
         * Create main entity
         */
        DepartureQuay departureQuay = new DepartureQuay(repository);

        /**
         * Create proxy
         */
        DepartureQuayProxy proxy = new DepartureQuayProxy(departureQuay);
         
        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.DepartureQuayPort, 1000);
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