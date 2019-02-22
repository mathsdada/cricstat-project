package rest.Response;

import java.math.BigDecimal;

public class PerInningsMatchScore {
    private final int inningsNumber;
    private final String battingTeam;
    private final String bowlingTeam;
    private final int runs;
    private final int wickets;
    private final BigDecimal overs;

    public PerInningsMatchScore(int inningsNumber, String battingTeam, String bowlingTeam, int runs, int wickets, BigDecimal overs) {
        this.inningsNumber = inningsNumber;
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.runs = runs;
        this.wickets = wickets;
        this.overs = overs;
    }

    public int getInningsNumber() {
        return inningsNumber;
    }

    public String getBattingTeam() {
        return battingTeam;
    }

    public String getBowlingTeam() {
        return bowlingTeam;
    }

    public int getRuns() {
        return runs;
    }

    public int getWickets() {
        return wickets;
    }

    public BigDecimal getOvers() {
        return overs;
    }
}
