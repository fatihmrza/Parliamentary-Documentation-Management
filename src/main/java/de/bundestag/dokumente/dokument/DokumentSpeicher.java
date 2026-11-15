package de.bundestag.dokumente.dokument;

import de.bundestag.dokumente.User;

import java.io.IOException;
import java.util.Map;

/**
 * public interface to defining rules by saving documents.
 */
public interface DokumentSpeicher {
    /**
     * determines how to save a document.
     * @param documents
     * @param filePath
     * @param user
     * @throws IOException
     */
    void saveDocuments(Map<Integer, Dokument> documents, String filePath, User user) throws IOException;

    /**
     * determines how to load a document.
     * @param filePath
     * @param user
     * @return
     * @throws IOException
     */
    Map<Integer, Dokument> loadDocuments(String filePath, User user) throws IOException;
}

