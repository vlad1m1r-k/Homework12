package ua.kiev.prog.homework12.part4;

import java.util.ArrayList;
import java.util.List;

public class AngryVoenkom implements Voenkom {
    @Override
    public List<Student> catchStudents(Group group) {
        List<Student> catchedStudents = new ArrayList<>();
        for (Student student : group.getStudents()) {
            if (student.getSex() == Sex.MALE && student.getAge() > 18) {
                catchedStudents.add(student);
            }
        }
        return catchedStudents;
    }
}
