package de.bundestag.dokumente.dokument;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * public class for Drucksache documents.
 * @author Fatih Mirza 8500551
 */
public class Drucksache extends Dokument{
    private static final String DOCUMENT_TYPE = "Drucksache";

    /**
     * protected constructor which inherits all of his attributes form super class.
     * @param id
     * @param title
     * @param content
     * @param date
     */
    protected Drucksache(int id, String title, String content, LocalDate date) {
        super(id, title, content, date);
    }

    /**
     * Overriding document type class to getting document type.
     * @return String
     */
    @Override
    public String getDocumentType(){
        return Drucksache.DOCUMENT_TYPE;
    }

    /**
     * Overriding toString() method to printing instances with their attributes.
     * @return String
     */
    @Override
    public String toString(){
        return ("Document type: " + Drucksache.DOCUMENT_TYPE + " " + "Document id: " + this.getId() + " " + "Session number: "  +
                "title: " + this.getTitle() + " " + "Date created: " + this.getDate() + " " + "Number of change on document: " + this.getLogs().size());
    }

}
