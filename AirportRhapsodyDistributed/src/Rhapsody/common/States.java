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
	 * Double blocking state (initial / final state)
	 * <p/>
	 * 
	 * The driver is waken up the first time by the Passenger who arrives at the
	 * transfer terminal and finds out it's place in the waiting queue equals the
	 * bus capacity, or when the departure time has been reached<br/>
	 * The driver is waken up the second time by the last passenger to enter the bus
	 * <p/>
	 */
	PARKING_AT_THE_ARRIVAL_LOUNGE("PKAT"),
	/**
	 * Transition state
	 * <p/>
	 * 
	 * Used for when driver is transfering passengers to departure terminal
	 * <p/>
	 */
	DRIVING_FORWARD("DRFW"),
	/**
	 * Blocking state
	 * <p/>
	 * The driver is waken up by the last passenger to exit the bus.
	 * <p/>
	 */
	PARKING_AT_THE_DEPARTURE_TERMINAL("PKDT"),
	/**
	 * Transition state
	 * <p/>
	 * Used for when driver is gooing back the to arrival lounge
	 * <p/>
	 */
	DRIVING_BACKWARD("DRBW"),

	/**
	 * Passenger States
	 */

	/**
	 * Transition state (initial / final state)
	 * <p/>
	 * Passenger just arrived to the airport, either disembarked or to embark.
	 * <p/>
	 * 
	 */
	AT_DISEMBARKING_ZONE("DEZ"),
	/**
	 * Blocking state with eventual transition.
	 * <p/>
	 * The Passenger is waken up by the Porter when he places on the conveyor belt
	 * an owned bag, or when the Porter signals that there are no more pieces of
	 * luggage in the plane hold and makes a transition when either the Passenger
	 * possess all the owned baggages, or was signaled that there are no more bags
	 * in the plane hold.
	 * <p/>
	 */
	AT_LUGGAGE_COLLECTION("LGC"),
	/**
	 * Transition state
	 * <p/>
	 * Passenger is at Luggage reclaim office
	 * <p/>
	 */
	AT_LUGGAGE_RECLAIM("LGR"),
	/**
	 * Blocking state with eventual transition (final state)
	 * <p/>
	 * The passenger is waken up by the the last Passenger of each flight to exit
	 * the arrival terminal or to enter the departure terminal.
	 */
	EXIT_ARRIVAL_TERMINAL("EAT"),
	/**
	 * Blocking state
	 * <p/>
	 * Before blocking, the Passenger wakes up the bus driver if the Passenger's
	 * place in the waiting queue equals the bus capacity.
	 * <p/>
	 * The Passenger is woken up by the Bus Driver to entry in the bus.
	 * <p/>
	 * 
	 */
	ARRIVING_TRANSFER_TERMINAL("ATT"),
	/**
	 * Blocking state
	 * <p/>
	 * The passenger is waken up by the Bus Driver.
	 * <p/>
	 */
	TERMINAL_TRANSFER("TTR"),
	/**
	 * Transition state
	 * <p/>
	 */
	DEPARTING_TRANSFER_TERMINAL("DTT"),
	/**
	 * Blocking state with eventual transition (final state) The passenger is waken
	 * up by the last Passenger of each flight to exit the arrival terminal or to
	 * enter the departure terminal.
	 */
	DEPARTING("DEP"),

	/**
	 * Porter States
	 */

	/**
	 * Blocking state (initial / final state)
	 * <p/>
	 * The porter is waken up by the last of the passengers to reach the arrival
	 * lounge.
	 * 
	 */
	WAITING_FOR_PLANE_TO_LAND("WPTL"),
	/**
	 * Transition state
	 * <p/>
	 * The porter is getting some baggages.
	 * <p/>
	 * 
	 */
	AT_THE_PLANES_HOLD("ATPH"),
	/**
	 * Transition state
	 * <p/>
	 * The porter is giving some baggages.
	 * <p/>
	 */
	AT_THE_LUGGAGE_BELT_CONVEYOR("ATCB"),
	/**
	 * Transition state
	 * <p/>
	 * The porter is giving some baggages.
	 * <p/>
	 * 
	 */
	AT_THE_STOREROOM("ATSR");

	private final String state;

	private States(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return this.state;
	}

}