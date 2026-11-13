package de.bundestag.dokumente;

import java.util.HashMap;
import java.util.Map;

public class Person {
    private final int id;
    private final String name;
    private final int age;
    static Person admin = Person.createPerson(0,"admin",0);
    private static Map<Integer, Person> persons = new HashMap<>();

    private Person (int id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }


    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getAge(){
        return this.age;
    }

    public static Map<Integer, Person> getPersonsList(){
        return persons;
    }

    public static Person createPerson(int id, String name, int age)
    {
        if (!persons.containsKey(id)){
            Person newPerson =  new Person(id, name, age);
            persons.put(id,newPerson);
            return newPerson;
        }
        throw new IllegalArgumentException("There exists already a person with this id.");
    }

    @Override
    public String toString(){
        return ("name: " + this.name + " " + "age: " + this.age);
    }

}
