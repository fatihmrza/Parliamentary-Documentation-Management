# Uebung 2 – Verwaltung von Bundestagsdokumenten

Das Projekt gehört zum Programmierpraktikum. Es verwaltet drei Dokumentarten: Reden, Protokolle und Drucksachen. Dokumente können angelegt, geändert und als JSON gespeichert werden. Die Änderungen werden beim jeweiligen Dokument festgehalten.

## Voraussetzungen

- JDK 21 oder neuer
- Maven
- Die Bibliothek `org.json:json:20260814`

Die Java-Version und die JSON-Abhängigkeit stehen in der `pom.xml`. Maven lädt die Bibliothek beim ersten Build herunter. Dafür ist eine Internetverbindung nötig.

Das Projekt lässt sich in IntelliJ IDEA als Maven-Projekt öffnen. Dafür die `pom.xml` auswählen, ein passendes JDK einstellen und die Maven-Abhängigkeiten laden.

## Projekt bauen

Im Projektordner ausführen:

```bash
mvn compile
```

Die übersetzten Klassen liegen danach unter `target/classes`.


## Pakete und Klassen

```text
src/main/java/
├── de/bundestag/dokumente/
│   ├── Person.java
│   ├── User.java
│   ├── Log.java
│   └── dokument/
│       ├── Dokument.java
│       ├── Rede.java
│       ├── Protokoll.java
│       ├── Drucksache.java
│       ├── DokumentFactory.java
│       ├── Dokumentenverwaltung.java
│       ├── DokumentSpeicher.java
│       └── JsonDokumentSpeicher.java
└── utilities/
    └── FileUtilities.java
```

| Klasse oder Interface | Aufgabe |
| --- | --- |
| `Person` | Enthält ID, Name und Alter einer Person. Personen werden über `createPerson` angelegt und in einer Map gesammelt. |
| `User` | Verbindet eine Person mit einem Benutzerkonto und einer Rolle. Enthält die Prüfung der Änderungsrechte. |
| `Log` | Hält eine einzelne Änderung mit altem Wert, neuem Wert, Benutzer und Zeitpunkt fest. |
| `Dokument` | Abstrakte Basisklasse mit ID, Titel, Inhalt, Datum und Änderungshistorie. |
| `Rede` | Ergänzt das Dokument um eine Person als Sprecher. |
| `Protokoll` | Ergänzt das Dokument um eine Sitzungsnummer und eine Liste von Reden. |
| `Drucksache` | Verwendet die gemeinsamen Dokumentdaten und liefert den Dokumenttyp `Drucksache`. |
| `DokumentFactory` | Erzeugt die drei konkreten Dokumentarten mit statischen Methoden. |
| `Dokumentenverwaltung` | Verwaltet Dokumente nach ihrer ID und bietet Methoden zum Anlegen, Ändern, Speichern und Laden an. |
| `DokumentSpeicher` | Interface für das Speichern und Laden der Dokumente. |
| `JsonDokumentSpeicher` | Setzt das Speicher-Interface mit JSON-Dateien um. |
| `FileUtilities` | Liest und schreibt den Inhalt von Dateien. Grundlage ist die Hilfsklasse aus der Lehrveranstaltung von Giuseppe Abrami. |

`Dokument` ist abstrakt, weil die Dokumentarten gemeinsame Daten und Methoden haben. Die Unterklassen geben mit `getDocumentType()` ihren jeweiligen Typ zurück.

Die Verwaltung verwendet die Factory zum Anlegen der Dokumente. Beim Laden verwendet auch `JsonDokumentSpeicher` diese Factory. 

Für das Speichern und Laden nimmt die Verwaltung ein Objekt vom Typ `DokumentSpeicher` entgegen. Im Projekt wird dafür `JsonDokumentSpeicher` verwendet. Eine andere Speicherklasse könnte dasselbe Interface implementieren.

## Daten und Beziehungen

Die Dokumente liegen in einer `Map<Integer, Dokument>`. Der Schlüssel ist die Dokument-ID. Diese ID muss über alle drei Dokumentarten hinweg eindeutig sein.

Die Dokument-Map ist in `Dokumentenverwaltung` statisch. Mehrere Verwaltungsobjekte verwenden deshalb denselben Dokumentbestand. Auch `Person` und `User` besitzen statische Maps für ihre Objekte.

Eine Person ist nicht dasselbe wie ein Benutzerkonto. Ein Sprecher kann als `Person` vorhanden sein, ohne ein `User` zu sein. Personen-IDs und Benutzer-IDs gehören zu getrennten Maps.

Jede Rede hat einen Sprecher. Ein Protokoll enthält eine Liste von Rede-Objekten. Beim Zuordnen einer Rede wird das vorhandene Objekt verwendet, keine Kopie angelegt.

## Benutzerrollen

Die Rollen werden als Strings gespeichert:

| Rolle | Vorgesehene Rechte über die Verwaltung |
| --- | --- |
| `LESER` | Dokumente und ihre Änderungshistorie ansehen. |
| `BEARBEITER` | Zusätzlich Dokumente anlegen, bearbeiten, speichern und laden. |
| `ADMIN` | Dieselben Dokumentrechte wie ein Bearbeiter und zusätzlich Benutzer anlegen. |

