package ua.kiev.prog.homework12.part4;

class StudentOperationException extends Error{
    private Student object;

    public StudentOperationException(Student object) {
        this.object = object;
    }

    public Student getObject() {
        return object;
    }

    @Override
    public String getMessage() {
        return "Not enough space.";
    }
}
