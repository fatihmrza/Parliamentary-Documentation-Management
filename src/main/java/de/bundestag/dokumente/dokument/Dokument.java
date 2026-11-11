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
    private final ArrayList<Log> logs = new ArrayList<>();

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

    protected void changeContent(String newContent, User user){
        Log newcont = Log.createLog("content", this.content, newContent, user, LocalDateTime.now());
        this.logs.add(newcont);
        this.content = newContent;
    }

    protected void changeTitle(String newTitle, User user){
        Log newtitle = Log.createLog("title", this.title, newTitle, user, LocalDateTime.now());
        this.logs.add(newtitle);
        this.title = newTitle;
    }

    protected void changeDate(LocalDate newDate, User user){
        Log newdate = Log.createLog("date", this.date.toString(), newDate.toString(), user, LocalDateTime.now());
        this.logs.add(newdate);
        this.date = newDate;
    }

    protected void addLog(Log log){
        this.logs.add(log);
    }  // I added this line to preventing to inherit logs list attribute directly, so that it can be changed or deleted from subclasses.
       // I thought it would be robuster and more professional.

    public abstract String getDocumentType();


}

