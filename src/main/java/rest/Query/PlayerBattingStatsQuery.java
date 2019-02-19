package rest.Query;

import Database.DatabaseEngine;
import rest.PlayerBattingStatsResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerBattingStatsQuery {
    public static ArrayList<PlayerBattingStatsResponse> getPlayerBattingStats(String name, String format, String venue,
                                                                              String teamPlayedFor, String teamPlayedAgainst,
                                                                              int numMatches) {
        ArrayList<PlayerBattingStatsResponse> response = new ArrayList<>();
        String sqlQuery =
                "select player.name, player_batting_score.runs_scored, player_batting_score.strike_rate, player_batting_score.status, " +
                        " player_batting_score.num_fours, player_batting_score.num_sixes, " +
                        " player_batting_score.team_played_for, player_batting_score.team_played_against, " +
                        " match.format, match.venue, match.date from player_batting_score " +
                " join player on player.id = player_batting_score.player_id " +
                " join match on match.id = player_batting_score.match_id " +
                " where player.name = ? ";
        if (format != null) sqlQuery += " and match.format = ? ";
        if (venue != null) sqlQuery += " and match.venue = ? ";
        if (teamPlayedFor != null) sqlQuery += " and player_batting_score.team_played_for = ? ";
        if (teamPlayedAgainst != null) sqlQuery += " and player_batting_score.team_played_against = ? ";
        sqlQuery += " order by match.date desc limit ? ";

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
                    boolean isNotOut = false;
                    if (resultSet.getString("status").equals("not out")) isNotOut = true;
                    String queriedCategoryId = null, queriedCategoryVal = null;
                    if (teamPlayedAgainst != null) {
                        queriedCategoryId = "team";
                        queriedCategoryVal = teamPlayedAgainst;
                    }
                    response.add(new PlayerBattingStatsResponse(
                            resultSet.getString("name"),
                            resultSet.getInt("runs_scored"),
                            resultSet.getBigDecimal("strike_rate"), isNotOut,
                            resultSet.getInt("num_fours"), resultSet.getInt("num_sixes"),
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

    public static ArrayList<PlayerBattingStatsResponse>
        getPlayerBattingStatsAgainst(String name, String format, String venue, int numMatches,
                                     String teamPlayedFor,String teamPlayedAgainst, String againstBowler, String againstBowlingStyle) {
            ArrayList<PlayerBattingStatsResponse> response = new ArrayList<>();
            String sqlQuery =
                    " select batsman.name as name," +
                            " batsman_to_bowler.num_runs as runs_scored, " +
                            " batsman_to_bowler.num_balls, " +
                            " not(batsman_to_bowler.num_wickets::boolean) as is_not_out," +
                            " batsman_to_bowler.num_fours, " +
                            " batsman_to_bowler.num_sixes, " +
                            " match.format, match.venue, match.date, " +
                            " batsman_to_bowler.batsman_team as team_played_for, " +
                            " batsman_to_bowler.bowler_team as team_played_against, " +
                            " bowler.name as against_bowler, bowler.bowling_style as against_bowling_style, " +
                            " match.title from batsman_to_bowler" +
                    " join player as batsman on batsman.id = batsman_to_bowler.batsman_id " +
                            " join player as bowler on bowler.id = batsman_to_bowler.bowler_id " +
                            " join match on match.id = batsman_to_bowler.match_id " +
                    " where (batsman_to_bowler.num_balls > 0) and batsman.name = ?";
            if (format != null) sqlQuery += " and match.format = ? ";
            if (venue != null) sqlQuery += " and match.venue = ? ";
            if (teamPlayedFor != null) sqlQuery += " and batsman_to_bowler.batsman_team = ? ";
            if (teamPlayedAgainst != null) sqlQuery += " and batsman_to_bowler.bowler_team = ? ";
            if (againstBowler != null) sqlQuery += " and bowler.name = ? ";
            if (againstBowlingStyle != null) sqlQuery += " and bowler.bowling_style = ? ";
            sqlQuery += " order by match.date desc, batsman_to_bowler.innings_num desc limit ? ";

            try {
                int parameterIndex = 0;
                Connection dbConnection = DatabaseEngine.getInstance().getConnection();
                PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery);

                preparedStatement.setString(++parameterIndex, name);
                if (format != null) preparedStatement.setString(++parameterIndex, format);
                if (venue != null) preparedStatement.setString(++parameterIndex, venue);
                if (teamPlayedFor != null) preparedStatement.setString(++parameterIndex, teamPlayedFor);
                if (teamPlayedAgainst != null) preparedStatement.setString(++parameterIndex, teamPlayedAgainst);
                if(againstBowler != null) preparedStatement.setString(++parameterIndex, againstBowler);
                if(againstBowlingStyle != null) preparedStatement.setString(++parameterIndex, againstBowlingStyle);
                preparedStatement.setInt(++parameterIndex, numMatches);
                System.out.println(preparedStatement.toString());
                {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        BigDecimal strikeRate = new BigDecimal(
                                resultSet.getInt("runs_scored")*100.0/resultSet.getInt("num_balls"));
                        response.add(new PlayerBattingStatsResponse(
                                resultSet.getString("name"),
                                resultSet.getInt("runs_scored"),
                                strikeRate.setScale(2, RoundingMode.FLOOR),
                                resultSet.getBoolean("is_not_out"),
                                resultSet.getInt("num_fours"), resultSet.getInt("num_sixes"),
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
