package Rhapsody.server;

import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import Rhapsody.common.RunParameters;
import Rhapsody.common.States;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.GeneralRepositoryProxy;
import Rhapsody.server.sharedRegions.GeneralRepository;

/**
 * Repository server launcher
 * 
 * @author José Paiva
 * @author André Mourato
 */

public class GeneralRepositoryMain {

    /**
     * Repository main
     * 
     * @param args
     */
    public static void main(String[] args) {

        ServerCom serverCom, serverConn;
        TunnelProvider provider;
        String logFile = RunParameters.logFile;

        // check if file exists, if not create
        Path path = Paths.get(logFile);
        if (Files.notExists(path)) {
            logFile = "logs.txt";
        }

        // create empty arrays
        int[] flightPassengers = new int[RunParameters.N];
        Arrays.fill(flightPassengers, -1);
        int[] waitingQueue = new int[RunParameters.N];
        Arrays.fill(waitingQueue, -1);
        int[] seats = new int[RunParameters.T];
        Arrays.fill(seats, -1);
        States[] passengersStates = new States[RunParameters.N];
        Arrays.fill(passengersStates, null);
        String[] passengersSituationG = new String[RunParameters.N];
        Arrays.fill(passengersSituationG, null);
        int[] bags = new int[RunParameters.N];
        Arrays.fill(bags, -1);

        // create repository
        GeneralRepository repository = new GeneralRepository(logFile, null, null, passengersStates, waitingQueue, seats,
                passengersSituationG, bags, bags.clone());
        GeneralRepositoryProxy repositoryProxy = new GeneralRepositoryProxy(repository);

        // start the repository server
        serverCom = new ServerCom(RunParameters.RepositoryPort, 1000);
        serverCom.start();

        System.out.println("Repository started, timeout set to 10 seconds");
        while (!repositoryProxy.hasSimEnded()) {
            try {
                serverConn = serverCom.accept();
                provider = new TunnelProvider(repositoryProxy, serverConn);
                provider.start();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [REPOSITORYMAIN] socket timouted\n", Thread.currentThread().getName());
            } catch (NullPointerException e) {
                System.err.println("Nothing Connected");
            } catch (Exception e) {
                System.err.printf("%s [REPOSITORYMAIN] unknown error\n", Thread.currentThread().getName());
                e.printStackTrace();
            }
        }
        repository.finish();
    }
}