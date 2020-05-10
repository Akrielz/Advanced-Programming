package com.example.demo;

import com.example.demo.Exceptions.ExceptionGameNotFound;
import com.example.demo.Exceptions.ExceptionPlayerNotFound;
import com.example.demo.Exceptions.ExceptionWinner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final Database database;

    public GameController() throws SQLException, ClassNotFoundException {
        database = Database.getInstance();
    }

    //Get
    @GetMapping
    public List<Game> getGames() throws SQLException {
        String query = "SELECT * FROM Games";
        ResultSet resultSet = database.executeQuery(query);

        List <Game> games = new ArrayList<>();
        while (resultSet.next()){
            int[] idp      = new int [2];
            int id         = resultSet.getInt   (1);
            idp[0]         = resultSet.getInt   (2);
            idp[1]         = resultSet.getInt   (3);
            int winner     = resultSet.getInt   (4);
            String content = resultSet.getString(5);

            games.add(new Game(id, idp, content, winner));
        }
        return games;
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable("id") int id) throws SQLException {
        String query = "SELECT * FROM Games WHERE ID = " + id;
        ResultSet resultSet = database.executeQuery(query);

        if (resultSet.next()){
            int[] idp      = new int [2];
            idp[0]         = resultSet.getInt   (2);
            idp[1]         = resultSet.getInt   (3);
            int winner     = resultSet.getInt   (4);
            String content = resultSet.getString(5);

            return new Game(id, idp, content, winner);
        }

        throw new ExceptionGameNotFound();
    }

    @GetMapping("/count")
    public int countGames() throws SQLException {
        String query = "SELECT COUNT(*) FROM Games";
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

    @PostMapping
    public ResponseEntity<String> createGame(@RequestParam int p1, @RequestParam int p2,
                                             @RequestParam int winner, @RequestParam String content) throws SQLException {

        if (!existsPlayer(p1)) {
            throw new ExceptionPlayerNotFound();
        }

        if (!existsPlayer(p2)) {
            throw new ExceptionPlayerNotFound();
        }

        if (winner != 0 && winner != 1 && winner != 2){
            throw  new ExceptionWinner();
        }

        int id = 1 + countGames();

        String query = "INSERT INTO Games(id, id_p1, id_p2, winner, content) VALUES(?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt   (1, id);
        preparedStatement.setInt   (2, p1);
        preparedStatement.setInt   (3, p2);
        preparedStatement.setInt   (4, winner);
        preparedStatement.setString(5, content);

        preparedStatement.execute();

        return new ResponseEntity<>("Game created successfully", HttpStatus.CREATED);
    }

}
