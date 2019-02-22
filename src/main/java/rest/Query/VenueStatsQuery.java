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

    public static ArrayList<AvgInningsScore> getVenueAveragePerInningsScore(String venueName, String format,
                                                                            int numMatches) {
        ArrayList<AvgInningsScore> response = new ArrayList<>();
        String sqlQuery = "" +
                " with matches as (select id, winning_team from match " +
                "                  where match.venue = ? ";
                if (format != null) sqlQuery += "                        and match.format = ? ";
                sqlQuery += "                  order by match.date desc limit ?), " +
                "      innings_data as (select innings_num, runs, wickets, (batting_team = matches.winning_team) as is_win from innings_score " +
                "                       join matches on matches.id = innings_score.match_id) " +
                " select innings_num, count(innings_num) as num_innings, round(avg(runs))::int as runs, " +
                "        round(avg(wickets),1)::int as wickets, round(100*avg(is_win::int))::int as win_percent " +
                "        from innings_data " +
                " group by innings_num order by innings_num asc";
        try {
            int parameterIndex = 0;
            Connection connection = DatabaseEngine.getInstance().getConnection();
            {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(++parameterIndex, venueName);
                if (format != null) preparedStatement.setString(++parameterIndex, format);
                preparedStatement.setInt(++parameterIndex, numMatches);
                {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        response.add(new AvgInningsScore(
                                resultSet.getInt("innings_num"),
                                resultSet.getInt("num_innings"),
                                resultSet.getInt("runs"),
                                resultSet.getInt("wickets"),
                                resultSet.getInt("win_percent")));
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

    public static class AvgInningsScore {
        private final int inningsNum;
        private final int numInnings;
        private final int runs;
        private final int wickets;
        private final int winPercentage;

        AvgInningsScore(int inningsNum, int numInnings, int runs, int wickets, int winPercentage) {
            this.inningsNum = inningsNum;
            this.numInnings = numInnings;
            this.runs = runs;
            this.wickets = wickets;
            this.winPercentage = winPercentage;
        }

        public int getInningsNum() {
            return inningsNum;
        }

        public int getNumInnings() {
            return numInnings;
        }

        public int getRuns() {
            return runs;
        }

        public int getWickets() {
            return wickets;
        }

        public int getWinPercentage() {
            return winPercentage;
        }
    }
}
