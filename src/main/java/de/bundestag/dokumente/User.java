package de.bundestag.dokumente;

public class User {
    private final int id;
    private Person person;
    private String rolle;
    private String name;

    private User(int id, Person person, String rolle){
        this.id = id;
        this.person = person;
        this.name = person.getName();
        if ((rolle != "LESER") && (rolle != "BEARBEITER")){
            throw new IllegalArgumentException("Falsche rolle eingegeben.");
        }
        else {this.rolle = rolle;}
    }

    public String getName(){

        return this.name;
    }

    public String getRolle(){

        return this.rolle;
    }
    public int getId(){
        return this.id;
    }

    public static User createBenutzer(int id, Person person, String rolle){
        return new User(id, person, rolle);
    }

    @Override
    public String toString(){
        return ("name: "+this.name + "\n"+ "rolle: " +this.rolle + "\n");
    }
}
