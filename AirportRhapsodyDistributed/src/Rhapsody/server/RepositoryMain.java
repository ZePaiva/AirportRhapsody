package Rhapsody.server;

import java.net.SocketTimeoutException;

import Rhapsody.common.RunParameters;
import Rhapsody.server.communications.ServerCom;
import Rhapsody.server.communications.TunnelProvider;
import Rhapsody.server.proxies.RepositoryProxy;
import Rhapsody.server.sharedRegions.Repository;

/**
 * Repository server launcher
 * 
 * @author José Paiva
 * @author André Mourato
 */

public class RepositoryMain {
    
    /**
     * Repository main
     * @param args
     */
    public static void main(String[] args) throws SocketTimeoutException {
        
        ServerCom serverCom, serverConn;
        TunnelProvider provider;

        Repository repository = new Repository();
        RepositoryProxy repositoryProxy = new RepositoryProxy(repository);
        
        // start the repository server
        serverCom = new ServerCom(RunParameters.RepositoryPort, 1000);
        serverCom.start();

        while (repositoryProxy.hasSimEnded()) {
            try {
                serverConn = serverCom.accept();
                provider = new TunnelProvider();
            } catch (SocketTimeoutException e) {
                System.err.printf("%s [REPOSITORYMAIN] socket timouted\n", Thread.currentThread().getName());
                e.printStackTrace();
                System.exit(2);
            }
        }

    }
}