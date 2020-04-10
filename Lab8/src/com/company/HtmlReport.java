package com.company;

import javafx.util.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class HtmlReport {
    private String startHTML;
    private String endHTML;

    public static String readFileByLine(String fileName) {
        String lines = "";
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                lines += scanner.next();
                lines += " ";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public HtmlReport() throws FileNotFoundException {
        String startPath = "F:\\Programming Projects\\Java\\Lab8v2 HTML\\start.html";
        startHTML = readFileByLine(startPath);
        String endPath = "F:\\Programming Projects\\Java\\Lab8v2 HTML\\end.html";
        endHTML = readFileByLine(endPath);
    }

    public void createReport(List<Pair<Integer, Integer>> pairList) throws SQLException, ClassNotFoundException, IOException {
        String html = startHTML;

        ArtistController artistController = ArtistController.getInstance();
        int index = 0;
        for (var pair : pairList){
            int id = pair.getValue();
            int points = pair.getKey();
            Artist artist = artistController.get(id);

            String player;

            if (index % 2 ==  0) {
                player = "<tr>\n" +
                        "\t<td class=\"dark left\">\n" +
                        "\t\t" + (index+1) + "\n" +
                        "\t</td>\n" +
                        "\t<td class=\"dark\">\n" +
                        "\t\t<a title=\"Unrated, " + artist.getName() + " \" class=\"rated-user user-black\"> " + artist.getName() + "</a> </td>\n" +
                        "\t<td class=\"dark right\">\n" +
                        "\t\t" + points + "\n" +
                        "\t</td>\n" +
                        "</tr>\n";
            }
            else{
                player = "<tr>\n" +
                        "\t<td class=\"left\">\n" +
                        "\t\t" + (index+1) + "\n" +
                        "\t</td>\n" +
                        "\t<td>\n" +
                        "\t\t<a title=\"Unrated, " + artist.getName() + "\" class=\"rated-user user-black\">" + artist.getName() + "</a></td>\n" +
                        "\t<td class=\"right\">\n" +
                        "\t\t" + points + "\n" +
                        "\t</td>\n" +
                        "</tr>\n";
            }
            html += player;

            index++;
        }

        html += endHTML;

        String finalPath = "F:\\Programming Projects\\Java\\Lab8v2 HTML\\example.html";
        FileOutputStream file = new FileOutputStream(finalPath);
        Writer writer = new BufferedWriter(new OutputStreamWriter(file, StandardCharsets.UTF_8));
        writer.write(html);
        writer.close();
        file.close();
    }
}
