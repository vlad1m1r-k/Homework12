package ua.kiev.prog.homework12.part4;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;

public class HttpClient implements Runnable {
    private Group group;
    ExecutorService executor;
    Socket clientSocket;

    public HttpClient(Group group, ExecutorService executor, Socket clientSocket) {
        this.group = group;
        this.executor = executor;
        this.clientSocket = clientSocket;
        executor.submit(this);
    }

    @Override
    public void run() {
        try (InputStream inputStream = clientSocket.getInputStream(); OutputStream outputStream = clientSocket.getOutputStream()) {
            byte[] fullRequest = new byte[inputStream.available()];
            inputStream.read(fullRequest);
            //System.out.println(new String(fullRequest));
            String[] requestLines = new String(fullRequest).split("\n");
            String requestType = requestLines[0].substring(0, requestLines[0].indexOf(" ")).trim();
            String requestAddress = requestLines[0].substring(requestLines[0].indexOf(" "), requestLines[0].indexOf(" HTTP")).trim();
            String form = requestLines[requestLines.length - 1];
            outputStream.write(formatPage(requestType, requestAddress, form).getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String formatPage(String type, String address, String form) {
        if (type.equals("GET")) {
            return renderPage(address);
        }
        if (type.equals("POST")) {
            String action = form.substring(form.indexOf("=") + 1, form.indexOf("&"));
            switch (action) {
                case "delete":
                    group.delete(form.substring(form.lastIndexOf("=") + 1, form.length()));
                    return renderPage(address);
                case "save":
                    return saveGroup(form.substring(form.lastIndexOf("=") + 1, form.length()).replaceAll("\\W",""));
            }
        }
        return "Unknown action";
    }

    private String renderPage(String address) {
        StringBuilder result = new StringBuilder();
        String name = address.equals("/") ? "/index.html" : address;
        try {
            Files.readAllLines(Paths.get("HTTPResources" + name)).stream().forEach(result::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = new StringBuilder(result.toString().replaceAll("@@grouplines@@", group.toString()));
        result = new StringBuilder(result.toString().replaceAll("@@groupname@@", group.getGroupName()));
        return result.toString();
    }

    private String saveGroup(String name){
        File file = new File("Groups/" + name + ".sav");
        if (file.exists() && !file.isDirectory()) file.delete();
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(group);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error saving file.";
        }
        return "Successful saved.";
    }
}
