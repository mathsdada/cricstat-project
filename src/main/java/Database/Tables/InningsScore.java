package Database.Tables;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class InningsScore {
    /* Schema
        CREATE TABLE public.innings_score
        (
            match_id integer NOT NULL,
            innings_num integer NOT NULL,
            batting_team text NOT NULL,
            bowling_team text NOT NULL,
            runs integer NOT NULL,
            wickets integer NOT NULL,
            balls integer NOT NULL,
            FOREIGN KEY (match_id)
                REFERENCES public.match (id) MATCH SIMPLE
                ON UPDATE NO ACTION
                ON DELETE NO ACTION
        )
        WITH (
            OIDS = FALSE
        );

        ALTER TABLE public.innings_score
            OWNER to vgangadhar11;
     */
    public static void insert(Connection connection, int matchId, int inningsNum, String battingTeam,
                              String bowlingTeam, int runs, int wickets, int balls) throws SQLException {
        String SQL = "INSERT INTO innings_score VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, matchId);
        preparedStatement.setInt(2, inningsNum);
        preparedStatement.setString(3, battingTeam);
        preparedStatement.setString(4, bowlingTeam);
        preparedStatement.setInt(5, runs);
        preparedStatement.setInt(6, wickets);
        preparedStatement.setInt(7, balls);

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
