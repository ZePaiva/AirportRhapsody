package Rhapsody.utils;

/**
 * Luggage class to hel porter pick the correct place where to put
 * 
 */
public class Luggage {

    /**
     * Passenger Id to who this luggage belongs
     */
    private final int passengerId;

    /**
     * Luggage type, varies with passenger <p/>
     * Can be TRT or FDT
     */
    private final String luggageType;

    /**
     * Luggage constructor
     * @param passengerId
     * @param passengerType
     */
    public Luggage(int passengerId, String passengerType) {
        assert(passengerType.equals("FDT") || passengerType.equals("TRT"));
        this.passengerId=passengerId;
        this.luggageType=passengerType;
    }

    /**
     * Returns the Id of the passenger whose this luggage belongs
     * @return passenger id of type int
     */
    public int getPassengerId() {
        return this.passengerId;
    }

    /**
     * Returns type of luggage
     * @return luggage type of type string
     */
    public String getLuggageType() {
        return this.luggageType;
    }

    /**
     * toString method to return luggage in String
     */
    @Override
    public String toString() {
        return "{" +
            " passengerId='" + getPassengerId() + "'" +
            ", luggageType='" + getLuggageType() + "'" +
            "}";
    }

}