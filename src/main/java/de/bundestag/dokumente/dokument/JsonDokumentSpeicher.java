package de.bundestag.dokumente.dokument;

import java.io.File;
import java.io.IOException;
import java.rmi.ServerError;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.bundestag.dokumente.Person;
import de.bundestag.dokumente.User;
import de.bundestag.dokumente.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import utilities.FileUtilities;

public class JsonDokumentSpeicher implements DokumentSpeicher {

    public JsonDokumentSpeicher() {

    }

    public void saveDocuments(Map<Integer, Dokument> documents, String filePath, User user) throws IOException {
        try {
            user.checkChangePermissions();
            JSONArray jsonDocuments = new JSONArray();
            for (Dokument doc : documents.values()) {
                JSONObject jsonDocument = new JSONObject();
                jsonDocument.put("type", doc.getDocumentType()); // we store type of document as well to getting document easily classified by loading.
                jsonDocument.put("id", doc.getId());
                jsonDocument.put("title", doc.getTitle());
                jsonDocument.put("content", doc.getContent());
                jsonDocument.put("date", doc.getDate().toString());
                JSONArray logs = new JSONArray();
                for (Log log : doc.getLogs() ){
                    JSONObject jsonLogObject = new JSONObject();
                    jsonLogObject.put("fieldname", log.getFieldName());
                    jsonLogObject.put("oldvalue", log.getOldValue());
                    jsonLogObject.put("newvalue", log.getNewValue());
                    jsonLogObject.put("editor_id", log.getUser().getId());
                    jsonLogObject.put("time_of_change", log.getTimestamp().toString());
                    logs.put(jsonLogObject);
                }
                jsonDocument.put("logs", logs);
                if (checkRede(doc)) {
                    Rede rede = (Rede) doc;
                    jsonDocument.put("speaker id", rede.getSpeakerId());
                } else if (checkProtokoll(doc)) {
                    Protokoll protokoll = (Protokoll) doc;
                    jsonDocument.put("session number", protokoll.getSessionNumber());
                    ArrayList<Integer> speechIds = new ArrayList<>();
                    for (Rede speech : protokoll.getSpeeches()) {
                        speechIds.add(speech.getId());
                    }
                    jsonDocument.put("speech id's", speechIds);
                    jsonDocument.put("number of speech", speechIds.size());

                }
                jsonDocuments.put(jsonDocument);

            }
            File file = new File(filePath);
            FileUtilities.writeContent(file, jsonDocuments.toString(2)); // this parameter 2 was recommended to me by asking to google.
        }
        catch (SecurityException ex){
            throw new SecurityException("User has no permission.");
        }



    }
    public Map<Integer, Dokument> loadDocuments(String filePath, User user) throws IOException{
        try {
            user.checkChangePermissions();
            File file = new File(filePath);
            Map<Integer, Dokument> documents = new HashMap<>();
            JSONArray jsonDocuments = FileUtilities.readContentAsJSON(file);
            for (int i = 0; i < jsonDocuments.length(); i++) {
                JSONObject jsonObject = jsonDocuments.getJSONObject(i);
                String type = jsonObject.getString("type");
                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String content = jsonObject.getString("content");
                LocalDate date = LocalDate.parse(jsonObject.getString("date"));
                JSONArray jsonArrayLogs = jsonObject.getJSONArray("logs");
                ArrayList<Log> logs = new ArrayList<>();
                for (int j = 0; j < jsonArrayLogs.length(); j++){
                    JSONObject jsonLogObject = jsonArrayLogs.getJSONObject(j);
                    String fieldname = jsonLogObject.getString("fieldname");
                    String newvalue = jsonLogObject.getString("newvalue");
                    String oldvalue = jsonLogObject.getString("oldvalue");
                    User editor = User.getUsers().get(jsonLogObject.getInt("editor_id"));
                    LocalDateTime timestamp = LocalDateTime.parse(jsonLogObject.getString("time_of_change"));
                    Log log = Log.createLog(fieldname, oldvalue, newvalue, editor, timestamp);
                    logs.add(log);
                }

                switch (type) {
                    case "Rede": {
                        Person person = Person.getPersonsList().get(jsonObject.getInt("speaker id"));
                        Rede newRede = DokumentFactory.createRede(id, title, content, date, person);
                        newRede.restoreLog(logs);
                        documents.put(id, newRede);
                        break;
                    }
                    case "Protokoll": {
                        Protokoll newProtokoll = DokumentFactory.createProtokoll(id, jsonObject.getInt("session number"), title, content, date);
                        newProtokoll.restoreLog(logs);
                        documents.put(id, newProtokoll);

                        break;
                    }
                    case "Drucksache": {
                        Drucksache newDrucksache = DokumentFactory.createDrucksache(id, title, content, date);
                        newDrucksache.restoreLog(logs);
                        documents.put(id, newDrucksache);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unknown document type.");
                }
            }
            for (int i = 0; i < jsonDocuments.length(); i++){
                JSONObject jsonObject = jsonDocuments.getJSONObject(i);
                if (jsonObject.getString("type").equals("Protokoll")){
                    Protokoll protokoll = (Protokoll) documents.get(jsonObject.getInt("id")); // to get true protokoll document and his id's to restore all speeches by using them as parameter.
                    ArrayList<Integer> speechesIdArray= convertJsonToArray(jsonObject.getJSONArray("speech id's"));
                    restoreSpeeches(documents, protokoll, speechesIdArray, user);

                }


            }
            return documents;
        } catch (SecurityException ex) {
            throw new SecurityException("User has no permission.");
        }


    }

    private boolean checkRede(Dokument doc) {
        if (doc instanceof Rede) {
            return true;
        }
        return false;

    }

    private boolean checkProtokoll(Dokument doc) {
        if (doc instanceof Protokoll) {
            return true;
        }
        return false;
    }


    private boolean checkDrucksache(Dokument doc) {
        if (doc instanceof Drucksache) {
            return true;
        }
        return false;

    }
    private ArrayList<Integer> convertJsonToArray(JSONArray jsonIds){
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < jsonIds.length(); i++){
            ids.add(jsonIds.getInt(i));
        }
        return ids;
    }

    protected void restoreSpeeches(Map<Integer, Dokument> documents, Protokoll protokoll, ArrayList<Integer> speechesIdArray, User user) {
        try {
            user.checkChangePermissions();
            ArrayList<Rede> restoredSpeeches = new ArrayList<>();
            for (int id : speechesIdArray){
                if (documents.get(id) instanceof Rede){
                    Rede rede = (Rede) documents.get(id);
                    restoredSpeeches.add(rede);
                }
                else{ throw new IllegalArgumentException("This id does not belong to a Rede document");}
            }
            protokoll.restoreSpeeches(restoredSpeeches); // I created a special method in Protokoll to avoiding creating log by adding speeches again.


        }
        catch(SecurityException ex){
            throw new SecurityException("User has no permission.");
        }
    }


}