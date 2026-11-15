package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.Log;
import de.bundestag.dokumente.Person;
import de.bundestag.dokumente.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * public management class to managing all methods by checking user permission to creating, editing, saving and loading documents.
 * @author Fatih Mirza 8500551
 */
public class Dokumentenverwaltung {

    private static Map<Integer, Dokument> docs = new HashMap<>();
    public Dokumentenverwaltung(){

    }

    /**
     * getter method for getting document hash map.
     * @return Map<Integer, Dokument>
     */
    public static Map<Integer, Dokument> getDocuments(){
        return docs;
    }

    /**
     * public method for creating Rede instances by checking user permission.
     * @param id
     * @param title
     * @param content
     * @param date
     * @param speaker
     * @param user
     * @return Rede
     */
    public Rede createRede(int id, String title, String content, LocalDate date, Person speaker, User user) {
        try {
            user.checkChangePermissions();
            if (!docs.containsKey(id)) {
                Rede newRede = DokumentFactory.createRede(id, title, content, date, speaker);
                docs.put(newRede.getId(), newRede);
                return newRede;
            }
            else {
                throw new IllegalArgumentException("There is already a document with this id.");
            }
        } catch (SecurityException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * public method for creating protokoll instances by checking user permission.
     * @param id
     * @param sessionNumber
     * @param title
     * @param content
     * @param date
     * @param user
     * @return Protokoll
     */
    public Protokoll createProtokoll(int id, int sessionNumber, String title, String content, LocalDate date, User user){
        try {
            user.checkChangePermissions();
            if (!docs.containsKey(id)) {
                Protokoll newProtokoll = DokumentFactory.createProtokoll(id, sessionNumber, title, content, date);
                docs.put(newProtokoll.getId(), newProtokoll);
                return newProtokoll;
            }
            else {
                throw new IllegalArgumentException("There is already a document with this id.");
            }
        }
        catch (SecurityException ex){
            System.out.println(ex.getMessage());
            return null;
        }

    }

    /**
    * public method for creating Drucksache instances by checking user permission.
     * @param id
     * @param title
     * @param content
     * @param date
     * @param user
     * @return
     */
    public Drucksache createDrucksache(int id, String title, String content, LocalDate date, User user){
        try {
            user.checkChangePermissions();
            if (!docs.containsKey(id)) {
                Drucksache newDrucksache = DokumentFactory.createDrucksache(id, title, content, date);
                docs.put(newDrucksache.getId(), newDrucksache);
                return newDrucksache;
            }
            else {
                throw new IllegalArgumentException("There is already a document with this id.");
            }
        }
        catch (SecurityException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * public method for changing content of a document by checking user permission.
     * @param id
     * @param newContent
     * @param user
     */
    public void changeContent(int id, String newContent, User user){
        try {
            user.checkChangePermissions();
            Dokument docInstance = findDocumentById(id);
            docInstance.changeContent(newContent, user);


        }
        catch (SecurityException ex){
            System.out.println(ex.getMessage());
        }

    }

    /**
     * public method for changing title of a document by checking user permission.
     * @param id
     * @param newTitle
     * @param user
     */
    public void changeTitle(int id, String newTitle, User user){
        try {
            user.checkChangePermissions();
            Dokument docInstance = findDocumentById(id);
            docInstance.changeTitle(newTitle, user);

            }
        catch (SecurityException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * public method for changing date of a document by checking user permission.
     * @param id
     * @param newDate
     * @param user
     */
    public void changeDate(int id, LocalDate newDate, User user){
        try {
            user.checkChangePermissions();
            Dokument docInstance = findDocumentById(id);
            docInstance.changeDate(newDate, user);

        }
        catch (SecurityException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * public method for changing speaker of a Rede document by checking user permission and if there is a document with this id.
     * @param id
     * @param newSpeaker
     * @param user
     */
    public void changeSpeaker(int id, Person newSpeaker, User user){
        try {
            user.checkChangePermissions();
            if (docs.containsKey(id)){
                if (docs.get(id) instanceof Rede){
                    Rede redeInstance = (Rede) docs.get(id);  //Type convention for reaching out changeSpeaker() method.
                    redeInstance.changeSpeaker(newSpeaker, user);

                }
                else {throw new IllegalArgumentException("Entered id ist not a Rede document.");
                }
            }
            else {throw new IllegalArgumentException("There is no document with this id.");
            }
        }
        catch (SecurityException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * public method for adding speeches in a Protokoll document by checking user permission and if there is a document with this id.
     * @param id
     * @param newRede
     * @param user
     */
    public void addSpeeches(int id, Rede newRede, User user){
        try{
            user.checkChangePermissions();
            if (docs.containsKey(id)){
                if (docs.get(id) instanceof Protokoll){
                    Protokoll protokollInstance = (Protokoll) docs.get(id);
                    if (!protokollInstance.getSpeeches().contains(newRede)){
                        protokollInstance.addSpeeches(newRede, user);
                    }
                    else { throw new IllegalArgumentException("The speech is already existing.");
                    }
                }
                else { throw new IllegalArgumentException("Entered id ist not a Protokoll document.");

                }
            }
            else { throw new IllegalArgumentException("There is no document with this id.");

            }
        }
        catch (SecurityException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * public method to saving document which is managed from this class.
     * @param speicherInstanz
     * @param filePath
     * @param user
     * @throws IOException
     */
    public void saveDocuments(DokumentSpeicher speicherInstanz, String filePath, User user) throws IOException {
        speicherInstanz.saveDocuments(docs, filePath, user);
    }

    /**
     * public method to updating loaded document.
     * @param speicherInstanz
     * @param filePath
     * @param user
     * @throws IOException
     */
    public void loadDocuments(DokumentSpeicher speicherInstanz, String filePath, User user) throws IOException{
        Map<Integer, Dokument> docs = speicherInstanz.loadDocuments(filePath, user);
        Dokumentenverwaltung.docs = docs;
    }

    /**
     * public method for printing logs.
     */
    public void printLogs(){
        for (Dokument doc : docs.values()){
            System.out.println("Document ID: " + doc.getId());
            for (Log log : doc.getLogs()){
                System.out.println(log);
            }
        }
    }

    /**
     * private helper method for finding document by id of the document.
     * @param id
     * @return Dokument
     */
    private Dokument findDocumentById(int id){
        if (!(docs.containsKey(id))){
            throw new IllegalArgumentException("There is no document with this id.");
        }
        return docs.get(id);

    }

}
