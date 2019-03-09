package Database.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class HeadToHead {
    /*
        CREATE TABLE public.player_head_to_head
        (
            batsman_id integer NOT NULL,
            bowler_id integer NOT NULL,
            batting_team text NOT NULL,
            bowling_team text NOT NULL,
            match_id integer NOT NULL,
            innings_num integer NOT NULL,
            balls integer NOT NULL,
            runs integer NOT NULL,
            wicket boolean NOT NULL,
            dot_balls integer NOT NULL,
            fours integer NOT NULL,
            sixes integer NOT NULL,
            no_balls integer NOT NULL,
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
        WITH (
            OIDS = FALSE
        );

        ALTER TABLE public.player_head_to_head
            OWNER to vgangadhar11;
     */
    public static void insert(Connection connection, int batsmanId, String batsmanTeam, int bowlerId, String bowlerTeam,
                              int matchId, int inningsNum, int numBalls, int numRuns, int numWickets, int numDotBalls,
                              int numFours, int numSixes, int numNoBalls) throws SQLException {
        String SQL = "INSERT INTO batsman_to_bowler VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        int parameterIndex = 0;
        preparedStatement.setInt(   ++parameterIndex,  batsmanId);
        preparedStatement.setInt(   ++parameterIndex,  bowlerId);
        preparedStatement.setString(++parameterIndex,  batsmanTeam);
        preparedStatement.setString(++parameterIndex,  bowlerTeam);
        preparedStatement.setInt(   ++parameterIndex,  matchId);
        preparedStatement.setInt(   ++parameterIndex,  inningsNum);
        preparedStatement.setInt(   ++parameterIndex,  numBalls);
        preparedStatement.setInt(   ++parameterIndex,  numRuns);
        preparedStatement.setInt(   ++parameterIndex,  numWickets);
        preparedStatement.setInt(   ++parameterIndex, numDotBalls);
        preparedStatement.setInt(   ++parameterIndex, numFours);
        preparedStatement.setInt(   ++parameterIndex, numSixes);
        preparedStatement.setInt(   ++parameterIndex, numNoBalls);

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
