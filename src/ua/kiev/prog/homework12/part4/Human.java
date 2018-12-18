package ua.kiev.prog.homework12.part4;

import java.io.Serializable;

public class Human implements Serializable {
    private String firstName;
    private String lastName;
    private int age;
    private Sex sex;

    public Human(String firstName, String lastName, int age, Sex sex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public Sex getSex() {
        return sex;
    }

    public String toString(){
        return "<td>" + firstName + "</td><td>" + lastName + "</td><td>" + age + "</td><td>" + sex + "</td>";
    }
}