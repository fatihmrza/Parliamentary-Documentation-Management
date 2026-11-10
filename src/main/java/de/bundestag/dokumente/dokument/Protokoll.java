package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.Log;
import de.bundestag.dokumente.Person;
import de.bundestag.dokumente.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Protokoll extends Dokument{
    private final int sessionNumber;
    private ArrayList<Rede> speeches = new ArrayList<Rede>();
    private static final String DOCUMENT_TYPE = "Protokoll";

    public Protokoll(int id, int sessionNumber, String title, String content, LocalDate date){
        super(id, title, content, date);
        this.sessionNumber = sessionNumber;
    }

    public int getSessionNumber(){
        return this.sessionNumber;
    }

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
        public ArrayList<Rede> getSpeeches(){
            return this.speeches;
        }

        @Override
        public String getDocumentType(){
            return Protokoll.DOCUMENT_TYPE;
        }

        @Override
        public String toString(){
            return ("Document type: " + Protokoll.DOCUMENT_TYPE + " " + "Document id: " + this.getId() + " " + "Session number: " + this.sessionNumber + " " +
                    "title: " + this.getTitle() + " " + "Date created: " + this.getDate() + " " + "Number of change on document: " + this.getLogs().size());
        }
}


