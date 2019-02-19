package Database;

import java.sql.*;

public class Player {
    /* Schema
     * CREATE TABLE public.player
        (
            id integer NOT NULL,
            name text NOT NULL,
            role text,
            batting_style text,
            bowling_style text,
            PRIMARY KEY (id)
        )
      * */
    /* TODO provide update API so that if any of [role, batting_style, bowling_style] are '--' in the DB,
            then they can be updated */
    public static void insert(Connection connection, int id, String name, String role,
                              String battingStyle, String bowlingStyle) throws SQLException {
        if (isAvailable(connection, id)) {
            System.out.println("Player with id = " + id + " is already available in DB");
            return;
        }
        String SQL = "INSERT INTO player VALUES(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, role);
        preparedStatement.setString(4, battingStyle);
        preparedStatement.setString(5, bowlingStyle);

        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    private static boolean isAvailable(Connection connection, int id) throws SQLException {
        boolean isAvailable = false;
        String SQL = "SELECT count(*) FROM player WHERE player.id = ?";
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
        String SQL = "DELETE FROM player";
        Statement preparedStatement = connection.createStatement();
        int rows = preparedStatement.executeUpdate(SQL);
        preparedStatement.close();
        System.out.println(rows + " Rows Deleted from Player Table");
    }
}
