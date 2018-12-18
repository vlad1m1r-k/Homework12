package ua.kiev.prog.homework12.part4;

import java.io.Serializable;

public class Student extends Human implements Serializable {
    private  int performance;

    public Student(String firstName, String lastName, int age, Sex sex, int performance) {
        super(firstName, lastName, age, sex);
        this.performance = performance;
    }

    public int getPerformance() {
        return performance;
    }

    @Override
    public String toString(){
        return "<tr>" + super.toString() + "<td>" + performance + "</td><td><form action='/' method='post' target='_self'>" +
                "<input type='submit' value='X'><input type='hidden' name='action' value='delete'><input type='hidden' name='group' value='@@groupname@@'>" +
                "<input type='hidden' name='name' value='" + super.getLastName() +"'></form></td></tr>";
    }
}
