package Rhapsody.common;

/**
 * Message types  
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public enum MessageType {

    /**
     * Porter related message types
     */
    PORTER_STORE_BAG_SR,
    PORTER_STORE_BAG_CB,
    PORTER_COLLECT_BAG,
    PORTER_NO_MORE_BAGS,
    PORTER_WAITING,

    /**
     * Bus Driver related message types
     */
    PASSENGERS_WAITING,
    BUS_FULL,

    /**
     * Passenger related message types
     */
    PASSENGER_ARRIVED,
    PASSENGER_GOING_HOME,
    PASSENGER_IN,

    /**
     * Shared memories related message types
     */
    DEPARTURE_SYNCH,
    DEPARTURE_REQUEST_HOWMANY,
    DEPARTURE_REQUEST_WAKEUP,
    ;
}