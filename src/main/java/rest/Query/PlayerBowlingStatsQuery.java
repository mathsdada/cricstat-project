package rest.Query;

import Database.DatabaseEngine;
import rest.Response.PlayerBowlingStatsResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerBowlingStatsQuery {
    public static ArrayList<PlayerBowlingStatsResponse> getPlayerBowlingStats(String name, String format, String venue,
                                                                              String teamPlayedFor, String teamPlayedAgainst,
                                                                              int numMatches) {
        ArrayList<PlayerBowlingStatsResponse> response = new ArrayList<>();
        String sqlQuery =
                "select player.name, player_bowling_score.runs_given, player_bowling_score.overs_bowled, " +
                        " player_bowling_score.wickets_taken, player_bowling_score.economy, " +
                        " player_bowling_score.team_played_for, player_bowling_score.team_played_against, " +
                        " match.format, match.venue, match.date from player_bowling_score " +
                " join player on player.id = player_bowling_score.player_id " +
                " join match on match.id = player_bowling_score.match_id " +
                " where player.name = ? ";
        if (format != null) sqlQuery += " and match.format = ? ";
        if (venue != null) sqlQuery += " and match.venue = ? ";
        if (teamPlayedFor != null) sqlQuery += " and player_bowling_score.team_played_for = ? ";
        if (teamPlayedAgainst != null) sqlQuery += " and player_bowling_score.team_played_against = ? ";
        sqlQuery += " order by match.date desc, player_bowling_score.innings_num limit ? ";

        try {
            int parameterIndex = 0;
            Connection dbConnection = DatabaseEngine.getInstance().getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery);

            preparedStatement.setString(++parameterIndex, name);
            if (format != null) preparedStatement.setString(++parameterIndex, format);
            if (venue != null) preparedStatement.setString(++parameterIndex, venue);
            if (teamPlayedFor != null) preparedStatement.setString(++parameterIndex, teamPlayedFor);
            if (teamPlayedAgainst != null) preparedStatement.setString(++parameterIndex, teamPlayedAgainst);
            preparedStatement.setInt(++parameterIndex, numMatches);

            {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    response.add(new PlayerBowlingStatsResponse(
                            resultSet.getString("name"),
                            resultSet.getInt("wickets_taken"), resultSet.getInt("runs_given"),
                            resultSet.getBigDecimal("overs_bowled"), resultSet.getBigDecimal("economy"),
                            resultSet.getString("format"),
                            resultSet.getString("venue"),
                            resultSet.getLong("date"),
                            resultSet.getString("team_played_for"),
                            resultSet.getString("team_played_against")));
                }
                resultSet.close();
            }
            preparedStatement.close();
            DatabaseEngine.getInstance().releaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static ArrayList<PlayerBowlingStatsResponse>
    getPlayerBowlingStatsAgainst(String name, String format, String venue, int numMatches,
                                 String teamPlayedFor, String teamPlayedAgainst, String againstBatsman, String againstBattingStyle) {
            ArrayList<PlayerBowlingStatsResponse> response = new ArrayList<>();
            String sqlQuery = "" +
                    "select  bowler.name as name, " +
                        " SUM(batsman_to_bowler.num_runs) as runs_given, " +
                        " SUM(batsman_to_bowler.num_balls) as balls_bowled, " +
                        " SUM(batsman_to_bowler.num_wickets) as wickets_taken, " +
                        " match.format, match.venue, match.date, " +
                        " batsman_to_bowler.bowler_team as team_played_for, " +
                        " batsman_to_bowler.batsman_team as team_played_against from batsman_to_bowler " +
                    " join player as batsman on batsman.id = batsman_to_bowler.batsman_id " +
                        " join player as bowler on bowler.id = batsman_to_bowler.bowler_id " +
                        " join match on match.id = batsman_to_bowler.match_id " +
                    " where (batsman_to_bowler.num_balls > 0) and bowler.name = ? ";
            if (format != null) sqlQuery += " and match.format = ? ";
            if (venue != null) sqlQuery += " and match.venue = ? ";
            if (teamPlayedFor != null) sqlQuery += " and batsman_to_bowler.bowler_team = ? ";
            if (teamPlayedAgainst != null) sqlQuery += " and batsman_to_bowler.batsman_team = ? ";
            if (againstBatsman != null) sqlQuery += " and batsman.name = ? ";
            if (againstBattingStyle != null) sqlQuery += " and batsman.batting_style = ? ";
            sqlQuery += " group by batsman_to_bowler.innings_num, date, format, venue, bowler.name, team_played_for, team_played_against " +
                        " order by match.date desc, batsman_to_bowler.innings_num desc limit ? ";

            try {
                int parameterIndex = 0;
                Connection dbConnection = DatabaseEngine.getInstance().getConnection();
                PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery);

                preparedStatement.setString(++parameterIndex, name);
                if (format != null) preparedStatement.setString(++parameterIndex, format);
                if (venue != null) preparedStatement.setString(++parameterIndex, venue);
                if (teamPlayedFor != null) preparedStatement.setString(++parameterIndex, teamPlayedFor);
                if (teamPlayedAgainst != null) preparedStatement.setString(++parameterIndex, teamPlayedAgainst);
                if(againstBatsman != null) preparedStatement.setString(++parameterIndex, againstBatsman);
                if(againstBattingStyle != null) preparedStatement.setString(++parameterIndex, againstBattingStyle);
                preparedStatement.setInt(++parameterIndex, numMatches);
                System.out.println(preparedStatement.toString());
                {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int ballsBowled = resultSet.getInt("balls_bowled");
                        BigDecimal economy = new BigDecimal(
                                resultSet.getInt("runs_given")*6.0/ballsBowled);
                        BigDecimal oversBowled = new BigDecimal(ballsBowled/6 +"."+ballsBowled%6);
                        response.add(new PlayerBowlingStatsResponse(
                                resultSet.getString("name"),
                                resultSet.getInt("wickets_taken"),
                                resultSet.getInt("runs_given"),
                                oversBowled,
                                economy.setScale(2, RoundingMode.FLOOR),
                                resultSet.getString("format"),
                                resultSet.getString("venue"),
                                resultSet.getLong("date"),
                                resultSet.getString("team_played_for"), resultSet.getString("team_played_against")));
                    }
                    resultSet.close();
                }
                preparedStatement.close();
                DatabaseEngine.getInstance().releaseConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return response;
    }
}
