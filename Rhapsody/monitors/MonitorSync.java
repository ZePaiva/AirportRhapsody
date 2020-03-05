package Rhapsody.monitors;

import Rhapsody.interfaces.IDecrement;
import Rhapsody.interfaces.IIncrement;

public class MonitorSync implements IIncrement, IDecrement {

    private int count=0;

    @Override
    public synchronized void increment(int threadId) {
        System.out.println("Enter increment: (" + threadId + "): " + count);
        try {
            while (count > 20 )
            {
                System.out.println("Waiting increment(" + threadId + "): " + count );
                wait();
            }
        } catch ( Exception ex) {}
        count++;
        System.out.println("After increment: (" + threadId + "): " + count);
        notify();

    }

    @Override
    public synchronized void decrement() {
        System.out.println("Enter decrement: " + count);
        try {
            while (count < 5) {
                System.out.println("Waiting to decrement: " + count);
                wait();
            }
        } catch( Exception ex) {}
        count -=5;
        System.out.println("After decrement: " + count);
        notifyAll();

    }
}
