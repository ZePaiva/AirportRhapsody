package Rhapsody.entities.states;

/**
 * Enum class with possible states for the Porter class
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public enum PorterState {

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

	private PorterState(String state) {
		this.state=state;
	}

	@Override
	public String toString(){
		return this.state;
	}
}