package ua.kiev.prog.homework12.part4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static CurrentGroup currentGroup;

    public static void main(String[] args) {
        currentGroup = new CurrentGroup(new Group("Default"));
        fillGroup();
        ExecutorService eService = Executors.newFixedThreadPool(5);
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started.");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new HttpClient(currentGroup, eService, clientSocket);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
