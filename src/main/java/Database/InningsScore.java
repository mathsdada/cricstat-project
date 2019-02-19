package Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class InningsScore {
    /* Schema
    *   CREATE TABLE public.innings_score
        (
            match_id integer NOT NULL,
            innings_num integer NOT NULL,
            batting_team text NOT NULL,
            bowling_team text NOT NULL,
            runs integer NOT NULL,
            wickets integer NOT NULL,
            overs numeric(4, 1) NOT NULL,
            FOREIGN KEY (match_id)
                REFERENCES public.match (id) MATCH SIMPLE
        )
     */
    public static void insert(Connection connection, int matchId, int inningsNum, String battingTeam,
                              String bowlingTeam, int runs, int wickets, BigDecimal overs) throws SQLException {
        String SQL = "INSERT INTO innings_score VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, matchId);
        preparedStatement.setInt(2, inningsNum);
        preparedStatement.setString(3, battingTeam);
        preparedStatement.setString(4, bowlingTeam);
        preparedStatement.setInt(5, runs);
        preparedStatement.setInt(6, wickets);
        preparedStatement.setBigDecimal(7, overs);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static void clear(Connection connection) throws SQLException {
        String SQL = "DELETE FROM innings_score";
        Statement preparedStatement = connection.createStatement();
        int rows = preparedStatement.executeUpdate(SQL);
        preparedStatement.close();
        System.out.println(rows + " Rows Deleted from innings_score Table");
    }
}
