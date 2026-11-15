package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.Log;
import de.bundestag.dokumente.Person;
import de.bundestag.dokumente.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * public underclass for creating protokoll document.
 * @author Fatih Mirza 8500551
 */
public class Protokoll extends Dokument{
    private final int sessionNumber;
    private ArrayList<Rede> speeches = new ArrayList<Rede>();
    private static final String DOCUMENT_TYPE = "Protokoll";

    /**
     * protected constructor for creating protokoll instances from factory class.
     * @param id
     * @param sessionNumber
     * @param title
     * @param content
     * @param date
     */
    protected Protokoll(int id, int sessionNumber, String title, String content, LocalDate date){
        super(id, title, content, date);
        this.sessionNumber = sessionNumber;
    }

    /**
     * getter method for sessionNumber.
     * @return int
     */
    public int getSessionNumber(){
        return this.sessionNumber;
    }

    /**
     * protected method for adding speeches from verwaltung class.
     * @param newRede
     * @param user
     */
    protected void addSpeeches(Rede newRede, User user) {
        List<Integer> ids = new ArrayList<>();
        for (Rede speech : speeches) {
            ids.add(speech.getId());
        }
        String oldValue = ids.toString(); // To transforming array list to String with id's.
        this.speeches.add(newRede);

        ids.clear();
        for (Rede speech : speeches) {
            ids.add(speech.getId());
        }
        String newValue = ids.toString();
        Log newSpeech = Log.createLog("speeches", oldValue, newValue, user, LocalDateTime.now());
        this.addLog(newSpeech);


    }

    /**
     * getter Method for getting all speeches in an array list.
     * @return ArrayList<Rede>
     */
    public ArrayList<Rede> getSpeeches(){
            return this.speeches;
        }

    /**
     * Overriding getter method for document type to getting document type of an instance.
     * @return String
     */
    @Override
        public String getDocumentType(){
            return Protokoll.DOCUMENT_TYPE;
        }

    /**
     * Overriding toString method to printing class instances with their attributes.
     * @return String
     */
        @Override
        public String toString(){
            return ("Document type: " + Protokoll.DOCUMENT_TYPE + " " + "Document id: " + this.getId() + " " + "Session number: " + this.sessionNumber + " " +
                    "title: " + this.getTitle() + " " + "Date created: " + this.getDate() + " " + "Number of change on document: " + this.getLogs().size() + "\n" + "Overview to speeches: " + this.getSpeeches().toString());
        }

    /**
     * protected method for restoring all speeches after loading the document.
     * @param restoredSpeeches
     */
    protected void restoreSpeeches(ArrayList<Rede> restoredSpeeches){
            this.speeches = restoredSpeeches;
        }
}


