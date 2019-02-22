package rest.Response;

import java.util.ArrayList;

public class TeamStatsRecentMatchesResponse {
    private final String name;
    private ArrayList<PerInningsMatchScore> inningsScores;
    private final String outcome;
    private final String status;
    private final String winningTeam;
    private final String format;
    private final String venue;
    private final Long date;

    public TeamStatsRecentMatchesResponse(String name, ArrayList<PerInningsMatchScore> inningsScores, String outcome, String status, String winningTeam, String format, String venue, Long date) {
        this.name = name;
        this.inningsScores = inningsScores;
        this.outcome = outcome;
        this.status = status;
        this.winningTeam = winningTeam;
        this.format = format;
        this.venue = venue;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public ArrayList<PerInningsMatchScore> getInningsScores() {
        return inningsScores;
    }

    public void addInningsScore(PerInningsMatchScore inningsScore) {
        inningsScores.add(inningsScore);
    }

    public String getOutcome() {
        return outcome;
    }

    public String getStatus() {
        return status;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public String getFormat() {
        return format;
    }

    public String getVenue() {
        return venue;
    }

    public Long getDate() {
        return date;
    }
}
