package ua.kiev.prog.homework12.part3;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        try {
            InputStream inputStream = ((HttpURLConnection) new URL(address).openConnection()).getInputStream();
            StringBuilder string = new StringBuilder();
            int b;
            while ((b = inputStream.read()) != -1) {
                string.append((char) b);
            }
            Pattern pattern = Pattern.compile("<a.*? href=\"(.*?)\".*?>");
            Matcher matcher = pattern.matcher(string);
            StringBuilder result = new StringBuilder();
            while (matcher.find()) {
                result.append(matcher.group(1) + "\n");
            }
            Files.write(Paths.get("links.txt"), result.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
