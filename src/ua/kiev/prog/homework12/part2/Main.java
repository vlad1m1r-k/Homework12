package ua.kiev.prog.homework12.part2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static int requestNumber = 1;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket connect = serverSocket.accept();
                PrintWriter out = new PrintWriter(connect.getOutputStream());
                out.println("Request # " + requestNumber++);
                out.println("Java version = " + System.getProperty("java.runtime.version"));
                out.println("OS name = " + System.getProperty("os.name"));
                out.println("OS version = " + System.getProperty("os.version"));
                out.println("User Name = " + System.getProperty("user.name"));
                out.println("Processors count = " + Runtime.getRuntime().availableProcessors());
                out.println("VM max memory = " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + " MB");
                out.close();
                connect.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
