package rest;

import rest.Query.PlayerBattingStatsQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rest.Query.PlayerBowlingStatsQuery;
import rest.Query.TeamStatsQuery;
import rest.Response.PlayerBattingStatsResponse;
import rest.Response.PlayerBowlingStatsResponse;
import rest.Response.TeamStatsBattingCommonResponse;
import rest.Response.TeamStatsRecentMatchesResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class RestApiController {

    @RequestMapping("/player_batting_stats")
    public ArrayList<PlayerBattingStatsResponse> playerBattingStats(
            @RequestParam(value = "name", required = true) String playerName,
            @RequestParam(value = "team_played_for", required = false) String teamPlayedFor,
            @RequestParam(value = "against_team", required = false) String againstTeam,
            @RequestParam(value = "against_bowler", required = false) String againstBowler,
            @RequestParam(value = "against_bowling_style", required = false) String againstBowlingStyle,
            @RequestParam(value = "format", required = false) String format,
            @RequestParam(value = "venue", required = false) String venue,
            @RequestParam(value = "num_matches", defaultValue = "10") int numMatches) {
        if (againstBowler == null && againstBowlingStyle == null) {
            return PlayerBattingStatsQuery.getPlayerBattingStats(playerName, format, venue,
                    teamPlayedFor, againstTeam, numMatches);
        } else {
            return PlayerBattingStatsQuery.getPlayerBattingStatsAgainst(playerName, format, venue, numMatches,
                    teamPlayedFor, againstTeam, againstBowler, againstBowlingStyle);
        }
    }

    @RequestMapping("/player_bowling_stats")
    public ArrayList<PlayerBowlingStatsResponse>  playerBowlingStats(
            @RequestParam(value = "name", required = true) String playerName,
            @RequestParam(value = "format", required = false) String format,
            @RequestParam(value = "venue", required = false) String venue,
            @RequestParam(value = "num_matches", defaultValue = "10") int numMatches,
            @RequestParam(value = "team_played_for", required = false) String teamPlayedFor,
            @RequestParam(value = "against_team", required = false) String againstTeam,
            @RequestParam(value = "against_batsman", required = false) String againstBatsman,
            @RequestParam(value = "against_batting_style", required = false) String againstBattingStyle) {
        if (againstBatsman == null && againstBattingStyle == null) {
            return PlayerBowlingStatsQuery.getPlayerBowlingStats(playerName, format, venue, teamPlayedFor,
                    againstTeam, numMatches);
        } else {
            return PlayerBowlingStatsQuery.getPlayerBowlingStatsAgainst(playerName, format, venue, numMatches,
                    teamPlayedFor, againstTeam, againstBatsman, againstBattingStyle);
        }
    }

    @RequestMapping("/team_stats/recent_matches")
    public ArrayList<TeamStatsRecentMatchesResponse> teamStatsRecentMatches(
            @RequestParam(value = "name", required = true) String teamName,
            @RequestParam(value = "format", required = false) String format,
            @RequestParam(value = "venue", required = false) String venue,
            @RequestParam(value = "num_matches", defaultValue = "10") int numMatches,
            @RequestParam(value = "against_team", required = false) String againstTeam) {
        return TeamStatsQuery.getTeamStatsRecentMatches(teamName, format, venue, numMatches, againstTeam);
    }

    @RequestMapping({
            "/team_stats/batting/most_runs",
            "/team_stats/batting/most_fours",
            "/team_stats/batting/most_sixes",
            "/team_stats/batting/most_fifties",
            "/team_stats/batting/most_hundreds",
            "/team_stats/batting/most_ducks",
            "/team_stats/batting/highest_strike_rate",
            "/team_stats/batting/highest_average"})
    public ArrayList<TeamStatsBattingCommonResponse> teamStatsBattingCommon(
            HttpServletRequest request,
            @RequestParam(value = "name", required = true) String teamName,
            @RequestParam(value = "format", required = false) String format,
            @RequestParam(value = "venue", required = false) String venue,
            @RequestParam(value = "num_matches", defaultValue = "10") int numMatches,
            @RequestParam(value = "against_team", required = false) String againstTeam
    ) {
        TeamStatsQuery.CommonBattingStats statsType;
        switch (request.getRequestURI()) {
            case "/team_stats/batting/most_runs":
                statsType = TeamStatsQuery.CommonBattingStats.MOST_RUNS; break;
            case "/team_stats/batting/most_fours":
                statsType = TeamStatsQuery.CommonBattingStats.MOST_FOURS; break;
            case "/team_stats/batting/most_sixes":
                statsType = TeamStatsQuery.CommonBattingStats.MOST_SIXES; break;
            case "/team_stats/batting/most_fifties":
                statsType = TeamStatsQuery.CommonBattingStats.MOST_FIFTIES; break;
            case "/team_stats/batting/most_hundreds":
                statsType = TeamStatsQuery.CommonBattingStats.MOST_HUNDREDS; break;
            case "/team_stats/batting/most_ducks":
                statsType = TeamStatsQuery.CommonBattingStats.MOST_DUCKS; break;
            case "/team_stats/batting/highest_strike_rate":
                statsType = TeamStatsQuery.CommonBattingStats.HIGH_STRIKE_RATE; break;
            case "/team_stats/batting/highest_average":
                statsType = TeamStatsQuery.CommonBattingStats.HIGH_AVERAGE; break;
            default: return new ArrayList<>();
        }
        return TeamStatsQuery.getTeamStatsBattingCommonStats(teamName, format, venue, numMatches, againstTeam, statsType);
    }
}
