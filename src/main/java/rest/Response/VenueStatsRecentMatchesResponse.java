package rest.Response;

import java.util.ArrayList;

public class VenueStatsRecentMatchesResponse {
    private final String title;
    private final String outcome;
    private final String format;
    private final Long date;
    private ArrayList<PerInningsMatchScore> inningsScores;

    public VenueStatsRecentMatchesResponse(String title, String outcome, String format, Long date,
                                           ArrayList<PerInningsMatchScore> inningsScores) {
        this.title = title;
        this.outcome = outcome;
        this.format = format;
        this.date = date;
        this.inningsScores = inningsScores;
    }

    public String getTitle() {
        return title;
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

    public String getFormat() {
        return format;
    }

    public Long getDate() {
        return date;
    }
}
