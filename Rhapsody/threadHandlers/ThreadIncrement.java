package Rhapsody.threadHandlers;

import Rhapsody.interfaces.IIncrement;

public class ThreadIncrement extends Thread {

    private final IIncrement monitor;
    private final int id;

    public ThreadIncrement (int id, IIncrement increment) {
        this.id=id;
        this.monitor=increment;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (Exception e){
                e.printStackTrace();
            }
            monitor.increment(id);
        }
    }
}
