package de.bundestag.dokumente.dokument;
import java.time.LocalDate;
import de.bundestag.dokumente.Person;

public class DokumentFactory {

    private DokumentFactory(){

    } // to prevent to creating instances for this class i set it as private modificator.

    protected static Rede createRede(int id, String title, String content, LocalDate date, Person speaker){
        return new Rede(id, title, content, date, speaker);
    }

    protected static Protokoll createProtokoll(int id, int sessionNumber, String title, String content, LocalDate date){
        return new Protokoll(id, sessionNumber, title, content, date);
    }

    protected static Drucksache createDrucksache(int id, String title, String content, LocalDate date){
        return new Drucksache(id, title, content, date);
    }

}
