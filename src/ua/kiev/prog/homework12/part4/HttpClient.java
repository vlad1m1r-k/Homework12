package ua.kiev.prog.homework12.part4;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;

public class HttpClient implements Runnable {
    private CurrentGroup currentGroup;
    private ExecutorService executor;
    private Socket clientSocket;

    public HttpClient(CurrentGroup currentGroup, ExecutorService executor, Socket clientSocket) {
        this.currentGroup = currentGroup;
        this.executor = executor;
        this.clientSocket = clientSocket;
        executor.submit(this);
    }

    @Override
    public void run() {
        try (InputStream inputStream = clientSocket.getInputStream(); OutputStream outputStream = clientSocket.getOutputStream()) {
            byte[] fullRequest = new byte[inputStream.available()];
            inputStream.read(fullRequest);
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
                    currentGroup.getGroup().delete(form.substring(form.lastIndexOf("=") + 1, form.length()));
                    return renderPage(address);
                case "save":
                    return saveGroup(form.substring(form.lastIndexOf("=") + 1, form.length()).replaceAll("\\W",""));
                case "load":
                    loadGroup(form.substring(form.lastIndexOf("=") + 1, form.length()));
                    return renderPage(address);
            }
        }
        return "Unknown action";
    }

    private String renderPage(String address) {
        StringBuilder result = new StringBuilder();
        result.append("HTTP/1.1 200 OK\n");
        result.append("Content-Type: text/html\n");
        result.append("\n");
        String name = address.equals("/") ? "/index.html" : address;
        try {
            Files.readAllLines(Paths.get("HTTPResources" + name)).stream().forEach(result::append);
        } catch (IOException e) {
            result = new StringBuilder("HTTP/1.1 404 Not Found");
            result.append("Content-Type: text/html\n");
            result.append("\n");
            e.printStackTrace();
            return result.toString();
        }
        result = new StringBuilder(result.toString().replaceAll("@@grouplines@@", currentGroup.getGroup().toString()));
        result = new StringBuilder(result.toString().replaceAll("@@groupname@@", currentGroup.getGroup().getGroupName()));
        result = new StringBuilder(result.toString().replaceAll("@@grouplist@@", getGroupList()));
        return result.toString();
    }

    private String saveGroup(String name){
        File file = new File("Groups/" + name + ".sav");
        if (file.exists() && !file.isDirectory()) file.delete();
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            currentGroup.getGroup().setGroupName(name);
            output.writeObject(currentGroup.getGroup());
        } catch (IOException e) {
            e.printStackTrace();
            return "Error saving file.";
        }
        return "Successful saved.";
    }

    private String getGroupList(){
        File dir = new File("Groups");
        File[] groups = dir.listFiles(path -> {
            if (!path.isDirectory() && !(path.getName().lastIndexOf(".") == -1) && path.getName().substring(path.getName().lastIndexOf(".")).equalsIgnoreCase(".sav"))
                return true;
            return false;
        });
        StringBuilder result = new StringBuilder();
        result.append("<table><tr>");
        for (File file : groups) {
            result.append("<td><form action='/' method='post' target='_self'>");
            result.append("<input type='hidden' name='action' value='load'>");
            result.append("<input type='hidden' name='name' value='" + file.getName().substring(0, file.getName().lastIndexOf(".")) + "'>");
            result.append("<input type='submit' value='" + file.getName().substring(0, file.getName().lastIndexOf(".")) + "'></form></td>");
        }
        result.append("</tr></table>");
        return result.toString();
    }

    private void loadGroup(String name){
        File file = new File("Groups/" + name + ".sav");
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            currentGroup.setGroup((Group) input.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}
