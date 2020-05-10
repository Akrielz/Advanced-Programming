package com.example.demo;

import com.example.demo.Exceptions.ExceptionPlayerNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final Database database;

    public PlayerController() throws SQLException, ClassNotFoundException {
        database = Database.getInstance();
    }

    //Get
    @GetMapping
    public List<Player> getPlayers() throws SQLException {
        String query = "SELECT * FROM Players";
        ResultSet resultSet = database.executeQuery(query);

        List <Player> players = new ArrayList<>();
        while (resultSet.next()){
            int    id   = resultSet.getInt   (1);
            String name = resultSet.getString(2);

            players.add(new Player(name, id));
        }
        return players;
    }
    @GetMapping("/count")
    public int countPlayers() throws SQLException {
        String query = "SELECT COUNT(*) FROM Players";
        ResultSet resultSet = database.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public boolean existsPlayer(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM Players WHERE ID = " + id;
        ResultSet resultSet = database.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt(1) != 0;
        }
        return false;
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable("id") int id) throws SQLException {
        String query = "SELECT * FROM Players WHERE ID = " + id;
        ResultSet resultSet = database.executeQuery(query);

        if (resultSet.next()){
            String name = resultSet.getString(2);
            return new Player(name, id);
        }

        throw new ExceptionPlayerNotFound();
    }

    @PostMapping
    public ResponseEntity<String> createPlayer(@RequestParam String name) throws SQLException {
        int id = 1 + countPlayers();

        String query = "INSERT INTO Players(id, name) VALUES(?, ?)";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt   (1, id);
        preparedStatement.setString(2, name);

        preparedStatement.execute();

        return new ResponseEntity<>("Player created successfully", HttpStatus.CREATED);
    }

    @PostMapping(value = "/obj", consumes = "application/json")
    public ResponseEntity<String> createPlayer(@RequestBody Player player) throws SQLException {
        String query = "INSERT INTO Players(id, name) VALUES(?, ?)";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt   (1, player.getId());
        preparedStatement.setString(2, player.getName());

        preparedStatement.execute();

        return new ResponseEntity<>("Player created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePlayer(@PathVariable int id, @RequestParam String name) throws SQLException {
        if (! existsPlayer(id)){
            throw new ExceptionPlayerNotFound();
        }

        String query = "UPDATE Players SET name=? WHERE id=?";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setInt   (2, id);

        preparedStatement.execute();

        return new ResponseEntity<>("Player updated succesfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable int id) throws SQLException {
        if (! existsPlayer(id)){
            throw new ExceptionPlayerNotFound();
        }

        String query = "DELETE FROM Players WHERE ID = ?";
        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);

        preparedStatement.execute();

        return new ResponseEntity<>("Player deleted succesfully", HttpStatus.OK);
    }

}
