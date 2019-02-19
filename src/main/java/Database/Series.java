package Database;

import java.sql.*;

public class Series {
    /* Schema
    *   CREATE TABLE public.series
        (
            id integer NOT NULL,
            title text NOT NULL,
            gender text NOT NULL,
            PRIMARY KEY (id)
        )
    * */
    public static void insert(Connection connection, int id, String title, String gender) throws SQLException {
        if (isAvailable(connection, id)) {
            System.out.println("Series with id = " + id + " is already available in DB");
            return;
        }
        String SQL = "INSERT INTO series VALUES(?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, title);
        preparedStatement.setString(3, gender);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private static boolean isAvailable(Connection connection, int id) throws SQLException {
        boolean isAvailable = false;
        String SQL = "SELECT count(*) FROM series WHERE series.id = ?";
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
        String SQL = "DELETE FROM series";
        Statement preparedStatement = connection.createStatement();
        int rows = preparedStatement.executeUpdate(SQL);
        preparedStatement.close();
        System.out.println(rows + " Rows Deleted from Series Table");
    }
}
