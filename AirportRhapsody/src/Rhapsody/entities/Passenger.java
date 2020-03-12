package Rhapsody.entities;

import Rhapsody.entities.states.PassengerState;

/**
 * Passenger Thread Implements the lif-cycle of th passenger and stores it's
 * current state.
 * 
 * @author José Paiva
 * @author Andre Mourato
 */
public class Passenger extends Thread {
    

    private PassengerState currentState;

    public Passenger() {
        currentState=currentState.AT_DISEMBARKING_ZONE;
    }

    public void run() {
        // run
    }
    
    public void whatShouldIDo(){}
    public void goCollectABag(){}
    public void reportMissingBags(){}
    public void goHome(){}
    public void takeABus() {}
    public void enterTheBus(){}
    public void leaveTheBus(){}
    public void prepareNextLeg(){}
    
}
