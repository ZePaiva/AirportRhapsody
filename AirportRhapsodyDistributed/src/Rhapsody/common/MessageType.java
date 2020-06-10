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
    BD_HAS_ENDED,
    BD_ANNOUNCING_BOARDING,
    BD_ARRIVING,
    BD_DRIVING, 
    BD_PARKING,
    /**
     * Passenger related message types
     */
    PASSENGER_ARRIVED,
    PASSENGER_GOING_HOME,
    PASSENGER_IN,
    PASSENGER_INTO_BUS,
    PASSENGER_COLLECTING_BAG,
    PASSENGER_COMPLAINT,
    PASSENGER_NEXT_FLIGHT,
    PASSENGER_EXITING_BUS,
    /**
     * Shared memories related message types
     */
    DEPARTURE_SYNCH,
    DEPARTURE_REQUEST_HOWMANY,
    DEPARTURE_REQUEST_WAKEUP,
    SIM_ENDED,
    NEW_FLIGHT,
    ATE_SYNCH,
    ATE_REQUEST_HOWMANY,
    ATE_REQUEST_WAKEUP, 
    /**
     * Repository update messages
     */
    UP_PASS_STATE, 
    UP_BD_STATE, 
    UP_PORTER_STATE, 
    UP_PASS_IN_WAIT, 
    UP_PASS_OUT_WAIT, 
    UP_PASS_IN_BUS, 
    UP_PASS_OUT_BUS, 
    UP_PASS_IN_FLIGHT, 
    UP_PASS_OUT_FLIGHT, 
    UP_FLIGHT, 
    UP_FLIGHT_CLEAR, 
    P_BAG_PLANE, 
    UP_BAG_CB, 
    P_BAG_SR, 
    UP_PASS_SIT, 
    UP_PASS_SB, 
    UP_PASS_CB, 
    UP_FDT, 
    UP_TRT, 
    UP_BAG_PH, 
    UP_BAG_L, 
    UP_BAG_PLANE, 
    UP_BAG_SR, 
    LOG_MEM
    ;
}