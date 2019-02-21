package rest.Response;

import java.math.BigDecimal;

public class TeamStatsBowlingCommonResponse {
    private final String bowler;
    private final int innings;
    private final BigDecimal overs;
    private final int maidens;
    private final int runs;
    private final int wickets;
    private final BigDecimal average;
    private final BigDecimal economy;
    private final BigDecimal strikeRate;
    private final int aboveFourWickets;
    private final int aboveFiveWickets;

    public TeamStatsBowlingCommonResponse(String bowler, int innings, BigDecimal overs, int maidens, int runs,
                                          int wickets, BigDecimal average, BigDecimal economy, BigDecimal strikeRate,
                                          int aboveFourWickets, int aboveFiveWickets) {
        this.bowler = bowler;
        this.innings = innings;
        this.overs = overs;
        this.maidens = maidens;
        this.runs = runs;
        this.wickets = wickets;
        this.average = average;
        this.economy = economy;
        this.strikeRate = strikeRate;
        this.aboveFourWickets = aboveFourWickets;
        this.aboveFiveWickets = aboveFiveWickets;
    }

    public String getBowler() {
        return bowler;
    }

    public int getInnings() {
        return innings;
    }

    public BigDecimal getOvers() {
        return overs;
    }

    public int getMaidens() {
        return maidens;
    }

    public int getRuns() {
        return runs;
    }

    public int getWickets() {
        return wickets;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public BigDecimal getEconomy() {
        return economy;
    }

    public BigDecimal getStrikeRate() {
        return strikeRate;
    }

    public int getAboveFourWickets() {
        return aboveFourWickets;
    }

    public int getAboveFiveWickets() {
        return aboveFiveWickets;
    }
}