package rest.Query;

import Database.DatabaseEngine;
import rest.Response.TeamStatsBattingCommonResponse;
import rest.Response.TeamStatsRecentMatchesResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamStatsQuery {
    public enum CommonBattingStats {
        MOST_RUNS,
        MOST_FIFTIES,
        MOST_HUNDREDS,
        MOST_FOURS,
        MOST_SIXES,
        MOST_DUCKS
    }
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
    public static ArrayList<TeamStatsBattingCommonResponse> getTeamStatsBattingCommonStats(String teamName, String format,
                                                                                           String venue, int numMatches,
                                                                                           String againstTeam, CommonBattingStats statsType) {
        ArrayList<TeamStatsBattingCommonResponse> response = new ArrayList<>();
        if (againstTeam != null && againstTeam.equals(teamName)) {
            /* Do not proceed further the request if againstTeam is same as teamName */
            return response;
        }
        String sqlQuery = " with matches as (select id from match where ?=any(teams) ";
        if (againstTeam != null) sqlQuery += " and ?=any(teams) ";
        if (format != null) sqlQuery += " and format=? ";
        if (venue != null) sqlQuery += " and venue = ? ";
        sqlQuery += " order by match.date desc limit ?), " +
                "     players as (select player.name, innings_num, runs_scored, balls_played, num_fours, num_sixes, " +
                "                        (runs_scored >= 50) as is_fifty, (runs_scored >= 100) as is_hundred, " +
                "                        (runs_scored = 0 and status!='not out') as is_duck from player_batting_score " +
                "                 join player on player.id = player_batting_score.player_id " +
                "                 join matches on matches.id = player_batting_score.match_id " +
                "                 where player_batting_score.team_played_for = ? ";
        if (againstTeam != null) sqlQuery += " and player_batting_score.team_played_against = ? ";
        sqlQuery += ") select players.name, COUNT(players.innings_num) as num_innings, SUM(players.runs_scored) as runs_scored, " +
                "        MAX(players.runs_scored) as high_score, " +
                "        SUM(players.balls_played) as balls_played, SUM(players.num_fours) as num_fours, " +
                "        SUM(players.num_sixes) as num_sixes, " +
                "        SUM(players.is_fifty::int) as num_fifties, SUM(players.is_hundred::int) as num_hundreds, " +
                "        SUM(players.is_duck::int) as num_ducks from players " +
                " group by players.name ";
        switch(statsType) {
            case MOST_RUNS: sqlQuery += " order by runs_scored desc, balls_played asc "; break;
            case MOST_FOURS: sqlQuery += " order by num_fours desc, runs_scored desc, balls_played asc ";break;
            case MOST_SIXES: sqlQuery += " order by num_sixes desc, runs_scored desc, balls_played asc ";break;
            case MOST_FIFTIES: sqlQuery += " order by num_fifties desc, runs_scored desc, balls_played asc ";break;
            case MOST_HUNDREDS: sqlQuery += " order by num_hundreds desc, runs_scored desc, balls_played asc ";break;
            case MOST_DUCKS: sqlQuery += " order by num_ducks desc, runs_scored desc, balls_played asc ";break;
        }

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
                preparedStatement.setString(++parameterIndex, teamName);
                if (againstTeam != null) preparedStatement.setString(++parameterIndex, againstTeam);
                {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int ballsPlayed = resultSet.getInt("balls_played");
                        if (ballsPlayed == 0) continue;
                        response.add(new TeamStatsBattingCommonResponse(
                                resultSet.getString("name"),
                                resultSet.getInt("num_innings"),
                                resultSet.getInt("runs_scored"),
                                ballsPlayed,
                                (new BigDecimal(resultSet.getInt("runs_scored")*100.0/ballsPlayed)).setScale(2, RoundingMode.FLOOR),
                                resultSet.getInt("high_score"),
                                resultSet.getInt("num_fours"),
                                resultSet.getInt("num_sixes"),
                                resultSet.getInt("num_ducks"),
                                resultSet.getInt("num_fifties"),
                                resultSet.getInt("num_hundreds")));
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
