package Rhapsody.server;

import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.BaggageReclaimProxy;
import Rhapsody.server.sharedRegions.BaggageReclaim;
import Rhapsody.server.stubs.GeneralRepositoryStub;

/**
 * Luggage reclaim office entity instantiator
 * 
 * @author José Paiva
 * @author André Mourato
 */
public class BaggageReclaimMain {
    
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
        BaggageReclaim baggageReclaim = new BaggageReclaim(repository);

        /**
         * Create proxy
         */
        BaggageReclaimProxy proxy = new BaggageReclaimProxy(baggageReclaim);
    
        /**
         * Start comms
         */
        serverCommunication = new ServerCom(RunParameters.BaggageReclaimPort, 1000);
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
        System.out.println("[Baggage Reclaim] terminating...");
    }
}