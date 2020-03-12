package Rhapsody.entities.states;

/**
 * Enum class with possible states for the Passenger class
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */

public enum BusDriverState {

    /**
     * Double blocking state (initial / final state)<p/>
     * 
     * The driver is waken up the first time by the Passenger who arrives at the transfer terminal and finds out it's place in the waiting queue equals the bus capacity, 
     * or when the departure time has been reached<br/>
     * The driver is waken up the second time by the last passenger to enter the bus <p/>
     * @StateTransitions
     * {@link Rhapsody.entities.BusDriver#hasDaysWorkEnded} keeps instance on the same state.<p/>
     * {@link Rhapsody.entities.BusDriver#announcingBusBoarding} keeps instance on the same state. <p/>
     * {@link Rhapsody.entities.BusDriver#goToDepartureTerminal} puts instante in {@link BusDriverState#DRIVING_FORWARD} state. <p/>
     * @Triggers
     * {@link Rhapsody.entities.Passenger#takeABus} triggers {@link Rhapsody.entities.BusDriver#announcingBusBoarding} (wakes the bus driver). <p/>
     * {@link Rhapsody.entities.Passenger#enterTheBus} triggers {@link Rhapsody.entities.BusDriver#goToDepartureTerminal} when bus is at full capacity.
     */
    PARKING_AT_THE_ARRIVAL_LOUNGE,
    /**
     * Transition state<p/>
     * 
     * Used for when driver is transfering passengers to departure terminal <p/>
     * @StateTransitions
     * {@link Rhapsody.entities.BusDriver#parkTheBusAndLetPassOff} puts instante in {@link BusDriverState#PARKING_AT_THE_DEPARTURE_TERMINAL} state. <p/>
     * 
     */
    DRIVING_FORWARD,
    /**
     * Blocking state
     * <p/>
     * The driver is waken up by the last passenger to exit the bus. <p/>
     * @StateTransitions
     * {@link Rhapsody.entities.BusDriver#goToArrivalTerminal} puts instante in {@link BusDriverState#DRIVING_BACKWARD} state. <p/>
     * @Triggers
     * {@link Rhapsody.entities.Passenger#leaveTheBus()} triggers {@link Rhapsody.entities.BusDriver#goToArrivalTerminal} (wakes the bus driver). <p/>
     */
    PARKING_AT_THE_DEPARTURE_TERMINAL,
    /**
     * Transition state
     * 
     * Used for when driver is transfering passengers to arrival lounge
     * {@link Rhapsody.entities.BusDriver#parkTheBus} puts instante in {@link BusDriverState#PARKING_AT_THE_ARRIVAL_LOUNGE} state. <p/>
     * 
     */
    DRIVING_BACKWARD,
}