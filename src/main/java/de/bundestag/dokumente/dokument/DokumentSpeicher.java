package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.User;

import java.io.IOException;
import java.util.Map;

public interface DokumentSpeicher {

    void saveDocuments(Map<Integer, Dokument> documents, String filePath, User user) throws IOException;

    Map<Integer, Dokument> loadDocuments(String filePath, User user) throws IOException;
}

