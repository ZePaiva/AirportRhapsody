package Rhapsody.common;

import java.io.Serializable;

/**
 * TCP Packet data type for the message passing
 * This is the most important class for the message-apssing logic as it implements the messages structures
 * 
 * @author José Paiva
 * @author André Mourato
 * 
 * @version 1.0
 */
public class Packet implements Serializable {
    
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 2020L;
    
    /**
     * Entity ID
     * 
     * @serial id
     */
    private int id;

    /**
     * Entity ID is valid
     * 
     * @serial validID
     */
    private boolean validID;

    /**
     * Entity State
     * 
     * @serial state
     */
    private States state;

    /**
     * Entity state is valid
     * 
     * @serial validState
     */
    private boolean validState;

    /**
     * Packet type 
     * 
     * @serial type
     */
    private PacketType type;

    /**
     * Packer type is valid
     * 
     * @serial validType
     */
    private boolean validType;

    /**
     * First int value to be transported
     * 
     * @serial int1
     */
	private int int1;

	/**
     * First int value is valid
     * 
     * @serial validInt1
     */
	private boolean validInt1;

	/**
     * Second int value to be transported
     * 
     * @serial int2
     */
	private int int2;

	/**
     * Second int value is valid
     * 
     * @serial validInt2
     */
	private boolean validInt2;

	/**
     * Third int value to be transported
     * 
     * @serial int3
     */
	private int int3;

	/**
     * Third int value is valid
     * 
     * @serial validInt3
     */
	private boolean validInt3;

	/**
     * First boolean value to be transported
     * 
     * @serial bool1
     */
	private boolean bool1;

	/**
     * First boolean value is valid
     * 
     * @serial validBool1
     */
	private boolean validBool1;

	/**
     * Second boolean value to be transported
     * 
     * @serial boole2
     */
	private boolean bool2;

	/**
     * Second boolean value is valid
     * 
     * @serial validBool2
     */
    private boolean validBool2;

    /**
     * String value, useful for comunication of passenger type
     * 
     * @serial string1
     */
    private String string1;

    /**
     * String value validity
     * 
     * @serial validString1
     */
    private boolean validString1;

    /**
     * Integer array, useful to pass amount of luggages at start of passenger
     * 
     * @serial intArray1
     */
    private int[] intArray1;

    /**
     * Integer Array validity
     * 
     * @serial validIntArray1
     */
    private boolean validIntArray1;
    
    /** 
     * Packet instantiation
     */
    public Packet() {
        this.validID = false;
        this.validState = false;
        this.validType = false;
        this.validInt1 = false;
        this.validInt2 = false;
        this.validInt3 = false;
        this.validBool1 = false;
        this.validBool2 = false;
        this.validString1 = false;
        this.validIntArray1 = false;
    }

    /**
     * Return entity ID
     * 
     * @return entity ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set entity ID
     * 
     * @param id entity ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Return entity ID validity
     * 
     * @return entity ID is valid
     */
    public boolean getValidID() {
        return this.validID;
    }

    /**
     * Set entity ID validity
     */
    public void setValidID() {
        this.validID = true;
    }

    /**
     * Return entity state
     * 
     * @return entity state
     */
    public States getState() {
        return this.state;
    }

    /**
     * Set entity state
     * 
     * @param state entity current state
     */
    public void setState(States state) {
        this.state = state;
    }

    /**
     * Return entity state validity
     * 
     * @return entity state validity 
     */
    public boolean getValidState() {
        return this.validState;
    }

    /**
     * Set entity state validity 
     */
    public void setValidState() {
        this.validState = true;
    }

    /**
     * Set packet type, see PacketType for possible types
     * 
     * @return packet type
     */
    public PacketType getType() {
        return this.type;
    }

    /**
     * Get Packet type
     * 
     * @param type
     */
    public void setType(PacketType type) {
        this.type = type;
    }

    /**
     * Get type validity 
     * 
     * @return packet type validity 
     */
    public boolean getValidType() {
        return this.validType;
    }

