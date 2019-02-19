package Database;

import java.math.BigDecimal;
import java.sql.*;

public class PlayerBattingScore {
    /*
    *   CREATE TABLE public.player_batting_score
        (
            player_id integer NOT NULL,
            match_id integer NOT NULL,
            innings_num integer NOT NULL,
            team_played_for text NOT NULL,
            team_played_against text NOT NULL,
            runs_scored integer NOT NULL,
            balls_played integer NOT NULL,
            num_fours integer NOT NULL,
            num_sixes integer NOT NULL,
            strike_rate numeric(10, 2) NOT NULL,
            status text NOT NULL,
            bowled_out_by integer,
            FOREIGN KEY (player_id)
                REFERENCES public.player (id) MATCH SIMPLE
                ON UPDATE NO ACTION
                ON DELETE NO ACTION,
            FOREIGN KEY (match_id)
                REFERENCES public.match (id) MATCH SIMPLE
                ON UPDATE NO ACTION
                ON DELETE NO ACTION,
            FOREIGN KEY (bowled_out_by)
                REFERENCES public.player (id) MATCH SIMPLE
                ON UPDATE NO ACTION
                ON DELETE NO ACTION
        )
    * */
    public static void insert(Connection connection, int playerId, int matchId, int inningsNum,
                              String teamPlayedFor, String teamPlayedAgainst,
                              int runsScored, int ballsPlayed, int numFours, int numSixes, BigDecimal strikeRate,
                              String status, int bowledOutBy) throws SQLException {
        String SQL = "INSERT INTO player_batting_score VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, playerId);
        preparedStatement.setInt(2, matchId);
        preparedStatement.setInt(3, inningsNum);
        preparedStatement.setString(4, teamPlayedFor);
        preparedStatement.setString(5, teamPlayedAgainst);
        preparedStatement.setInt(6, runsScored);
        preparedStatement.setInt(7, ballsPlayed);
        preparedStatement.setInt(8, numFours);
        preparedStatement.setInt(9, numSixes);
        preparedStatement.setBigDecimal(10, strikeRate);
        preparedStatement.setString(11, status);
        if (bowledOutBy != 0) {
            preparedStatement.setInt(12, bowledOutBy);
        } else {
            preparedStatement.setNull(12, Types.INTEGER);
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static void clear(Connection connection) throws SQLException {
        String SQL = "DELETE FROM player_batting_score";
        Statement preparedStatement = connection.createStatement();
        int rows = preparedStatement.executeUpdate(SQL);
        preparedStatement.close();
        System.out.println(rows + " Rows Deleted from player_batting_score Table");
    }
}
