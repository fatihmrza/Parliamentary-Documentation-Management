package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.Log;
import de.bundestag.dokumente.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Dokument {
    private final int id;
    private String title;
    private String content;
    private LocalDate date;
    private final ArrayList<Log> logs = new ArrayList<Log>();

    public Dokument(int id, String title, String content, LocalDate date){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;

    }
    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return this.title;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public ArrayList<Log> getLogs(){
        return this.logs;
    }

    protected void changeContent(String new_content, User user){
        Log newcont = Log.createLog("content", this.content, new_content, user, LocalDateTime.now());
        this.logs.add(newcont);
        this.content = new_content;
    }

    protected void changeTitle(String new_title, User user){
        Log newtitle = Log.createLog("title", this.title, new_title, user, LocalDateTime.now());
        this.logs.add(newtitle);
        this.title = new_title;
    }

    protected void changeDate(LocalDate new_date, User user){
        Log newdate = Log.createLog("date", this.date.toString(), new_date.toString(), user, LocalDateTime.now());
        this.logs.add(newdate);
        this.date = new_date;
    }

    protected void addLog(Log log){
        this.logs.add(log);
    }  // I added this line to preventing to inherit logs list attribute directly, so that it can be changed or deleted from subclasses.
       // I thought it would be robuster and more professional.

    public abstract String getDocumentType();


}

