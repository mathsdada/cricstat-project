package Database.Tables;

import Database.Model.Player;
import Database.Model.Team;
import com.google.gson.Gson;
import rest.Response.ScheduleResponse;

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
            teams text NOT NULL,
            series text NOT NULL,
            category text NOT NULL,
            PRIMARY KEY (id)
        )
        WITH (
            OIDS = FALSE
        );

        ALTER TABLE public.schedule
            OWNER to vgangadhar11;
    */
    public static void insert(Connection connection, int id, String title, String format, String venue, Long date,
                              ArrayList<Team> teams, String series, String category) throws SQLException {
        Gson gson = new Gson();
        ArrayList<ScheduleResponse.ScheduleTeam> scheduleTeams = new ArrayList<>();
        for (Team team: teams) {
            ArrayList<ScheduleResponse.SchedulePlayer> squad = new ArrayList<>();
            for (Player player: team.getSquad()) {
                squad.add(new ScheduleResponse.SchedulePlayer(player.getName(), player.getRole(),
                        player.getBattingStyle(), player.getBowlingStyle()));
            }
            scheduleTeams.add(new ScheduleResponse.ScheduleTeam(team.getName(), squad));
        }
        String SQL = "INSERT INTO schedule VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, format);
        preparedStatement.setString(4, venue);
        preparedStatement.setLong(5, date);
        preparedStatement.setString(6, gson.toJson(scheduleTeams));
        preparedStatement.setString(7, series);
        preparedStatement.setString(8, category);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static void clear(Connection connection) throws SQLException {
        String SQL = "DELETE FROM schedule";
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(SQL);
        statement.close();
        System.out.println(rows + " Rows Deleted from Schedule Table");
    }
}
