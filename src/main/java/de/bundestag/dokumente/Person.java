package de.bundestag.dokumente;

public class Person {
    private final int id;
    private final String name;
    private final int age;
    static Person admin = Person.createPerson(0,"admin",0);

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

    public static Person createPerson(int id, String name, int age){
        return new Person(id, name, age);
    }

    @Override
    public String toString(){
        return ("name: " + this.name + " " + "age: " + this.age);
    }

}
