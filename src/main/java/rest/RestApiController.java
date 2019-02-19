package rest;

import rest.Query.PlayerBattingStatsQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RestApiController {

    @RequestMapping("/player_batting_stats")
    public ArrayList<PlayerBattingStatsResponse> playerBattingStats(@RequestParam(value = "name", required = true) String playerName,
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
}
