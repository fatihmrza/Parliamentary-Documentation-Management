package de.bundestag.dokumente.dokument;
import de.bundestag.dokumente.Person;
import de.bundestag.dokumente.User;
import de.bundestag.dokumente.Log;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Rede extends Dokument {
    private Person speaker;
    private static final String DOCUMENT_TYPE = "Rede";

    public Rede(int id, String title, String content, LocalDate date,Person speaker){
        super(id, title, content, date);
        this.speaker = speaker;

    }
    public String getSpeakerName(){
        return this.speaker.getName();
    }
    protected void changeSpeaker(Person new_speaker, User user){
        Log newspeak = Log.createLog("speaker", String.valueOf(this.speaker.getId()), String.valueOf(new_speaker.getId()), user, LocalDateTime.now());
        this.addLog(newspeak);
        this.speaker = new_speaker;
    }


    @Override
    public String getDocumentType(){
        return DOCUMENT_TYPE;
    }

    @Override
    public String toString(){
        return ("Document type: " + Rede.DOCUMENT_TYPE + " " + "Speaker: " + this.speaker.getId() + " " +
                "title: " + this.getTitle() + " " + "Date created: " + this.getDate() + " ");
    }

}
