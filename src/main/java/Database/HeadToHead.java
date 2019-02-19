package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class HeadToHead {
    /*
        CREATE TABLE public.batsman_to_bowler
        (
            batsman_id integer NOT NULL,
            batsman_team text NOT NULL,
            bowler_id integer NOT NULL,
            bowler_team text NOT NULL,
            match_id integer NOT NULL,
            innings_num integer NOT NULL,
            num_balls integer NOT NULL,
            num_runs integer NOT NULL,
            num_wickets integer NOT NULL,
            num_dot_balls integer NOT NULL,
            num_fours integer NOT NULL,
            num_sixes integer NOT NULL,
            num_no_balls integer NOT NULL,
            FOREIGN KEY (batsman_id)
                REFERENCES public.player (id) MATCH SIMPLE
                ON UPDATE NO ACTION
                ON DELETE NO ACTION,
            FOREIGN KEY (bowler_id)
                REFERENCES public.player (id) MATCH SIMPLE
                ON UPDATE NO ACTION
                ON DELETE NO ACTION,
            FOREIGN KEY (match_id)
                REFERENCES public.match (id) MATCH SIMPLE
                ON UPDATE NO ACTION
                ON DELETE NO ACTION
        )
     */
    public static void insert(Connection connection, int batsmanId, String batsmanTeam, int bowlerId, String bowlerTeam,
                              int matchId, int inningsNum, int numBalls, int numRuns, int numWickets, int numDotBalls,
                              int numFours, int numSixes, int numNoBalls) throws SQLException {
        String SQL = "INSERT INTO batsman_to_bowler VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(   1,  batsmanId);
        preparedStatement.setString(2,  batsmanTeam);
        preparedStatement.setInt(   3,  bowlerId);
        preparedStatement.setString(4,  bowlerTeam);
        preparedStatement.setInt(   5,  matchId);
        preparedStatement.setInt(   6,  inningsNum);
        preparedStatement.setInt(   7,  numBalls);
        preparedStatement.setInt(   8,  numRuns);
        preparedStatement.setInt(   9,  numWickets);
        preparedStatement.setInt(   10, numDotBalls);
        preparedStatement.setInt(   11, numFours);
        preparedStatement.setInt(   12, numSixes);
        preparedStatement.setInt(   13, numNoBalls);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static void clear(Connection connection) throws SQLException {
        String SQL = "DELETE FROM batsman_to_bowler";
        Statement preparedStatement = connection.createStatement();
        int rows = preparedStatement.executeUpdate(SQL);
        preparedStatement.close();
        System.out.println(rows + " Rows Deleted from batsman_to_bowler Table");
    }
}
