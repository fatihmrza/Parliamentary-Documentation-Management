package de.bundestag.dokumente;

import java.util.HashMap;
import java.util.Map;

/**
 * public class for creating person by specifying all it's attributes and storing them in Array list.
 * @author Fatih Mirza 8500551
 */
public class Person {
    private final int id;
    private final String name;
    private final int age;
    private static Map<Integer, Person> persons = new HashMap<>();
    static Person admin = Person.createPerson(0,"admin",0);

    private Person (int id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /**
     * getter method for id.
     * @return int
     */
    public int getId(){
        return this.id;
    }

    /**
     * getter method for name.
     * @return String
     */
    public String getName(){
        return this.name;
    }

    /**
     * getter method for age.
     * @return int
     */
    public int getAge(){
        return this.age;
    }

    /**
     * getter method for hash map which stores all Person instances.
     * @return Map<Integer, Person>
     */
    public static Map<Integer, Person> getPersonsList(){
        return persons;
    }

    /**
     * factory method for class Person.
     * @param id
     * @param name
     * @param age
     * @return Person
     */
    public static Person createPerson(int id, String name, int age)
    {
        if (!persons.containsKey(id)){
            Person newPerson =  new Person(id, name, age);
            persons.put(id,newPerson);
            return newPerson;
        }
        throw new IllegalArgumentException("There exists already a person with this id.");
    }

    /**
     * Overriding String method by entering all attributes.
     * @return String
     */
    @Override
    public String toString(){
        return ("name: " + this.name + " " + "age: " + this.age);
    }

}
