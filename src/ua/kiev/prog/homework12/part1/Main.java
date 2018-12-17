package ua.kiev.prog.homework12.part1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        URL url = null;
        try {
            List<String> siteList = Files.readAllLines(Paths.get("checkSites.txt"));
            for (String site : siteList) {
                url = new URL(site);
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.getResponseCode();
                    System.out.println(url + " - Available");
                } catch (IOException ioe) {
                    System.out.println(url + " - Connection error");

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
