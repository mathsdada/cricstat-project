package Database;

import Database.Common.Pair;
import Database.Common.StringUtils;
import Database.Model.*;
import Database.Scraper.ScheduleMatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Controller {
    public static void updateStatsDatabase() {
        Connection connection = null;
        try {
            Season season = Database.Scraper.Season.build("2019");
            connection = DatabaseEngine.getInstance().getConnection();
            for (Series series: season.getSeriesList()) {
                Database.Tables.Series.insert(connection, series.getId(), series.getTitle(), StringUtils.getGender(series.getTitle()));
                for (Match match : series.getMatches()) {
                    /* do not fill other tables if Match table insertion fails */
                    if (!Database.Tables.Match.insert(connection, match.getId(), series.getId(), match.getTitle(),
                            match.getFormat(), new String[]{match.getTeams().get(0).getName(), match.getTeams().get(1).getName()},
                            match.getOutcome(), match.getWinningTeam(), match.getVenue(), match.getDate(), match.getStatus())) {
                        continue;
                    }
                    for (InningsScore inningsScore : match.getInningsScores()) {
                        Database.Tables.InningsScore.insert(connection, match.getId(), inningsScore.getInningsNum(),
                                inningsScore.getBattingTeam().getName(), inningsScore.getBowlingTeam().getName(),
                                inningsScore.getScoreHeader().getRuns(), inningsScore.getScoreHeader().getWickets(),
                                inningsScore.getScoreHeader().getOvers());
                        for (PlayerBattingScore playerBattingScore: inningsScore.getPlayerBattingScores()) {
                            Player batsman = playerBattingScore.getBatsman();
                            Player bowler = playerBattingScore.getBowler();
                            int bowlerId = 0;
                            Database.Tables.Player.insert(connection, batsman.getId(), batsman.getName(), batsman.getRole(),
                                    batsman.getBattingStyle(), batsman.getBowlingStyle());
                            if (bowler != null) {
                                Database.Tables.Player.insert(connection, bowler.getId(), bowler.getName(), bowler.getRole(),
                                        bowler.getBattingStyle(), bowler.getBowlingStyle());
                                bowlerId = bowler.getId();
                            }
                            Database.Tables.PlayerBattingScore.insert(connection, batsman.getId(), match.getId(),
                                    inningsScore.getInningsNum(), inningsScore.getBattingTeam().getName(), inningsScore.getBowlingTeam().getName(),
                                    playerBattingScore.getRuns(), playerBattingScore.getBalls(),
                                    playerBattingScore.getFours(), playerBattingScore.getSixes(),
                                    playerBattingScore.getStrikeRate(), playerBattingScore.getStatus(),
                                    bowlerId);
                        }
                        for (PlayerBowlingScore playerBowlingScore: inningsScore.getPlayerBowlingScores()) {
                            Player bowler = playerBowlingScore.getPlayer();
                            Database.Tables.Player.insert(connection, bowler.getId(), bowler.getName(), bowler.getRole(),
                                    bowler.getBattingStyle(), bowler.getBowlingStyle());
                            Database.Tables.PlayerBowlingScore.insert(connection, bowler.getId(), match.getId(),
                                    inningsScore.getInningsNum(), inningsScore.getBowlingTeam().getName(), inningsScore.getBattingTeam().getName(),
                                    playerBowlingScore.getOvers(), playerBowlingScore.getMaidens(),
                                    playerBowlingScore.getRuns(), playerBowlingScore.getWickets(),
                                    playerBowlingScore.getNoBalls(), playerBowlingScore.getWides(),
                                    playerBowlingScore.getEconomy());
                        }

                    }
                    for (HeadToHead headToHead : match.getHeadToHeadList()) {
                        Pair<Player, Team> batsman = headToHead.getBatsman();
                        Pair<Player, Team> bowler = headToHead.getBowler();
                        Database.Tables.Player.insert(connection, batsman.getFirst().getId(), batsman.getFirst().getName(),
                                batsman.getFirst().getRole(), batsman.getFirst().getBattingStyle(),
                                batsman.getFirst().getBowlingStyle());
                        Database.Tables.Player.insert(connection, bowler.getFirst().getId(), bowler.getFirst().getName(),
                                bowler.getFirst().getRole(), bowler.getFirst().getBattingStyle(),
                                bowler.getFirst().getBowlingStyle());
                        Database.Tables.HeadToHead.insert(connection, batsman.getFirst().getId(), batsman.getSecond().getName(),
                                bowler.getFirst().getId(), bowler.getSecond().getName(),
                                match.getId(), headToHead.getInningsNum(),
                                headToHead.getHeadToHeadData().getBalls(), headToHead.getHeadToHeadData().getRuns(),
                                headToHead.getHeadToHeadData().getWicket(), headToHead.getHeadToHeadData().getDotBalls(),
                                headToHead.getHeadToHeadData().getFours(), headToHead.getHeadToHeadData().getSixes(),
                                headToHead.getHeadToHeadData().getNoBall());
                    }
                }
            }
            DatabaseEngine.getInstance().releaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateScheduleDatabase() {
        /* "TUE, FEB 26 2019"; */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd YYYY");
        String date = formatter.format(LocalDate.now()).toLowerCase();

        try {
            Connection connection = DatabaseEngine.getInstance().getConnection();
            ArrayList<Database.Model.ScheduleMatch> matches = ScheduleMatch.build(date);
            Database.Tables.Schedule.clear(connection);
            for (Database.Model.ScheduleMatch scheduleMatch: matches) {
                Match match = scheduleMatch.getMatch();
                Database.Tables.Schedule.insert(connection, match.getId(), match.getTitle(), match.getFormat(),
                        match.getVenue(), match.getDate(), match.getTeams(), scheduleMatch.getSeries(), scheduleMatch.getCategory());
            }
            DatabaseEngine.getInstance().releaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
