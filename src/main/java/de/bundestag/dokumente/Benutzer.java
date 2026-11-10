package de.bundestag.dokumente;

public class Benutzer {
    private int id;
    private String name;
    private String rolle;

    private Benutzer(int id, String name, String rolle){
        this.id = id;
        this.name = name;
        if ((rolle != "LESER") && (rolle != "BEARBEITER")){
            throw new IllegalArgumentException("Falsche rolle eingegeben.");
        }
        else {this.rolle = rolle;}
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getRolle(){
        return this.rolle;
    }

    public Benutzer createBenutzer(int id, String name, String rolle){
        return new Benutzer(id, name, rolle);
    }

    @Override
    public String toString(){
        return ("name: "+this.name + "\n"+ "rolle: " +this.rolle + "\n");
    }
}
