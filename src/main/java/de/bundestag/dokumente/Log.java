package de.bundestag.dokumente;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class Log {
    private final String fieldName;
    private final String oldValue;
    private final String newValue;
    private final User user;
    private final LocalDateTime timestamp;

    private  Log(String fieldName, String oldValue, String newValue,
                 User user, LocalDateTime timestamp){
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.user = user;
        this.timestamp = timestamp;
    }

    public String getFieldName(){
        return this.fieldName;
    }

    public String getOldValue(){
        return this.oldValue;
    }

    public String getNewValue(){
        return this.newValue;
    }

    public User getUser(){
        return this.user;
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }

    public static Log createLog(String fieldName, String oldValue, String newValue,
                                User user, LocalDateTime timestamp){
        return new Log(fieldName, oldValue, newValue, user, timestamp);
    }
    @Override
    public String toString(){
        return ("Changed field: "+ this.fieldName + " " + "Previous input: "+ this.oldValue + " "+
                "New input: " + this.newValue + " " + "Editor: " + this.user.getId() + " "
        +"Time of change: " + this.timestamp);
    }
}
