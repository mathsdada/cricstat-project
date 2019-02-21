package rest.Query;

import Database.DatabaseEngine;
import rest.Response.TeamStatsBattingCommonResponse;
import rest.Response.TeamStatsBowlingCommonResponse;
import rest.Response.TeamStatsRecentMatchesResponse;

import java.math.BigDecimal;
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
        MOST_DUCKS,
        HIGH_STRIKE_RATE,
        HIGH_AVERAGE
    }

    public enum CommonBowlingStats {
        MOST_WICKETS,
        MOST_MAIDENS,
        MOST_FOUR_PLUS_WKTS,
        MOST_FIVE_PLUS_WKTS,
        BEST_AVERAGE,
        BEST_STRIKE_RATE,
        BEST_ECONOMY
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
                            String winningTeam = resultSet.getString("winning_team");
                            String status = resultSet.getString("status");
                            if (winningTeam != null && !teamName.equals(winningTeam)) status = "l";
                            match = new TeamStatsRecentMatchesResponse(teamName, new ArrayList<>(),
                                    resultSet.getString("outcome"),
                                    status, winningTeam,
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
                "                        (runs_scored = 0 and status!='not out') as is_duck, (status='not out') as is_not_out from player_batting_score " +
                "                 join player on player.id = player_batting_score.player_id " +
                "                 join matches on matches.id = player_batting_score.match_id " +
                "                 where balls_played != 0 and player_batting_score.team_played_for = ? ";
        if (againstTeam != null) sqlQuery += " and player_batting_score.team_played_against = ? ";
        sqlQuery += "), players_stats as (select players.name, COUNT(players.innings_num) as num_innings, SUM(players.runs_scored) as runs_scored, " +
                "                                MAX(players.runs_scored) as high_score, " +
                "                                SUM(players.balls_played) as balls_played, SUM(players.num_fours) as num_fours, " +
                "                                SUM(players.num_sixes) as num_sixes, " +
                "                                SUM(players.is_fifty::int) as num_fifties, SUM(players.is_hundred::int) as num_hundreds, " +
                "                                SUM(players.is_duck::int) as num_ducks, SUM(is_not_out::int) as num_not_outs from players " +
                "                         group by players.name) " +
                "   select *, round((runs_scored*100.0/balls_played), 2) as strike_rate, round(runs_scored*1.0/(num_innings-num_not_outs), 2) as average from players_stats " +
                "   where num_innings > num_not_outs ";

        switch(statsType) {
            case MOST_RUNS: sqlQuery += " order by runs_scored desc, balls_played asc "; break;
            case MOST_FOURS: sqlQuery += " order by num_fours desc, runs_scored desc, balls_played asc ";break;
            case MOST_SIXES: sqlQuery += " order by num_sixes desc, runs_scored desc, balls_played asc ";break;
            case MOST_FIFTIES: sqlQuery += " order by num_fifties desc, runs_scored desc, balls_played asc ";break;
            case MOST_HUNDREDS: sqlQuery += " order by num_hundreds desc, runs_scored desc, balls_played asc ";break;
            case MOST_DUCKS: sqlQuery += " order by num_ducks desc, runs_scored desc, balls_played asc ";break;
            case HIGH_STRIKE_RATE: sqlQuery += " order by strike_rate desc, runs_scored desc, balls_played asc ";break;
            case HIGH_AVERAGE: sqlQuery += " order by average desc, runs_scored desc, balls_played asc ";break;
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
//                System.out.println(preparedStatement.toString());
                {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        response.add(new TeamStatsBattingCommonResponse(
                                resultSet.getString("name"),
                                resultSet.getInt("num_innings"),
                                resultSet.getInt("runs_scored"),
                                resultSet.getInt("balls_played"),
                                resultSet.getBigDecimal("average"),
                                resultSet.getBigDecimal("strike_rate"),
                                resultSet.getInt("high_score"),
                                resultSet.getInt("num_fours"),
                                resultSet.getInt("num_sixes"),
                                resultSet.getInt("num_ducks"),
                                resultSet.getInt("num_fifties"),
                                resultSet.getInt("num_hundreds"),
                                resultSet.getInt("num_not_outs")));
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
    public static ArrayList<TeamStatsBowlingCommonResponse> getTeamStatsBowlingCommonStats(String teamName, String format,
                                                                                           String venue, int numMatches,
                                                                                           String againstTeam, CommonBowlingStats statsType) {
        ArrayList<TeamStatsBowlingCommonResponse> response = new ArrayList<>();
        if (againstTeam != null && againstTeam.equals(teamName)) {
            /* Do not proceed further the request if againstTeam is same as teamName */
            return response;
        }
        String sqlQuery = " with matches as (select id from match where ?=any(teams) ";
        if (againstTeam != null) sqlQuery += " and ?=any(teams) ";
        if (format != null) sqlQuery += " and format=? ";
        if (venue != null) sqlQuery += " and venue = ? ";
        sqlQuery += " order by match.date desc limit ?), " +
                "     players as (select player.name, innings_num, runs_given, overs_bowled, wickets_taken, num_maidens, " +
                "                        (wickets_taken >= 4) as is_four_plus, (wickets_taken >= 5) as is_five_plus from player_bowling_score " +
                "                 join player on player.id = player_bowling_score.player_id " +
                "                 join matches on matches.id = player_bowling_score.match_id " +
                "                 where player_bowling_score.team_played_for = ? ";
        if (againstTeam != null) sqlQuery += " and player_bowling_score.team_played_against = ? ";
        sqlQuery += "), players_stats as (select players.name, COUNT(players.innings_num) as num_innings, SUM(players.runs_given) as runs_given, " +
                "                                SUM(players.overs_bowled) as overs_bowled, SUM(players.wickets_taken) as wickets_taken, " +
                "                                SUM(players.num_maidens) as num_maidens, " +
                "                                SUM(players.is_four_plus::int) as num_four_plus, SUM(players.is_five_plus::int) as num_five_plus from players " +
                "                         group by players.name) " +
                "   select *, case when wickets_taken > 0 then round((runs_given*1.0/wickets_taken),2) else 0 end as average, " +
                "             case when wickets_taken > 0 then round(overs_bowled*6.0/wickets_taken,1) else 0 end as strike_rate, " +
                "             round((runs_given*1.0/overs_bowled),2) as economy from players_stats ";

        switch(statsType) {
            case MOST_WICKETS: sqlQuery += " order by wickets_taken desc, runs_given asc "; break;
            case MOST_MAIDENS: sqlQuery += " order by num_maidens desc, wickets_taken desc, runs_given asc ";break;
            case MOST_FOUR_PLUS_WKTS: sqlQuery += " order by num_four_plus desc, wickets_taken desc, runs_given asc ";break;
            case MOST_FIVE_PLUS_WKTS: sqlQuery += " order by num_five_plus desc, num_four_plus desc, wickets_taken desc, runs_given asc ";break;
            case BEST_AVERAGE: sqlQuery += " order by average asc, wickets_taken desc, runs_given asc ";break;
            case BEST_ECONOMY: sqlQuery += " order by economy asc, wickets_taken desc, runs_given asc ";break;
            case BEST_STRIKE_RATE: sqlQuery += " order by strike_rate asc, wickets_taken desc, runs_given asc ";break;
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
                        BigDecimal average = resultSet.getBigDecimal("average");
                        BigDecimal strikeRate = resultSet.getBigDecimal("strike_rate");
                        if (average.equals(new BigDecimal("0")) || strikeRate.equals(new BigDecimal("0"))) {
                            continue;
                        }
                        response.add(new TeamStatsBowlingCommonResponse(
                                resultSet.getString("name"),
                                resultSet.getInt("num_innings"),
                                resultSet.getBigDecimal("overs_bowled"),
                                resultSet.getInt("num_maidens"),
                                resultSet.getInt("runs_given"),
                                resultSet.getInt("wickets_taken"),
                                average,
                                resultSet.getBigDecimal("economy"),
                                strikeRate,
                                resultSet.getInt("num_four_plus"),
                                resultSet.getInt("num_five_plus")));
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
