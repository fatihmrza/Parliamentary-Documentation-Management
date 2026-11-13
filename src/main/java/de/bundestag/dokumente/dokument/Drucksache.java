package de.bundestag.dokumente.dokument;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Drucksache extends Dokument{
    private static final String DOCUMENT_TYPE = "Drucksache";
    protected Drucksache(int id, String title, String content, LocalDate date) {
        super(id, title, content, date);
    }

    @Override
    public String getDocumentType(){
        return Drucksache.DOCUMENT_TYPE;
    }

    @Override
    public String toString(){
        return ("Document type: " + Drucksache.DOCUMENT_TYPE + " " + "Document id: " + this.getId() + " " + "Session number: "  +
                "title: " + this.getTitle() + " " + "Date created: " + this.getDate() + " " + "Number of change on document: " + this.getLogs().size());
    }

}
