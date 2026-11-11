package de.bundestag.dokumente;

public class User {
    private final int id;
    private Person person;
    private String role;
    private final String name;
    private static final User admin = new User(0, Person.admin, "ADMIN");

    private User(int id, Person person, String role){
        this.id = id;
        this.person = person;
        this.name = person.getName();
        if (!((role == "LESER") || (role == "BEARBEITER") || (role == "ADMIN"))){
            throw new IllegalArgumentException("Falsche rolle eingegeben.");
        }
        else {this.role = role;}
    }

    public String getName(){

        return this.name;
    }

    public String getRolle(){

        return this.role;
    }
    public int getId(){
        return this.id;
    }

    public User getAdmin(){
        return User.admin;
    }

    public void checkChangePermissions(){
        if (!((this.role.equals("BEARBEITER") || (this.role.equals("ADMIN"))))){
            throw new SecurityException("User does not have permission to modify this document");
        }
    }

    public static User createBenutzer(int id, Person person, String rolle, User user){
        if (user.getRolle() == "ADMIN") {
            return new User(id, person, rolle);

        }
        else { throw new SecurityException("User does not have a permission to creating user.");

        }
    }

    @Override
    public String toString(){
        return ("name: "+this.name + "\n"+ "rolle: " +this.role + "\n");
    }
}
