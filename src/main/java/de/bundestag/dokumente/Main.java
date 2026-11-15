package de.bundestag.dokumente;

import de.bundestag.dokumente.dokument.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        User admin = User.getAdmin();
        User.getUsers().put(admin.getId(), admin);
        Person person = Person.createPerson(1, "Anna", 30);
        Person otherPerson = Person.createPerson(2, "Max", 35);
        User editor = User.createBenutzer(10, person, "BEARBEITER", admin);
        User reader = User.createBenutzer(20, otherPerson, "LESER", admin);

        Dokumentenverwaltung verwaltung = new Dokumentenverwaltung();
        DokumentSpeicher speicher = new JsonDokumentSpeicher();
        LocalDate date = LocalDate.of(2026, 11, 15);

        Rede rede = verwaltung.createRede(101, "Rede", "Redetext", date, person, editor);
        Protokoll protokoll = verwaltung.createProtokoll(201, 12, "Protokoll", "Protokolltext", date, admin);
        Drucksache drucksache = verwaltung.createDrucksache(301, "Antrag", "Antragstext", date, editor);
        check(rede != null && protokoll != null && drucksache != null
                && Dokumentenverwaltung.getDocuments().size() == 3, "Dokumente angelegt");

        for (Dokument dokument : Dokumentenverwaltung.getDocuments().values()) {
            System.out.println(dokument);
        }

        verwaltung.changeTitle(101, "Neuer Titel", editor);
        verwaltung.changeContent(301, "Neuer Inhalt", editor);
        verwaltung.changeDate(201, date.plusDays(1), admin);
        verwaltung.changeSpeaker(101, otherPerson, editor);
        verwaltung.addSpeeches(201, rede, editor);
        check(rede.getTitle().equals("Neuer Titel")
                && drucksache.getContent().equals("Neuer Inhalt")
                && protokoll.getDate().equals(date.plusDays(1))
                && rede.getSpeakerId() == otherPerson.getId()
                && protokoll.getSpeeches().get(0) == rede, "Aenderungen und Zuordnung");
        check(rede.getLogs().size() == 2 && protokoll.getLogs().size() == 2
                && drucksache.getLogs().size() == 1, "Logs erstellt");

        verwaltung.changeTitle(101, "Nicht erlaubt", reader);
        check(rede.getTitle().equals("Neuer Titel") && rede.getLogs().size() == 2,
                "Leser kann nicht bearbeiten");
        check(verwaltung.createRede(999, "Test", "Text", date, person, reader) == null
                && !Dokumentenverwaltung.getDocuments().containsKey(999), "Leser kann nichts anlegen");

        boolean rejected = false;
        try {
            verwaltung.createDrucksache(101, "Doppelte ID", "Text", date, admin);
        } catch (IllegalArgumentException ex) {
            rejected = true;
        }
        check(rejected && Dokumentenverwaltung.getDocuments().get(101) == rede, "Doppelte ID abgelehnt");

        rejected = false;
        try {
            verwaltung.addSpeeches(201, rede, editor);
        } catch (IllegalArgumentException ex) {
            rejected = true;
        }
        check(rejected && protokoll.getSpeeches().size() == 1
                && protokoll.getLogs().size() == 2, "Doppelte Rede abgelehnt");

        Path directory = Files.createTempDirectory("uebung2-test-");
        String filePath = directory.resolve("dokumente.json").toString();
        verwaltung.saveDocuments(speicher, filePath, editor);
        check(Files.size(Path.of(filePath)) > 0, "JSON gespeichert");

        Map<Integer, Dokument> before = new HashMap<>(Dokumentenverwaltung.getDocuments());
        Dokumentenverwaltung.getDocuments().clear();
        verwaltung.loadDocuments(speicher, filePath, admin);
        Map<Integer, Dokument> loaded = Dokumentenverwaltung.getDocuments();
        check(loaded.keySet().equals(before.keySet()), "Alle Dokumente geladen");

        for (Dokument original : before.values()) {
            Dokument restored = loaded.get(original.getId());
            check(restored != null && restored != original
                            && restored.getClass() == original.getClass()
                            && restored.getTitle().equals(original.getTitle())
                            && restored.getContent().equals(original.getContent())
                            && restored.getDate().equals(original.getDate())
                            && restored.getLogs().size() == original.getLogs().size(),
                    "Dokument " + original.getId() + " erhalten");

            for (int i = 0; i < original.getLogs().size(); i++) {
                Log oldLog = original.getLogs().get(i);
                Log newLog = restored.getLogs().get(i);
                check(newLog.getFieldName().equals(oldLog.getFieldName())
                        && newLog.getOldValue().equals(oldLog.getOldValue())
                        && newLog.getNewValue().equals(oldLog.getNewValue())
                        && newLog.getUser() == oldLog.getUser()
                        && newLog.getTimestamp().equals(oldLog.getTimestamp()), "Log erhalten");
            }
        }

        Rede loadedRede = (Rede) loaded.get(101);
        Protokoll loadedProtokoll = (Protokoll) loaded.get(201);
        check(loadedRede.getSpeakerId() == otherPerson.getId()
                && loadedProtokoll.getSessionNumber() == 12
                && loadedProtokoll.getSpeeches().size() == 1
                && loadedProtokoll.getSpeeches().get(0) == loadedRede, "Beziehungen erhalten");

        rejected = false;
        String savedText = Files.readString(Path.of(filePath));
        try {
            verwaltung.saveDocuments(speicher, filePath, reader);
        } catch (SecurityException ex) {
            rejected = true;
        }
        check(rejected && savedText.equals(Files.readString(Path.of(filePath))), "Speichern fuer Leser abgelehnt");

        rejected = false;
        try {
            verwaltung.loadDocuments(speicher, filePath, reader);
        } catch (SecurityException ex) {
            rejected = true;
        }
        check(rejected && Dokumentenverwaltung.getDocuments() == loaded, "Laden fuer Leser abgelehnt");

        verwaltung.printLogs();
        System.out.println("Alle Tests erfolgreich.");
        System.out.println("JSON-Datei: " + filePath);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException("Test fehlgeschlagen: " + message);
        }
        System.out.println("OK: " + message);
    }
}
