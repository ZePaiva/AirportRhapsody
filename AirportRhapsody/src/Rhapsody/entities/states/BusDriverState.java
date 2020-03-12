package Rhapsody.entities.states;

/**
 * Enum class with possible states for the Passenger class
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */

public enum BusDriverState {

    /**
     * Double blocking state (initial / final state)
     * 
     * The driver is waken up the first time by the operation takeABus of the Passenger who arrives at
     * the transfer terminal and finds out her place in the waiting queue equals the bus capacity, or
     * when the departure time has been reached 
     * (transition will only occurs if there is at least one passenger forming the queue); 
     * The driver is waken up the second time by the operation enter
     * TheBus of the last passenger to enter the bus
     */
    PARKING_AT_THE_ARRIVAL_LOUNGE,
    /**
     * Transition state
     * 
     * Used for when driver is transfering passengers to departure terminal  
     */
    DRIVING_FORWARD,
    /**
     * Blocking state
     * 
     * The driver is waken up by the operation leaveTheBus of the last passenger to exit the bus.
     */
    PARKING_AT_THE_DEPARTURE_TERMINAL,
    /**
     * Transition state
     * 
     * Used for when driver is transfering passengers to arrival lounge
     */
    DRIVING_BACKWARD,
}