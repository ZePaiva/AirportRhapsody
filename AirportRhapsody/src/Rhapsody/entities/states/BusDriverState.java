package Rhapsody.entities.states;

/**
 * Enum class with possible states for the BusDriver class
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
	 * <ul>
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalTransfer#hasDaysWorkEnded} keeps instance on the same state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalTransfer#announcingBusBoarding} keeps instance on the same state.
	 * <li>{@link Rhapsody.sharedMems.ArrivalTerminalTransfer#goToDepartureTerminal} puts instante in {@link BusDriverState#DRIVING_FORWARD} state. 
	 * </ul>
	 */
	PARKING_AT_THE_ARRIVAL_LOUNGE ("PKAL"),
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
	DRIVING_BACKWARD ("DRBW");

	private final String state;
	private BusDriverState(String state) {
		this.state=state;
	}

	@Override
	public String toString(){
		return this.state;
	}
}