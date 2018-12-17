package ua.kiev.prog.homework12.part4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class HttpClient implements Runnable{
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
        try (InputStream inputStream = clientSocket.getInputStream(); OutputStream outputStream = clientSocket.getOutputStream()){
            byte[] request = new byte[inputStream.available()];
            inputStream.read(request);
            System.out.println(new String(request));
            outputStream.write("Hello".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
