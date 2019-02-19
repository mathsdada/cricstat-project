package Database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;

public class Match {
    /* Schema
            CREATE TABLE public.match
            (
                id integer NOT NULL,
                series_id integer NOT NULL,
                title text NOT NULL,
                format text NOT NULL,
                teams text[] NOT NULL,
                outcome text NOT NULL,
                winning_team text,
                venue text NOT NULL,
                date bigint NOT NULL,
                status text NOT NULL;
                PRIMARY KEY (id),
                FOREIGN KEY (series_id)
                    REFERENCES public.series (id) MATCH SIMPLE
                    ON UPDATE NO ACTION
                    ON DELETE NO ACTION
            )
     */
    public static boolean insert(Connection connection, int id, int seriesId, String title, String format, String[] teams,
                         String outcome, String winningTeam, String venue, Long date, String status) throws SQLException {
        if (isAvailable(connection, id)) {
            System.out.println("Match with id = " + id + " is already available in DB");
            return false;
        }
        String SQL = "INSERT INTO match VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, seriesId);
        preparedStatement.setString(3, title);
        preparedStatement.setString(4, format);
        preparedStatement.setArray(5, connection.createArrayOf("TEXT", teams));
        preparedStatement.setString(6, outcome);
        preparedStatement.setString(7, winningTeam);
        preparedStatement.setString(8, venue);
        preparedStatement.setLong(9, date);
        preparedStatement.setString(10, status);

        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    public static boolean isAvailable(Connection connection, int id) throws SQLException {
        boolean isAvailable = false;
        String SQL = "SELECT count(*) FROM match WHERE match.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setInt(1, id);
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            isAvailable = 0 != resultSet.getInt(1);
            resultSet.close();
        }
        preparedStatement.close();
        return isAvailable;
    }

    public static void clear(Connection connection) throws SQLException {
        String SQL = "DELETE FROM match";
        Statement preparedStatement = connection.createStatement();
        int rows = preparedStatement.executeUpdate(SQL);
        preparedStatement.close();
        System.out.println(rows + " Rows Deleted from Match Table");
    }

}