`User.checkChangePermissions()` erlaubt Änderungen für `BEARBEITER` und `ADMIN`. Für `LESER` wirft die Methode eine `SecurityException`. `User.createBenutzer` prüft gesondert, ob der aufrufende Benutzer ein Admin ist.

Der erste Admin ist bereits in `User` angelegt und wird über `User.getAdmin()` abgerufen. Seine Benutzer-ID ist `0`. Die zugehörige Admin-Person hat ebenfalls die ID `0` in der Personen-Map.



## Dokumente bearbeiten und Änderungen anzeigen

Die Verwaltung bietet unter anderem diese Methoden an:

- `createRede`, `createProtokoll` und `createDrucksache`
- `changeTitle`, `changeContent` und `changeDate`
- `changeSpeaker` für eine Rede
- `addSpeeches` zum Hinzufügen einer einzelnen Rede zu einem Protokoll
- `printLogs` zum Anzeigen der Änderungshistorien mit der jeweiligen Dokument-ID

Die Änderungsmethoden in den Dokumentklassen sind `protected`. Der normale Aufruf erfolgt über `Dokumentenverwaltung`, wo zuerst die Berechtigung geprüft wird.

Jedes Dokument hat eine eigene `ArrayList<Log>`. Änderungen an Titel, Inhalt, Datum oder Sprecher sowie das Hinzufügen einer Rede erzeugen einen Logeintrag. Ein Log enthält:

- den Namen des geänderten Feldes,
- den bisherigen und den neuen Wert als String,
- den Benutzer, der die Änderung vorgenommen hat,
- den Zeitpunkt als `LocalDateTime`.

Bei einer Sprecheränderung werden die Personen-IDs festgehalten. Beim Hinzufügen einer Rede werden die bisherige und die neue Liste der Rede-IDs gespeichert.

## JSON speichern und laden

`saveDocuments` schreibt den gesamten übergebenen Dokumentbestand in eine Datei. Der bisherige Dateiinhalt wird dabei ersetzt. Die Datei enthält ein JSON-Array mit einem Objekt pro Dokument.

Alle Dokumente haben die Felder `type`, `id`, `title`, `content`, `date` und `logs`. Das Datum wird als String im Format `YYYY-MM-DD` gespeichert.

Zusätzliche Felder sind:

| Dokumentart | JSON-Felder |
| --- | --- |
| `Rede` | `speaker id` |
| `Protokoll` | `session number`, `speech id's`, `number of speech` |
| `Drucksache` | Keine zusätzlichen Felder. |

Die Schreibweise der Schlüssel entspricht dem aktuellen Code, einschließlich der Leerzeichen. Die Zuordnung der Reden erfolgt über `speech id's`.

Die Logeinträge stehen innerhalb des jeweiligen Dokuments. Sie enthalten `fieldname`, `oldvalue`, `newvalue`, `editor_id` und `time_of_change`.

Beim Laden gibt es zwei Durchläufe. Zuerst werden alle Dokumente und ihre bisherigen Logs angelegt. Danach werden die Rede-IDs der Protokolle mit den geladenen Rede-Objekten verbunden. So ist die Reihenfolge der Dokumente in der Datei für diese Zuordnung nicht entscheidend.

Das Wiederherstellen erfolgt mit `restoreLog` und `restoreSpeeches`. Dabei werden keine neuen Änderungslogs erzeugt.

Beim Aufruf von `Dokumentenverwaltung.loadDocuments` wird die bisherige Dokument-Map durch die geladene Map ersetzt. Die Daten werden nicht mit dem bisherigen Bestand zusammengeführt.

Dort stehen nur ihre IDs. Die passenden Personen und Benutzer müssen daher vor dem Laden bereits in `Person.getPersonsList()` und `User.getUsers()` vorhanden sein. Alle Reden, auf die ein Protokoll verweist, müssen in der geladenen Dokumentdatei enthalten sein.


Der Pfad `data/dokumente.json` ist relativ zum Arbeitsverzeichnis des Programms. Personen und Benutzer bleiben bei diesem Speichern und Laden im selben Programmablauf in ihren Maps erhalten.

## Fehlerbehandlung

Eine bereits verwendete Dokument-ID, eine unbekannte Dokument-ID oder ein falscher Dokumenttyp führt in der Verwaltung zu einer `IllegalArgumentException`. Eine Rede kann demselben Protokoll über die Verwaltung nicht zweimal hinzugefügt werden.

Bei fehlender Änderungsberechtigung fängt die Verwaltung die `SecurityException` in ihren Anlege- und Änderungsmethoden ab und gibt die Meldung auf der Konsole aus. Die Anlegemethoden geben in diesem Fall `null` zurück. Ihr Rückgabewert sollte vor der weiteren Verwendung geprüft werden.

Die JSON-Speicherklasse gibt eine `SecurityException` weiter, wenn der Benutzer nicht speichern oder laden darf. Fehler beim Lesen und Schreiben werden als `IOException` an den Aufrufer weitergegeben. Ungültiges JSON oder ein ungültiges Datum kann zusätzlich zu einem Fehler beim Einlesen führen.

## Autor

Fatih Mirza