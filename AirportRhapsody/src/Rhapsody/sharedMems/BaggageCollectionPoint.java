package Rhapsody.sharedMems;

import Rhapsody.utils.Logger;
import Rhapsody.utils.Luggage;

/**
 * Conveyor Belt shared memory entity
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BaggageCollectionPoint{

    /**
     * Logger for debugging purposes
     */
    private Logger logger;

    /**
     * Amount of bags in the conveyor belt
     */
    private Luggage[] bagsInConveyorBelt;

    /**
     * Constructor of Baggage collection point
     * @param logger
     */
    public BaggageCollectionPoint(Logger logger) {
        this.logger = logger;
    }



	
}