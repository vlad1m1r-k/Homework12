package ua.kiev.prog.homework12.part4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;

public class Group implements Serializable {
    private List<Student> students = new ArrayList<>();

    public void add(Student student) {
        if (students.size() > 9) throw new StudentOperationException(student);
        students.add(student);
    }

    public void delete(String lastName) {
        students.remove(getIndex(lastName));
    }

    public int getIndex(String lastName) {
        for (int i = 0; i < students.size() - 1; i++) {
            if (students.get(i).getLastName().equalsIgnoreCase(lastName)) return i;
        }
        throw new InputMismatchException();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void sort(Parameters sortParameters) {
        switch (sortParameters) {
            case FIRSTNAME:
                Collections.sort(students, (student1, student2) -> {
                    if (student1 != null && student2 != null)
                        return student1.getFirstName().compareToIgnoreCase(student2.getFirstName());
                    else return 1;
                });
                break;
            case LASTNAME:
                Collections.sort(students, (student1, student2) -> {
                    if (student1 != null && student2 != null)
                        return student1.getLastName().compareToIgnoreCase(student2.getLastName());
                    else return 1;
                });
                break;
            case AGE:
                Collections.sort(students, (student1, student2) -> {
                    if (student1 != null && student2 != null)
                        return student1.getAge() - student2.getAge();
                    else return 1;
                });
                break;
            case SEX:
                Collections.sort(students, (student1, student2) -> {
                    if (student1 != null && student2 != null) {
                        if (student1.getSex() == student2.getSex()) return 0;
                        if (student1.getSex() == Sex.MALE) return -1;
                        else return 1;
                    } else return 1;
                });
                break;
            case PERFORMANCE:
                Collections.sort(students, (student1, student2) -> {
                    if (student1 != null && student2 != null)
                        return student1.getPerformance() - student2.getPerformance();
                    else return 1;
                });
        }
    }

    @Override
    public String toString() {
        String formattedStudents = "";
        for (Student student : students) {
            formattedStudents += student + "\n";
        }
        return formattedStudents;
    }
}
