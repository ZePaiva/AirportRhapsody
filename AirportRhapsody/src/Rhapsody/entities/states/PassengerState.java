package Rhapsody.entities.states;

/**
 * Enum class with possible states for the Passenger class
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */

public enum PassengerState {

    /**
     * Transition state (initial / final state)
     * <p/>
     * Passenger just arrived to the airport, either disembarked or to embark.
     * <p/>
     * @StateTransitions
     * {@link Rhapsody.entities.Passenger#whatShouldIDo} keeps instance on this state. <p/>
     * {@link Rhapsody.entities.Passenger#goCollectABag} puts instance in {@link PassengerState#AT_LUGGAGE_COLLECTION} state.<p/>
     * {@link Rhapsody.entities.Passenger#goHome} puts instance in {@link PassengerState#EXIT_TERMINAL} state.<p/>
     * {@link Rhapsody.entities.Passenger#takeABus} puts instance in {@link PassengerState#ARRIVING_TRANSFER_TERMINAL} state.
     */
    AT_DISEMBARKING_ZONE,
    /**
     * Blocking state with eventual transition.
     * <p/>
     * The Passenger is waken up by the Porter when he places on the conveyor belt an owned bag,
     * or when the Porter signals that there are no more pieces of luggage in the plane hold
     * and makes a transition when either the Passenger possess all the owned baggages, 
     * or was signaled that there are no more bags in the plane hold.
     * <p/>
     * @StateTransitions
     * {@link Rhapsody.entities.Passenger#goCollectABag} puts instance in {@link PassengerState#AT_LUGGAGE_COLLECTION} state.<p/>
     * {@link Rhapsody.entities.Passenger#goHome} puts instance in {@link PassengerState#EXIT_ARRIVAL_TERMINAL} state.<p/>
     * {@link Rhapsody.entities.Passenger#reportMissingBags} puts instance in {@link PassengerState#AT_LUGGAGE_RECLAIM} state.<p/>
     * @Triggers
     * {@link Rhapsody.entities.Porter#carryItToAppropriateStore} wakes up the Passenger instance to check if it's passenger baggage and might decrease the instance baggage countdown if positive.<p/>
     * {@link Rhapsody.entities.Porter#tryToCollectABag} signals bags collection finished.<p/>
     * In case the baggage countdown is not 0 after baggage collection is finished, {@link Rhapsody.entities.Passenger#reportMissingBags} is triggered.<br/>
     * In case the baggage countdown is 0 after baggage collection is finished, {@link Rhapsody.entities.Passenger#goHome} is triggered.<br/>
     */
    AT_LUGGAGE_COLLECTION,
    /**
     * Transition state 
     * <p/>
     * Passenger is at Luggage reclaim office
     * <p/>
     * @StateTransitions
     * {@link Rhapsody.entities.Passenger#goHome} puts instance in {@link PassengerState#EXIT_ARRIVAL_TERMINAL} state.<p/>
     */
    AT_LUGGAGE_RECLAIM,
    /**
     * Blocking state with eventual transition (final state)<p/>
     * The passenger is waken up by the the last Passenger of each flight to exit the arrival terminal or to enter the departure terminal.
     */
    EXIT_ARRIVAL_TERMINAL,
    /**
     * Blocking state
     * <p/>
     * Before blocking, the Passenger wakes up the bus driver if the Passenger's place in the waiting queue equals the bus capacity.
     * <p/> 
     * The Passenger is woken up by the Bus Driver to entry in the bus.
     * <p/>
     * @StateTransitions
     * {@link Rhapsody.entities.Passenger#enterTheBus} puts instance in {@link PassengerState#TERMINAL_TRANSFER} and wakes BusDriver up if needed.
     * @Triggers
     * {@link Rhapsody.entities.BusDriver#announcingBusBoarding} triggers {@link Rhapsody.entities.Passenger#enterTheBus}.
     */
    ARRIVING_TRANSFER_TERMINAL,
    /**
     * Blocking state
     * <p/>
     * The passenger is waken up by the Bus Driver.
     * <p/>
     * @StateTransitions
     * StateTransitions
     * {@link Rhapsody.entities.Passenger#leaveTheBus} puts instance in {@link PassengerState#DEPARTING_TRANSFER_TERMINAL}.
     * @Triggers
     * {@link Rhapsody.entities.BusDriver#parkTheBusAndLetPassOff} triggers {@link Rhapsody.entities.Passenger#leaveTheBus}.
     */
    TERMINAL_TRANSFER,
    /**
     * Transition state
     * <p/>
     * StateTransitions
     * {@link Rhapsody.entities.Passenger#prepareNextLeg} puts instance in {@link PassengerState#DEPARTING}.
     */
    DEPARTING_TRANSFER_TERMINAL,
    /**
     * Blocking state with eventual transition (final state)
     * The passenger is waken up by the last Passenger of each flight to exit the arrival terminal or to enter the departure terminal.
     */
    DEPARTING;
}