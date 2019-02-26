package rest.Query;

import Database.DatabaseEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import rest.Response.ScheduleResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScheduleQuery {
    public static ArrayList<ScheduleResponse> getSchedule(String category) {
        ArrayList<ScheduleResponse> response = new ArrayList<>();
        Gson gson = new Gson();
        String sqlQuery = " select * from schedule ";
        if (category != null) sqlQuery += " where category = ? ";

        try {
            Connection connection = DatabaseEngine.getInstance().getConnection();
            {
                int parameterIndex = 0;
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                if (category != null) preparedStatement.setString(++parameterIndex, category);
                {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        response.add(new ScheduleResponse(
                                resultSet.getString("title"),
                                resultSet.getString("format"),
                                resultSet.getString("venue"),
                                resultSet.getLong("date"),
                                gson.fromJson(resultSet.getString("teams"),
                                        new TypeToken<ArrayList<ScheduleResponse.ScheduleTeam>>(){}.getType()),
                                resultSet.getString("series")));
                    }
                    resultSet.close();
                }
                preparedStatement.close();
            }
            DatabaseEngine.getInstance().releaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }
}
