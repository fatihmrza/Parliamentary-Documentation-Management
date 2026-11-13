package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.Log;
import de.bundestag.dokumente.Person;
import de.bundestag.dokumente.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Dokumentenverwaltung {

    private static Map<Integer, Dokument> docs = new HashMap<>();
    public Dokumentenverwaltung(){

    }


    public static Map<Integer, Dokument> getDocuments(){
        return docs;
    }

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

    public void saveDocuments(DokumentSpeicher speicherInstanz, String filePath, User user) throws IOException {
        speicherInstanz.saveDocuments(docs, filePath, user);
    }

    public void loadDocuments(DokumentSpeicher speicherInstanz, String filePath, User user) throws IOException{
        Map<Integer, Dokument> docs = speicherInstanz.loadDocuments(filePath, user);
        Dokumentenverwaltung.docs = docs;
    }

    public void printLogs(){
        for (Dokument doc : docs.values()){
            System.out.println("Document ID: " + doc.getId());
            for (Log log : doc.getLogs()){
                System.out.println(log);
            }
        }
    }

    private Dokument findDocumentById(int id){
        if (!(docs.containsKey(id))){
            throw new IllegalArgumentException("There is no document with this id.");
        }
        return docs.get(id);

    }

}
