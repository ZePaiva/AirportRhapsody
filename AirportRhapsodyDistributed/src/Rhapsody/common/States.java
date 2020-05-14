package Rhapsody.common;

/**
 * Enum class with possible states for all entites
 * 
 * @author José Paiva
 * @author André Mourato
 */
public enum States {

    /**
     * BusDriver States
     */

     /**
	 * Double blocking state (initial / final state)<p/>
	 * 
	 * The driver is waken up the first time by the Passenger who arrives at the transfer terminal and finds out it's place in the waiting queue equals the bus capacity, 
	 * or when the departure time has been reached<br/>
	 * The driver is waken up the second time by the last passenger to enter the bus <p/>
	 * @StateTransitions
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalTransfer#hasDaysWorkEnded} keeps instance on the same state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalTransfer#announcingBusBoarding} keeps instance on the same state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalTransfer#goToDepartureTerminal} puts instante in {@link BusDriverState#DRIVING_FORWARD} state. 
	 * </ul>
	 */
	PARKING_AT_THE_ARRIVAL_LOUNGE ("PKAT"),
	/**
	 * Transition state<p/>
	 * 
	 * Used for when driver is transfering passengers to departure terminal <p/>
	 * @StateTransitions
	 * {@link Rhapsody.sharedMems.DepartureTerminalTransfer#parkTheBusAndLetPassOff} puts instante in {@link BusDriverState#PARKING_AT_THE_DEPARTURE_TERMINAL} state. <p/>
	 * 
	 */
	DRIVING_FORWARD ("DRFW"),
	/**
	 * Blocking state
	 * <p/>
	 * The driver is waken up by the last passenger to exit the bus. <p/>
	 * @StateTransitions
	 * {@link Rhapsody.sharedMems.DepartureTerminalTransfer#goToArrivalTerminal} puts instante in {@link BusDriverState#DRIVING_BACKWARD} state. <p/>
	 */
	PARKING_AT_THE_DEPARTURE_TERMINAL ("PKDT"),
	/**
	 * Transition state
	 * <p/>
	 * Used for when driver is transfering passengers to arrival lounge
	 * {@link Rhapsody.sharedMems.ArrivalTerminalTransfer#parkTheBus} puts instante in {@link BusDriverState#PARKING_AT_THE_ARRIVAL_LOUNGE} state. <p/>
	 */
    DRIVING_BACKWARD ("DRBW"),
    
    /**
     * Passenger States
     */

	/**
	 * Transition state (initial / final state)
	 * <p/>
	 * Passenger just arrived to the airport, either disembarked or to embark.
	 * <p/>
	 * @StateTransitions
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.Lounge#whatShouldIDo} keeps instance on this state. 
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#goCollectABag} or {@link Rhapsody.sharedMems.StoreRoom#goCollectABag} puts instance in {@link PassengerState#AT_LUGGAGE_COLLECTION} state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminaExit#goHome} puts instance in {@link PassengerState#EXIT_TERMINAL} state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminaTransfer#takeABus} puts instance in {@link PassengerState#ARRIVING_TRANSFER_TERMINAL} state.
	 * </ul>
	 */
	AT_DISEMBARKING_ZONE ("DEZ"),
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
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#goCollectABag} or {@link Rhapsody.sharedMems.StoreRoom#goCollectABag} puts instance in {@link PassengerState#AT_LUGGAGE_COLLECTION} state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalExit#goHome} puts instance in {@link PassengerState#EXIT_ARRIVAL_TERMINAL} state.
	 * <li>{@link Rhapsody.sharedMems.BaggageReclaim#reportMissingBags} puts instance in {@link PassengerState#AT_LUGGAGE_RECLAIM} state.
	 * </ul>
	 */
	AT_LUGGAGE_COLLECTION ("LGC"),
	/**
	 * Transition state 
	 * <p/>
	 * Passenger is at Luggage reclaim office
	 * <p/>
	 * @StateTransitions
	 * {@link Rhapsody.sharedMems.ArrivalTerminalExit#goHome} puts instance in {@link PassengerState#EXIT_ARRIVAL_TERMINAL} state.
	 */
	AT_LUGGAGE_RECLAIM ("LGR"),
	/**
	 * Blocking state with eventual transition (final state)<p/>
	 * The passenger is waken up by the the last Passenger of each flight to exit the arrival terminal or to enter the departure terminal.
	 */
	EXIT_ARRIVAL_TERMINAL ("EAT"),
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
	ARRIVING_TRANSFER_TERMINAL ("ATT"),
	/**
	 * Blocking state
	 * <p/>
	 * The passenger is waken up by the Bus Driver.
	 * <p/>
	 * @StateTransitions
	 * StateTransitions
	 * {@link Rhapsody.sharedMems.DepartureTerminalTransfer#leaveTheBus} puts instance in {@link PassengerState#DEPARTING_TRANSFER_TERMINAL}.
	 */
	TERMINAL_TRANSFER ("TTR"),
	/**
	 * Transition state
	 * <p/>
	 * StateTransitions
	 * {@link Rhapsody.sharedMems.DepartureTerminalEntrance#prepareNextLeg} puts instance in {@link PassengerState#DEPARTING}.
	 */
	DEPARTING_TRANSFER_TERMINAL ("DTT"),
	/**
	 * Blocking state with eventual transition (final state)
	 * The passenger is waken up by the last Passenger of each flight to exit the arrival terminal or to enter the departure terminal.
	 */
    DEPARTING ("DEP"),
    
    /**
     * Porter States
     */

     /**
	 * Blocking state (initial / final state) <p/>
	 * The porter is waken up by the last of the passengers to reach the arrival lounge.
	 * @StateTransitions
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.Lounge#takeARest} keeps instance on the same state.
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#tryToCollectABag} or {@link Rhapsody.sharedMems.StoreRoom#tryToCollectABag} puts instante in {@link PorterState#AT_THE_PLANES_HOLD} state. 
	 * </ul>
	 */
	WAITING_FOR_PLANE_TO_LAND ("WPTL"),
	/**
	 * Transition state <p/>
	 * The porter is getting some baggages. <p/>
	 * @StateTransitions
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#noMoreBagsToCollect} or {@link Rhapsody.sharedMems.StoreRoom#noMoreBagsToCollect} puts instante in {@link PorterState#WAITING_FOR_PLANE_TO_LAND} state.
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#carryItToAppropriateStore} puts instante in {@link PorterState#AT_THE_LUGGAGE_BELT_CONVEYOR} state if baggage claimed. 
	 * <li>{@link Rhapsody.sharedMems.StoreRoom#carryItToAppropriateStore} puts instante in {@link PorterState#AT_THE_STOREROOM} state if baggage not claimed.
	 * </ul>
	 */
	AT_THE_PLANES_HOLD ("ATPH"),
	/**
	 * Transition state <p/>
	 * The porter is giving some baggages. <p/>
	 * @StateTransitions
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.BaggageCollectionPoint#tryToCollectABag} puts instante in {@link PorterState#AT_THE_PLANES_HOLD} state.
	 * </ul>
	 */
	AT_THE_LUGGAGE_BELT_CONVEYOR ("ATCB"),
		/**
	 * Transition state <p/>
	 * The porter is giving some baggages. <p/>
	 * @StateTransitions
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.StoreRoom#tryToCollectABag} puts instante in {@link PorterState#AT_THE_PLANES_HOLD} state.
	 * </ul>
	 */
	AT_THE_STOREROOM ("ATSR");

    private final String state;
	private States(String state) {
		this.state=state;
	}

	@Override
	public String toString(){
		return this.state;
	}
    
}