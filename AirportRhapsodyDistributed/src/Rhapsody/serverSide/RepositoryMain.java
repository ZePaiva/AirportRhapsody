package Rhapsody.serverSide;

import Rhapsody.serverSide.communications.ServerCom;
import Rhapsody.serverSide.interfaces.TunnelProvider;
import Rhapsody.serverSide.proxies.RepositoryProxy;
import Rhapsody.serverSide.sharedRegions.Repository;

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
    public static void main(String[] args) {
        
        ServerCom serverCom, serverConn;
        TunnelProvider provider;

        Repository repository = new Repository();
        RepositoryProxy repositoryProxy = new RepositoryProxy();
        
        // start the repository server
        serverCom = new ServerCom(8000);
        serverCom.start();

    }
}