package com.company;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArtistController implements Dao<Artist> {
    List<Artist> artists;
    static ArtistController artistController;

    private ArtistController() throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        artists = new ArrayList<>();

        String query = "SELECT * FROM Artists";
        ResultSet resultSet = database.executeQuery(query);
        while (resultSet.next()){
            int    id      = resultSet.getInt   (1);
            String name    = resultSet.getString(2);
            String country = resultSet.getString(3);

            Artist artist;
            if (country.isEmpty()){
                artist = new Artist(id, name);
            }
            else {
                artist = new Artist(id, name, country);
            }
            artists.add(artist);
        }
    }

    public static ArtistController getInstance() throws SQLException, ClassNotFoundException {
        if (artistController == null)
            artistController = new ArtistController();
        return artistController;
    }

    @Override
    public List<Artist> getAll() {
        return artists;
    }

    public void create(String name, String country) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();

        int id = artists.size()+1;
        String query =  "INSERT INTO Artists(id, name, country) VALUES(?, ?, ?)";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt   (1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, country);

        preparedStatement.execute();

        artists.add(new Artist(id, name, country));
    }

    @Override
    public Artist get(int id) {
        return artists.get(id-1);
    }

    public List<Artist> findByName(String name) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        String query = "SELECT * FROM Artists WHERE NAME LIKE '%" + name + "%'";
        //System.out.println(query);

        ResultSet resultSet = database.executeQuery(query);
        List <Artist> resultList = new ArrayList<>();
        while(resultSet.next()){
            int    id         = resultSet.getInt   (1);
            String nameArtist = resultSet.getString(2);
            String country    = resultSet.getString(3);
            resultList.add(new Artist(id, nameArtist, country));
        }

        return resultList;
    }
}
