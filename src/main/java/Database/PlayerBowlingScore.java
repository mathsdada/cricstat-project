package Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerBowlingScore {
    /*
    CREATE TABLE public.player_bowling_score
    (
        player_id integer NOT NULL,
        match_id integer NOT NULL,
        innings_num integer NOT NULL,
        team_played_for text NOT NULL,
        team_played_against text NOT NULL,
        overs_bowled numeric(4, 1) NOT NULL,
        num_maidens integer NOT NULL,
        runs_given integer NOT NULL,
        wickets_taken integer NOT NULL,
        num_no_balls integer NOT NULL,
        num_wides integer NOT NULL,
        economy numeric(5, 2) NOT NULL,
        FOREIGN KEY (player_id)
            REFERENCES public.player (id) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION,
        FOREIGN KEY (match_id)
            REFERENCES public.match (id) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION
        )
    */

    public static void insert(Connection connection, int playerId, int matchId, int inningsNum,
                              String teamPlayedFor, String teamPlayedAgainst,
                              BigDecimal oversBowled, int numMaidens, int runsGiven, int wicketsTaken, int numNoBalls,
                              int numWides, BigDecimal economy) throws SQLException {
        String SQL = "INSERT INTO player_bowling_score VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, playerId);
        preparedStatement.setInt(2, matchId);
        preparedStatement.setInt(3, inningsNum);
        preparedStatement.setString(4, teamPlayedFor);
        preparedStatement.setString(5, teamPlayedAgainst);
        preparedStatement.setBigDecimal(6, oversBowled);
        preparedStatement.setInt(7, numMaidens);
        preparedStatement.setInt(8, runsGiven);
        preparedStatement.setInt(9, wicketsTaken);
        preparedStatement.setInt(10, numNoBalls);
        preparedStatement.setInt(11, numWides);
        preparedStatement.setBigDecimal(12, economy);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static void clear(Connection connection) throws SQLException {
        String SQL = "DELETE FROM player_bowling_score";
        Statement preparedStatement = connection.createStatement();
        int rows = preparedStatement.executeUpdate(SQL);
        preparedStatement.close();
        System.out.println(rows + " Rows Deleted from player_bowling_score Table");
    }
}