    /**
     * Set packet type validity
     */
    public void setValidType() {
        this.validType = true;
    }

    /**
     * Get first integer value
     * 
     * @return first integer
     */
    public int getInt1() {
        return this.int1;
    }

    /**
     * Set first integer value
     * 
     * @param int1
     */
    public void setInt1(int int1) {
        this.int1 = int1;
    }

    /**
     * Get first integer value validity
     * 
     * @return integer 1 is valid
     */
    public boolean getValidInt1() {
        return this.validInt1;
    }

    /**
     * Set first integer value validity
     */
    public void setValidInt1() {
        this.validInt1 = true;
    }

    /**
     * Get second integer value
     * 
     * @return second integer
     */
    public int getInt2() {
        return this.int2;
    }

    /**
     * Set second integer value
     * 
     * @param int2
     */
    public void setInt2(int int2) {
        this.int2 = int2;
    }

    /**
     * Get second integer value validity
     * 
     * @return integer 1 is valid
     */
    public boolean getValidInt2() {
        return this.validInt2;
    }

    /**
     * Set second integer value validity
     */
    public void setValidInt2() {
        this.validInt2 = true;
    }

    /**
     * Get third integer value
     * 
     * @return third integer
     */
    public int getInt3() {
        return this.int3;
    }

    /**
     * Set third integer value
     * 
     * @param int3
     */
    public void setInt3(int int3) {
        this.int3 = int3;
    }

    /**
     * Get third integer value validity
     * 
     * @return integer 1 is valid
     */
    public boolean getValidInt3() {
        return this.validInt3;
    }

    /**
     * Set third integer value validity
     */
    public void setValidInt3() {
        this.validInt3 = true;
    }

    /**
     * Get first boolean value
     * 
     * @return first boolean
     */
    public boolean getBool1() {
        return this.bool1;
    }

    /**
     * Set first boolean to given value
     * 
     * @param bool1
     */
    public void setBool1(boolean bool1) {
        this.bool1 = bool1;
    }

    /**
     * Get first boolean value validity
     * 
     * @return boolean 1 validity
     */
    public boolean getValidBool1() {
        return this.validBool1;
    }

    /**
     * Set first boolean value validity
     */
    public void setValidBool1() {
        this.validBool1 = true;
    }

    /**
     * Get second boolean value
     * 
     * @return second boolean
     */
    public boolean getBool2() {
        return this.bool2;
    }

    /**
     * Set second boolean value to given value
     * 
     * @param bool2
     */
    public void setBool2(boolean bool2) {
        this.bool2 = bool2;
    }

    /**
     * Get second boolean value validity
     * 
     * @return boolean 1 validity
     */
    public boolean getValidBool2() {
        return this.validBool2;
    }

    /**
     * Set second boolean value validity
     */
    public void setValidBool2() {
        this.validBool2 = true;
    }

    /**
     * Get String sent, can be parsed as a string array (and will be)
     * 
     * @return sent String
     */
    public String getString1() {
        return this.string1;
    }

    /**
     * Set string to be sent, will be parsed as a CSV
     * 
     * @param string1 outgoing string value
     */
    public void setString1(String string1) {
        this.string1 = string1;
    }

    /**
     * Get validity of string1 field
     * 
     * @return string1 validity
     */
    public boolean getValidString1() {
        return this.validString1;
    }

    /**
     * Set string1 validity
     */
    public void setValidString1() {
        this.validString1 = true;
    }

    /**
     * Get intArray value
     * 
     * @return array
     */
    public int[] getIntArray1() {
        return this.intArray1;
    }

    /**
     * set intArray value to be sent
     * 
     * @param intArray1
     */
    public void setIntArray1(int[] intArray1) {
        this.intArray1 = intArray1;
    }

    /**
     * Get validity of intArray1 field
     * 
     * @return
     */
    public boolean getValidIntArray1() {
        return this.validIntArray1;
    }

    /**
     * Set validity of intArray field
     */
    public void setValidIntArray1() {
        this.validIntArray1 = true;
    }

}