package Rhapsody.monitors;

import Rhapsody.interfaces.IDecrement;
import Rhapsody.interfaces.IIncrement;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor implements IDecrement, IIncrement {
    private final ReentrantLock rl;
    private final Condition increment;
    private final Condition decrement;
    private int count=0;


    @Override
    public void decrement() {
        rl.lock();
        try {
            System.out.printf("Enter decrement %d\n", count);
            while (count<5) {
                System.out.printf("Waiting decrement %d\n", count);
                decrement.await();
            }
            count-=5;
            System.out.printf("After decrement %d ",count);
            decrement.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void increment(int threadId) {
        rl.lock();
        try {
            System.out.printf("Enter increment {%   d}: %d\n", threadId, count);
            while (count>=20) {
                System.out.printf("Waiting increment {%d}: %d\n", threadId, count);
                decrement.await();
            }
            count++;
            System.out.printf("After increment {%d}: %d ",threadId,count);
            increment.signalAll();
        } catch (Exception e) {

        } finally {
            rl.unlock();
        }
    }

    public Monitor() {
        this.rl = new ReentrantLock(true);
        this.increment = this.rl.newCondition();
        this.decrement = this.rl.newCondition();
    }
}
