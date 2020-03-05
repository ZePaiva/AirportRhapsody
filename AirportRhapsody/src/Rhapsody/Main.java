package Rhapsody;
import Rhapsody.interfaces.IDecrement;
import Rhapsody.interfaces.IIncrement;
import Rhapsody.monitors.Monitor;
import Rhapsody.monitors.MonitorSync;
import Rhapsody.threadHandlers.ThreadDecrement;
import Rhapsody.threadHandlers.ThreadIncrement;

public class Main {

    public static void main(String[] args) {
        int nThreads=5;
        ThreadIncrement [] threadAdders = new ThreadIncrement[nThreads];
        Monitor monitor = new Monitor();
        //MonitorSync monitor = new MonitorSync();
        for (int i=0; i<nThreads; i++) {
            threadAdders[i] = new ThreadIncrement(i+1, (IIncrement) monitor);
            threadAdders[i].start();
        }
        ThreadDecrement threadDecrement = new ThreadDecrement((IDecrement) monitor);
        threadDecrement.start();
        try {
            for (int i=0; i < nThreads; i++) {
                threadAdders[i].join();
            }
            threadDecrement.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
