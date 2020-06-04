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
    PASSENGER_IN,
    PASSENGER_OUT,
    PORTER_STORE_BAG_SR,
    PORTER_STORE_BAG_CB,
    PORTER_COLLECT_BAG,
    PORTER_NO_MORE_BAGS,
    PORTER_WAITING,

    /**
     * Bus Driver related message types
     */
    PASSENGERS_WAITING,
    BUS_FULL;
}