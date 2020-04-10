package com.company;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumController implements Dao<Album> {
    private List<Album> albums;
    private static AlbumController albumController;

    private AlbumController() throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        albums = new ArrayList<>();

        String query = "SELECT * FROM Albums";
        ResultSet resultSet = database.executeQuery(query);
        while (resultSet.next()){
            int    id        = resultSet.getInt   (1);
            String name      = resultSet.getString(2);
            int    artistsId = resultSet.getInt   (3);
            int    release   = resultSet.getInt   (4);

            Album album = new Album(id, name, artistsId, release);
            albums.add(album);
        }
    }

    public static AlbumController genInstance() throws SQLException, ClassNotFoundException {
        if (albumController == null)
            albumController = new AlbumController();
        return albumController;
    }

    @Override
    public Album get(int id) {
        return albums.get(id-1);
    }

    @Override
    public List<Album> getAll() {
        return albums;
    }

    public void create(String name, int artistId, int releaseYear) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();

        int id = albums.size()+1;
        String query =  "INSERT INTO Albums(id, name, artist_id, release_year) VALUES(?, ?, ?, ?)";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt   (1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setInt   (3, artistId);
        preparedStatement.setInt   (4, releaseYear);
        preparedStatement.execute();

        albums.add(new Album(id, name, artistId, releaseYear));
    }

    public List<Album> findByArtist(int artistId) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        String query = "SELECT * FROM Albums WHERE Artist_Id = " + artistId;
        ResultSet resultSet = database.executeQuery(query);

        List<Album> results = new ArrayList<>();
        while (resultSet.next()){
            int id          = resultSet.getInt   (1);
            String name     = resultSet.getString(2);
            int releaseYear = resultSet.getInt   (4);

            Album album = new Album(id, name, artistId, releaseYear);
            results.add(album);
        }

        return results;
    }
}
