package rest.Query;

import Database.DatabaseEngine;
import rest.Response.TeamStatsRecentMatchesResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamStatsQuery {
    public static ArrayList<TeamStatsRecentMatchesResponse> getTeamStatsRecentMatches(String teamName, String format,
                                                                                      String venue, int numMatches,
                                                                                      String againstTeam) {
        ArrayList<TeamStatsRecentMatchesResponse> response = new ArrayList<>();
        if (againstTeam != null && againstTeam.equals(teamName)) {
            /* Do not proceed further the request if againstTeam is same as teamName */
            return response;
        }
        String sqlQuery = "" +
                "select id, innings_num, batting_team, bowling_team, runs, wickets, overs, outcome, status, " +
                        " winning_team, format, venue, date from match " +
                " join innings_score on innings_score.match_id = match.id where ?=any(teams) ";
        if (againstTeam != null) sqlQuery += " and ?=any(teams) ";
        if (format != null) sqlQuery += " and format = ? ";
        if (venue != null) sqlQuery += " and venue = ? ";
        sqlQuery += " order by match.date desc, innings_num asc limit ?";

        try {
            int parameterIndex = 0;
            Connection connection = DatabaseEngine.getInstance().getConnection();
            {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(++parameterIndex, teamName);
                if (againstTeam != null) preparedStatement.setString(++parameterIndex, againstTeam);
                if (format != null) preparedStatement.setString(++parameterIndex, format);
                if (venue != null) preparedStatement.setString(++parameterIndex, venue);
                preparedStatement.setInt(++parameterIndex, numMatches);
                {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    int lastMatchId = Integer.MAX_VALUE;
                    TeamStatsRecentMatchesResponse match = null;
                    while (resultSet.next()) {
                        int currentMatchId = resultSet.getInt("id");
                        TeamStatsRecentMatchesResponse.PerInningsMatchScore perInningsMatchScore =
                                new TeamStatsRecentMatchesResponse.PerInningsMatchScore(
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
                            match = new TeamStatsRecentMatchesResponse(teamName, new ArrayList<>(),
                                    resultSet.getString("outcome"),
                                    resultSet.getString("status"),
                                    resultSet.getString("winning_team"),
                                    resultSet.getString("format"),
                                    resultSet.getString("venue"),
                                    resultSet.getLong("date"));
                        }
                        match.addInningsScore(perInningsMatchScore);
                        lastMatchId = currentMatchId;
                    }
                    if (match != null) response.add(match);
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
