package de.bundestag.dokumente;


import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * public class for defining rules to creating a log data.
 * @author Fatih Mirza 8500551
 */
public class Log {
    private final String fieldName;
    private final String oldValue;
    private final String newValue;
    private final User user;
    private final LocalDateTime timestamp;

    /**
     * private constructor for Log class, which can be initialized through a factory method.
     * @param fieldName
     * @param oldValue
     * @param newValue
     * @param user
     * @param timestamp
     */
    private  Log(String fieldName, String oldValue, String newValue,
                 User user, LocalDateTime timestamp){
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.user = user;
        this.timestamp = timestamp;
    }

    /**
     * getter method for field name.
     * @return String
     */
    public String getFieldName(){
        return this.fieldName;
    }

    /**
     * getter method for old value.
     * @return String
     */
    public String getOldValue(){
        return this.oldValue;
    }

    /**
     * getter method for new value
     * @return String
     *
     */
    public String getNewValue(){
        return this.newValue;
    }

    /**
     * getter method for user.
     * @return User
     */
    public User getUser(){
        return this.user;
    }

    /**
     * getter method for timestamp.
     * @return LocalDateTime
     */
    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }

    /**
     * factory method for Log class to creating log intances.
     * @param fieldName
     * @param oldValue
     * @param newValue
     * @param user
     * @param timestamp
     * @return Log
     */
    public static Log createLog(String fieldName, String oldValue, String newValue,
                                User user, LocalDateTime timestamp){
        return new Log(fieldName, oldValue, newValue, user, timestamp);
    }

    /**
     * Overriding toString() Method so that class instances could be printed with their attributes.
     * @return String
     */
    @Override
    public String toString(){
        return ("Changed field: "+ this.fieldName + " " + "Previous input: "+ this.oldValue + " "+
                "New input: " + this.newValue + " " + "Editor: " + this.user.getId() + " "
        +"Time of change: " + this.timestamp);
    }
}
