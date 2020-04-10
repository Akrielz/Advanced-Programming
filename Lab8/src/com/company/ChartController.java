package com.company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ChartController implements Dao <Chart>{
    private Map<Chart, List<Album>> relationship;
    private List<Chart> charts;

    private static ChartController chartController;

    private ChartController() throws SQLException, ClassNotFoundException {
        relationship = new HashMap<>();
        charts = new ArrayList<>();
        Database database = Database.getInstance();

        String queryCharts = "SELECT * FROM Charts";
        ResultSet resultSetCharts = database.executeQuery(queryCharts);
        while(resultSetCharts.next()){
            int    id   = resultSetCharts.getInt(1);
            String name = resultSetCharts.getString(2);

            Chart chart = new Chart(id, name);
            charts.add(chart);
            relationship.put(chart, new ArrayList<>());
        }

        String queryBinds = "SELECT * FROM Bind";
        AlbumController albumController = AlbumController.genInstance();
        ResultSet resultSetBinds = database.executeQuery(queryBinds);
        while(resultSetBinds.next()){
            int chartId = resultSetBinds.getInt(1);
            int albumId = resultSetBinds.getInt(2);

            Album album = albumController.get(albumId);
            System.out.println(albumId + " " + chartId);
            System.out.println(charts);
            Chart chart = get(chartId);

            relationship.get(chart).add(album);
        }
    }

    public static ChartController getInstance() throws SQLException, ClassNotFoundException {
        if (chartController == null)
            chartController = new ChartController();
        return chartController;
    }

    @Override
    public List<Chart> getAll() {
        return charts;
    }

    public Map<Chart, List<Album>> getAllBinds() {
        return relationship;
    }

    @Override
    public Chart get(int id) {
        return charts.get(id-1);
    }

    public List<Album> getBind(int id) {
        return relationship.get(get(id));
    }

    public void createChart(String name) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();

        int id = charts.size()+1;
        String query =  "INSERT INTO Charts(id, name) VALUES(?, ?)";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt   (1, id);
        preparedStatement.setString(2, name);
        preparedStatement.execute();

        Chart chart = new Chart(id, name);
        charts.add(chart);
        relationship.put(chart, new ArrayList<>());
    }

    public void createBind(int albumId, int chartId) throws SQLException, ClassNotFoundException {
        Database database = Database.getInstance();
        String query =  "INSERT INTO Bind(chart_id, album_id) VALUES(?, ?)";

        PreparedStatement preparedStatement = database.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, chartId);
        preparedStatement.setInt(2, albumId);
        preparedStatement.execute();

        AlbumController albumController = AlbumController.genInstance();
        Chart chart = chartController.get(chartId);
        Album album = albumController.get(albumId);

        relationship.get(chart).add(album);
    }
}
