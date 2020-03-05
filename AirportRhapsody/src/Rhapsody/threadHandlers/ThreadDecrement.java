package Rhapsody.threadHandlers;

import Rhapsody.interfaces.IDecrement;

public class ThreadDecrement extends Thread {
    private final IDecrement monitor;

    public ThreadDecrement(IDecrement decrement) {
        this.monitor=decrement;
    }

    @Override
    public void run() {
        while (true) {
            monitor.decrement();
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
