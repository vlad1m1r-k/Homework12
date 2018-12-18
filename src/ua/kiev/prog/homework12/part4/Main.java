package ua.kiev.prog.homework12.part4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    private static CurrentGroup currentGroup;
    public static void main(String[] args) {
        currentGroup = new CurrentGroup(new Group("Default"));
        fillGroup();
        ExecutorService eService = Executors.newFixedThreadPool(4);
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("Server started.");
            while (true){
                Socket clientSocket = serverSocket.accept();
                new HttpClient(currentGroup, eService, clientSocket);
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
     //       System.out.println("1 - Add Student, 2 - Sort, 5 - Summon Voenkom, 8 - Filter");
    }

    private static void filterGroup() {
        Scanner keyboardScanner = new Scanner(System.in);
        System.out.print("Enter first letter of student LastName: ");
        String letter = keyboardScanner.nextLine();
        if (letter.length() != 0){
            List<Student> filteredList = currentGroup.getGroup().getStudents().stream()
                    .filter(s -> s.getLastName().toLowerCase().charAt(0) == letter.toLowerCase().charAt(0))
                    .collect(Collectors.toList());
            String formattedStudents = "";
            for (Student student : filteredList) {
                formattedStudents += student + "\n";
            }
            System.out.println(formattedStudents);
        }
    }

    private static void addStudent() {
        Scanner keyboardScanner = new Scanner(System.in);
        System.out.print("Enter \nFirstName LastName Age(1-100) Sex(M|F) AcademicPerformance(0-100) \n Separated by space: ");
        String[] args = keyboardScanner.nextLine().split(" +");
        if (args.length != 5) {
            System.out.println("Wrong parameters count. " + args.length);
            return;
        }
        if (!args[2].matches("\\d+")) {
            System.out.println("Error parsing Age: " + args[2]);
            return;
        }
        int age = Integer.parseInt(args[2]);
        if (!args[3].equalsIgnoreCase("M") && !args[3].equalsIgnoreCase("F")) {
            System.out.println("Sex accepts only M or F symbols. " + args[3]);
            return;
        }
        Sex sex = args[3].equalsIgnoreCase("M") ? Sex.MALE : Sex.FEMALE;
        if (!args[4].matches("\\d+")) {
            System.out.println("Error parsing AcademicPerformance: " + args[2]);
            return;
        }
        int academPerform = Integer.parseInt(args[4]);
        if (academPerform < 0 || academPerform > 100) {
            System.out.println("AcademicPerformance is out of range (0 - 100)");
            return;
        }
        try {
            currentGroup.getGroup().add(new Student(args[0], args[1], age, sex, academPerform));
        } catch (StudentOperationException soe) {
            System.out.println("Can not add more students.");
        }
    }

    private static void sortStudents() {
        Scanner keyboardScanner = new Scanner(System.in);
        System.out.print("Sort by: \n1 - FirstName \n2 - LastName \n3 - Age \n4 - Sex \n5 - AcademicPerformance\n>");
        String choose = keyboardScanner.nextLine();
        switch (choose) {
            case "1":
                currentGroup.getGroup().sort(Parameters.FIRSTNAME);
                break;
            case "2":
                currentGroup.getGroup().sort(Parameters.LASTNAME);
                break;
            case "3":
                currentGroup.getGroup().sort(Parameters.AGE);
                break;
            case "4":
                currentGroup.getGroup().sort(Parameters.SEX);
                break;
            case "5":
                currentGroup.getGroup().sort(Parameters.PERFORMANCE);
                break;
        }
    }

    private static void summonVoenkom() {
        AngryVoenkom angryVoenkom = new AngryVoenkom();
        List<Student> students = angryVoenkom.catchStudents(currentGroup.getGroup());
        String formattedStudents = "";
        for (Student student : students) {
            formattedStudents += student + "\n";
        }
        System.out.println("Catched students:");
        System.out.println(formattedStudents);
    }

    private static void fillGroup() {
        try {
            currentGroup.getGroup().add(new Student("Ivan", "Karko", 18, Sex.MALE, 90));
            currentGroup.getGroup().add(new Student("Lena", "Baskova", 19, Sex.FEMALE, 60));
            currentGroup.getGroup().add(new Student("Petr", "Azirov", 18, Sex.MALE, 70));
            currentGroup.getGroup().add(new Student("Uy", "Chang", 18, Sex.FEMALE, 55));
            currentGroup.getGroup().add(new Student("Petr", "Ali", 18, Sex.MALE, 79));
            currentGroup.getGroup().add(new Student("Inna", "Torba", 18, Sex.FEMALE, 100));
            currentGroup.getGroup().add(new Student("Petr", "Kent", 19, Sex.MALE, 100));
            currentGroup.getGroup().add(new Student("Tom", "Star", 17, Sex.MALE, 30));
            currentGroup.getGroup().add(new Student("Yas", "Barto", 19, Sex.MALE, 45));
        } catch (StudentOperationException soe) {
            System.out.println("Error adding " + soe.getObject().toString());
            System.out.println("Cause " + soe.getMessage());
        }
    }
}
