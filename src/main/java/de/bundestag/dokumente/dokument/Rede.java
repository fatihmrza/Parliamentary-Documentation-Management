package de.bundestag.dokumente.dokument;
import de.bundestag.dokumente.Person;
import de.bundestag.dokumente.User;
import de.bundestag.dokumente.Log;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * public underclass for creating rede document.
 * @author Fatih Mirza 8500551
 */
public class Rede extends Dokument {
    private Person speaker;
    private static final String DOCUMENT_TYPE = "Rede";

    /**
     * protected constructor to creating rede instances by factory class.
      * @param id
     * @param title
     * @param content
     * @param date
     * @param speaker
     */
    protected Rede(int id, String title, String content, LocalDate date,Person speaker){
        super(id, title, content, date);
        this.speaker = speaker;

    }

    /**
     * getter method for speaker id.
     * @return int
     */
    public int getSpeakerId(){
        return this.speaker.getId();
    }

    /**
     * protected method for changing speaker from Dokumentenverwaltung.
     * @param newSpeaker
     * @param user
     */
    protected void changeSpeaker(Person newSpeaker, User user){
        Log newspeak = Log.createLog("speaker", String.valueOf(this.speaker.getId()), String.valueOf(newSpeaker.getId()), user, LocalDateTime.now());
        this.addLog(newspeak);
        this.speaker = newSpeaker;
    }

    /**
     * Overriding getter method for getting document type for a rede instances.
     * @return
     */
    @Override
    public String getDocumentType(){
        return DOCUMENT_TYPE;
    }

    /**
     * Overriding toString() to printing rede instances with its attributes.
     * @return String
     */
    @Override
    public String toString(){
        return ("Document type: " + Rede.DOCUMENT_TYPE + " " + "Document id: " + this.getId() + " " + "Speaker: " + this.speaker.getId() + " " +
                "title: " + this.getTitle() + " " + "Date created: " + this.getDate() + " " + "Number of change on document: " + this.getLogs().size());
    }

}
