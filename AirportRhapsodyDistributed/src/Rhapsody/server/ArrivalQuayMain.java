package Rhapsody.server;

import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.ArrivalQuayProxy;
import Rhapsody.server.sharedRegions.ArrivalQuay;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Main class for the arrival terminal transfer quay entity
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class ArrivalQuayMain {
    
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
        ArrivalQuay arrivalQuay = new ArrivalQuay(100, repository);

        /**
         * Create proxy
         */
        ArrivalQuayProxy arrivalQuayProxy = new ArrivalQuayProxy(arrivalQuay);
        
        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.ArrivalQuayPort, 1000);
        serverCommunication.start();

        while (!arrivalQuayProxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunication.accept();
                provider = new TunnelProvider(arrivalQuayProxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.println("Socket timeout on arrival exit");
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.err.println("Error on proxy");
                e.printStackTrace();
            }     
        }
        System.out.println("[Arrival Quay] terminating...");
    }
}