package Database.Tables;

import Database.Model.Player;
import Database.Model.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Schedule {
    /*
        CREATE TABLE public.schedule
        (
            id integer NOT NULL,
            title text NOT NULL,
            format text NOT NULL,
            venue text NOT NULL,
            date bigint NOT NULL,
            series text NOT NULL,
            first_team json NOT NULL,
            second_team json NOT NULL,
            PRIMARY KEY (id)
        )
    */
    public static void insert(Connection connection, int id, String title, String format, String venue, Long date,
                              ArrayList<Team> teams, String series) throws SQLException {
        clear(connection);
        JSONArray teamsJsonArray = new JSONArray();
        for (Team team: teams) {
            JSONArray squadJsonArray = new JSONArray();
            for (Player player: team.getSquad()) {
                JSONObject playerJson = new JSONObject();
                playerJson.put("name", player.getName());
                playerJson.put("role", player.getRole());
                playerJson.put("batting_style", player.getBattingStyle());
                playerJson.put("bowling_style", player.getBowlingStyle());
                squadJsonArray.add(playerJson);
            }
            JSONObject teamJsonObject = new JSONObject();
            teamJsonObject.put("name", team.getName());
            teamJsonObject.put("squad", squadJsonArray);
            teamsJsonArray.add(teamJsonObject);
        }
        String SQL = "INSERT INTO schedule VALUES (?, ?, ?, ?, ?, ?, ?::json, ?::json)";

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, format);
        preparedStatement.setString(4, venue);
        preparedStatement.setLong(5, date);
        preparedStatement.setString(6, series);
        preparedStatement.setString(7, teamsJsonArray.get(0).toString());
        preparedStatement.setString(8, teamsJsonArray.get(1).toString());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private static void clear(Connection connection) throws SQLException {
        String SQL = "DELETE FROM schedule";
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(SQL);
        statement.close();
        System.out.println(rows + " Rows Deleted from Schedule Table");
    }
}
