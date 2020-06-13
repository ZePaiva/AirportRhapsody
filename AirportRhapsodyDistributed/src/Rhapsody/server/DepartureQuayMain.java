package Rhapsody.server;

import java.io.IOException;
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

        System.out.println("Departure terminal transfer started");
        while (!proxy.hasSimEnded()) {
            try {
                serverConnections = serverCommunication.accept();
                provider = new TunnelProvider(proxy, serverConnections);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [DEPARTURETERMINAL] socket timouted\n", Thread.currentThread().getName());
            }  catch (NullPointerException e) {
                //System.err.println("Nothing Connected");  
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