package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.Log;
import de.bundestag.dokumente.User;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * abstract class Dokument to defining Document attributes and some other common methods.
 * @author Fatih Mirza
 */
public abstract class Dokument {
    private final int id;
    private String title;
    private String content;
    private LocalDate date;
    private  ArrayList<Log> logs = new ArrayList<>();

    /**
     * constructor to inheriting attributes onto underclasses.
     * @param id
     * @param title
     * @param content
     * @param date
     */
    public Dokument(int id, String title, String content, LocalDate date){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;

    }

    /**
     * getter method for id.
     * @return int
     */
    public int getId(){
        return this.id;
    }

    /**
     * getter method for Title.
     * @return String
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * getter method for content.
     * @return String
     */
    public String getContent(){
        return this.content;
    }

    /**
     * getter method for date of document.
     * @return LocalDate
     */
    public LocalDate getDate(){
        return this.date;
    }

    /**
     * getter method for all logs of a document instance.
     * @return ArrayList<Log>
     */
    public ArrayList<Log> getLogs(){
        return this.logs;
    }

    /**
     * protected method for changing content only by Dokumentenverwaltung class.
     * @param newContent
     * @param user
     */
    protected void changeContent(String newContent, User user){
        Log newcont = Log.createLog("content", this.content, newContent, user, LocalDateTime.now());
        this.logs.add(newcont);
        this.content = newContent;
    }

    /**
     * protected method for changing title only by Dokumentenverwaltung class.
     * @param newTitle
     * @param user
     */
    protected void changeTitle(String newTitle, User user){
        Log newtitle = Log.createLog("title", this.title, newTitle, user, LocalDateTime.now());
        this.logs.add(newtitle);
        this.title = newTitle;
    }

    /**
     * protected method for changing date only by Dokumentenverwaltung class.
     * @param newDate
     * @param user
     */
    protected void changeDate(LocalDate newDate, User user){
        Log newdate = Log.createLog("date", this.date.toString(), newDate.toString(), user, LocalDateTime.now());
        this.logs.add(newdate);
        this.date = newDate;
    }

    /**
     * protected method for adding logs from underclasses.
     * @param log
     */
    protected void addLog(Log log){
        this.logs.add(log);
    }  // I added this line to preventing to inherit logs list attribute directly, so that it can be changed or deleted from subclasses.
       // I thought it would be robuster and more professional.

    /**
     * restoring all logs by loading documents in JsonDocumentSpeicher.
     * @param logs
     */
    protected void restoreLog(ArrayList<Log> logs){
        this.logs = logs;
    }

    /**
     * abstract getter method for getting document type by overriding it in underclasses.
     * @return String
     */
    public abstract String getDocumentType();


}

