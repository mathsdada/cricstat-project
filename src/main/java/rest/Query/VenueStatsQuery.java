package rest.Query;

import Database.DatabaseEngine;
import rest.Response.PerInningsMatchScore;
import rest.Response.VenueStatsRecentMatchesResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VenueStatsQuery {
    public static ArrayList<VenueStatsRecentMatchesResponse> getVenueStatsRecentMatches(String venueName, String format,
                                                                                        int numMatches) {
        ArrayList<VenueStatsRecentMatchesResponse> response = new ArrayList<>();
        String sqlQuery = "" +
                " with matches as (select id, title, format, outcome, date from match " +
                "                  where match.venue = ? ";
        if (format != null) sqlQuery += " and format = ? ";
        sqlQuery += " order by match.date desc, match.id desc limit ?) " +
                    " select id, title, format, outcome, date, innings_num, batting_team, bowling_team, runs, wickets, overs from matches " +
                    " join innings_score on innings_score.match_id = matches.id " +
                    " order by matches.date desc, matches.id desc, innings_num asc";
        try {
            int parameterIndex = 0;
            Connection connection = DatabaseEngine.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(++parameterIndex, venueName);
            if (format != null) preparedStatement.setString(++parameterIndex, format);
            preparedStatement.setInt(++parameterIndex, numMatches);
            {
                ResultSet resultSet = preparedStatement.executeQuery();
                int lastMatchId = Integer.MAX_VALUE;
                VenueStatsRecentMatchesResponse match = null;
                while (resultSet.next()) {
                    int currentMatchId = resultSet.getInt("id");
                    PerInningsMatchScore perInningsMatchScore =
                            new PerInningsMatchScore(
                                    resultSet.getInt("innings_num"),
                                    resultSet.getString("batting_team"),
                                    resultSet.getString("bowling_team"),
                                    resultSet.getInt("runs"),
                                    resultSet.getInt("wickets"),
                                    resultSet.getBigDecimal("overs")
                            );
                    if ((lastMatchId == Integer.MAX_VALUE) || (lastMatchId != currentMatchId)) {
                        if (match != null) {
                            response.add(match);
                        }
                        match = new VenueStatsRecentMatchesResponse(
                                resultSet.getString("title"),
                                resultSet.getString("outcome"),
                                resultSet.getString("format"),
                                resultSet.getLong("date"),
                                new ArrayList<>());
                    }
                    match.addInningsScore(perInningsMatchScore);
                    lastMatchId = currentMatchId;
                }
                if (match != null) response.add(match);
                resultSet.close();
            }
            DatabaseEngine.getInstance().releaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }
}
