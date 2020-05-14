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
     * 
     * @param id entity ID
     */
    public void setValidID(boolean validID) {
        this.validID = validID;
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
     * 
     * @param validState
     */
    public void setValidState(boolean validState) {
        this.validState = validState;
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
     * 
     * @param validType
     */
    public void setValidType(boolean validType) {
        this.validType = validType;
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
     * 
     * @param int1
     */
    public void setValidInt1(boolean validInt1) {
        this.validInt1 = validInt1;
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
     * 
     * @param int2
     */
    public void setValidInt2(boolean validInt2) {
        this.validInt2 = validInt2;
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
     * 
     * @param int3
     */
    public void setValidInt3(boolean validInt3) {
        this.validInt3 = validInt3;
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
     * Set first boolean value
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
     * 
     * @param validBool1
     */
    public void setValidBool1(boolean validBool1) {
        this.validBool1 = validBool1;
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
     * Set second boolean value
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
     * 
     * @param validBool2
     */
    public void setValidBool2(boolean validBool2) {
        this.validBool2 = validBool2;
    }
}