package com.company;


import com.github.javafaker.Faker;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            resetValues();
            //compulsory();
            optional();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void compulsory() throws SQLException, ClassNotFoundException {
        /*Artists*/
        ArtistController artistController = ArtistController.getInstance();
        artistController.create("Necula Adrian", "Germania");
        artistController.create("Akriel", "Romania");
        artistController.create("Stefan Balauca", "Romania");

        List<Artist> artistList = artistController.getAll();
        System.out.println("Artists:");
        for (Artist artist : artistList){
            System.out.println(artist);
        }

        System.out.println("\nFound artists:");

        List<Artist> nameList = artistController.findByName("A");
        for (Artist artist : nameList){
            System.out.println(artist);
        }

        /*Albums*/;
        AlbumController albumController = AlbumController.genInstance();
        albumController.create("Hitul anului", 1, 2020);
        albumController.create("Rapsodia Efemerului", 2, 2018);
        albumController.create("Balada Universului", 3, 2022);
        albumController.create("Flacarile Iadului", 2, 2045);


        List<Album> albumList = albumController.getAll();
        System.out.println("\nAlbums:");
        for (Album album : albumList){
            System.out.println(album);
        }

        System.out.println("\nFound albums:");
        List<Album> idArtists = albumController.findByArtist(2);
        for (Album album : idArtists){
            System.out.println(album);
        }

        /*Charts*/
        ChartController chartController = ChartController.getInstance();
        chartController.createChart("Awards");
        chartController.createChart("Oscar");
        chartController.createBind(1, 1);
        chartController.createBind(2, 2);
        chartController.createBind(3, 2);
        chartController.createBind(4, 1);

        System.out.println("\nCharts: ");
        for (var chart : chartController.getAll()){
            System.out.print(chart.getName() + ": ");
            for (var album : chartController.getBind(chart.getId())){
                System.out.print(album.getName() + ", ");
            }
            System.out.println();
        }
    }

    public static void optional() throws SQLException, ClassNotFoundException, IOException {
        //Fake Data Generation
        Database database = Database.getInstance();
        Faker faker = new Faker();
        int nrArtists = 100;
        int nrAlbums = (int)(1e3);
        int nrCharts = 100;
        int nrBinds = 300;

        ArtistController artistController = ArtistController.getInstance();
        for (int i = 0; i < nrArtists; i++) {
            String name = faker.name().fullName();
            String country = faker.address().country();
            artistController.create(name, country);
        }

        AlbumController albumController = AlbumController.genInstance();
        for (int i = 0; i < nrAlbums; i++){
            int id = (int) (Math.random() * nrArtists) +1;
            String name = faker.book().title();
            int year = (int) (Math.random() * 200) +1900;
            albumController.create(name, id, year);
        }

        ChartController chartController = ChartController.getInstance();
        for (int i = 0; i < nrCharts; i++){
            String name = faker.beer().name();
            chartController.createChart(name);
        }

        for (int i = 0; i < nrBinds; i++){
            int idChart = (int)(Math.random()*nrCharts) + 1;
            int idAlbum = (int)(Math.random()*nrAlbums) + 1;
            chartController.createBind(idAlbum, idChart);
        }

        //Ranking Artists
        int []ranking = new int[nrArtists];
        for (int i = 1; i < nrArtists; i++){
            ranking[i] = 0;
        }

        for (var chart : chartController.getAll()){
            int index = chartController.getBind(chart.getId()).size();

            for (var album : chartController.getBind(chart.getId())){
                int idArtist = album.getArtistId()-1;
                ranking[idArtist] += index;
                index--;
            }
        }

        List <Pair<Integer, Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < nrArtists; i++){
            pairs.add(new Pair<>(ranking[i], i+1));
        }
        pairs.sort(
                (Pair <Integer, Integer> p1, Pair <Integer, Integer> p2) ->
                        p2.getKey() - p1.getKey()
        );

        for (var pair : pairs){
            int id = pair.getValue();
            int points = pair.getKey();
            Artist artist = artistController.get(id);
            System.out.println(artist.getName() + ": " + points);
        }

        HtmlReport htmlReport = new HtmlReport();
        htmlReport.createReport(pairs);
    }

    public static void resetValues() throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();

        Statement statement = database.getConnection().createStatement();
        statement.execute("DELETE FROM Bind");
        statement.execute("DELETE FROM Charts");
        statement.execute("DELETE FROM Albums");
        statement.execute("DELETE FROM Artists");
    }
}