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
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.Lounge#whatShouldIDo} keeps instance on this state. 
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#goCollectABag} or {@link Rhapsody.sharedMems.TemporaryStorage#goCollectABag} puts instance in {@link PassengerState#AT_LUGGAGE_COLLECTION} state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminaExit#goHome} puts instance in {@link PassengerState#EXIT_TERMINAL} state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminaTransfer#takeABus} puts instance in {@link PassengerState#ARRIVING_TRANSFER_TERMINAL} state.
	 * </ul>
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
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#goCollectABag} or {@link Rhapsody.sharedMems.TemporaryStorage#goCollectABag} puts instance in {@link PassengerState#AT_LUGGAGE_COLLECTION} state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalExit#goHome} puts instance in {@link PassengerState#EXIT_ARRIVAL_TERMINAL} state.
	 * <li>{@link Rhapsody.sharedMems.BaggageReclaim#reportMissingBags} puts instance in {@link PassengerState#AT_LUGGAGE_RECLAIM} state.
	 * </ul>
	 */
	AT_LUGGAGE_COLLECTION,
	/**
	 * Transition state 
	 * <p/>
	 * Passenger is at Luggage reclaim office
	 * <p/>
	 * @StateTransitions
	 * {@link Rhapsody.sharedMems.ArrivalTerminalExit#goHome} puts instance in {@link PassengerState#EXIT_ARRIVAL_TERMINAL} state.
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
	 * {@link Rhapsody.sharedMems.ArrivalTerminalTransfer#enterTheBus} puts instance in {@link PassengerState#TERMINAL_TRANSFER} and wakes BusDriver up if needed.
	 */
	ARRIVING_TRANSFER_TERMINAL,
	/**
	 * Blocking state
	 * <p/>
	 * The passenger is waken up by the Bus Driver.
	 * <p/>
	 * @StateTransitions
	 * StateTransitions
	 * {@link Rhapsody.sharedMems.DepartureTerminalTransfer#leaveTheBus} puts instance in {@link PassengerState#DEPARTING_TRANSFER_TERMINAL}.
	 */
	TERMINAL_TRANSFER,
	/**
	 * Transition state
	 * <p/>
	 * StateTransitions
	 * {@link Rhapsody.sharedMems.DepartureTerminalEntrance#prepareNextLeg} puts instance in {@link PassengerState#DEPARTING}.
	 */
	DEPARTING_TRANSFER_TERMINAL,
	/**
	 * Blocking state with eventual transition (final state)
	 * The passenger is waken up by the last Passenger of each flight to exit the arrival terminal or to enter the departure terminal.
	 */
	DEPARTING;
}