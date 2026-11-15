package de.bundestag.dokumente.dokument;
import java.time.LocalDate;
import de.bundestag.dokumente.Person;

/**
 * public class to remoting to create classes.
 */
public class DokumentFactory {

    private DokumentFactory(){

    } // to prevent to creating instances for this class i set it as private modificator.

    /**
     * static method for creating Rede instance.
     * @param id
     * @param title
     * @param content
     * @param date
     * @param speaker
     * @return Rede
     */
    protected static Rede createRede(int id, String title, String content, LocalDate date, Person speaker){
        return new Rede(id, title, content, date, speaker);
    }

    /**
     * static method for creating Protokoll instance.
     * @param id
     * @param sessionNumber
     * @param title
     * @param content
     * @param date
     * @return Protokoll
     */
    protected static Protokoll createProtokoll(int id, int sessionNumber, String title, String content, LocalDate date){
        return new Protokoll(id, sessionNumber, title, content, date);
    }

    /**
     * static method for creating Drucksache instances.
     * @param id
     * @param title
     * @param content
     * @param date
     * @return Drucksache
     */
    protected static Drucksache createDrucksache(int id, String title, String content, LocalDate date){
        return new Drucksache(id, title, content, date);
    }

}
