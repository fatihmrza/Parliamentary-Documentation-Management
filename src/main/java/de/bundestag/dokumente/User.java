package de.bundestag.dokumente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *User class for storing all user datas and utility method for checking user permission
 * @author Fatih Mirza 8500551
 */
public class User {
    private final int id;
    private Person person;
    private String role;
    private final String name;
    private static final User admin = new User(0, Person.admin, "ADMIN");
    private static Map<Integer, User> users = new HashMap<>();


    /**
     * private user construction which is initialized by factory method.
     * @param id
     * @param person
     * @param role
     */
    private User(int id, Person person, String role){
        this.id = id;
        this.person = person;
        this.name = person.getName();
        if (!((role.equals("LESER")) || (role.equals("BEARBEITER")) || (role.equals("ADMIN")))){
            throw new IllegalArgumentException("Falsche rolle eingegeben.");
        }
        else {this.role = role;}
    }

    /**
     * getter method for name.
     * @return String
     */
    public String getName(){

        return this.name;
    }

    /**
     * getter method for Rolle.
     * @return String
     */
    public String getRolle(){

        return this.role;
    }

    /**
     * getter method for id.
     * @return int
     */
    public int getId(){
        return this.id;
    }

    public static User getAdmin(){
        return User.admin;
    }

    /**
     * getter method for user hash map.
     * @return Map<Integer, User>
     */
    public static Map<Integer, User> getUsers(){
        return users;
    }

    /**
     * enables to check user instance if it has permission to edit something.
     */
    public void checkChangePermissions(){
        if (!((this.role.equals("BEARBEITER") || (this.role.equals("ADMIN"))))){
            throw new SecurityException("User does not have permission to modify this document");
        }
    }

    /**
     * factory method for User class.
     * @param id
     * @param person
     * @param rolle
     * @param user
     * @return User
     */
    public static User createBenutzer(int id, Person person, String rolle, User user){
        if (user.getRolle().equals("ADMIN")) {
            if (!users.containsKey(id)) {
                User newUser = new User(id, person, rolle);
                users.put(id, newUser);
                return newUser;
            }
            else {throw new IllegalArgumentException("There exist already an user with this id.");}

        }
        else { throw new SecurityException("User does not have a permission to creating user.");

        }
    }

    /**
     * Overriding String method to printing class.
     * @return String
     */
    @Override
    public String toString(){
        return ("name: "+this.name + "\n"+ "rolle: " +this.role + "\n");
    }
}
