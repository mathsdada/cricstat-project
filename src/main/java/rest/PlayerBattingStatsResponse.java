package rest;

import java.math.BigDecimal;

public class PlayerBattingStatsResponse {
    private final String name;
    private final int runs;
    private final BigDecimal strikeRate;
    private final boolean isNotOut;
    private final int fours;
    private final int sixes;
    private final String format;
    private final String venue;
    private final Long date;
    private final String teamPlayedFor;
    private final String teamPlayedAgainst;

    public PlayerBattingStatsResponse(String name, int runs, BigDecimal strikeRate, boolean isNotOut,
                                      int fours, int sixes, String format, String venue, Long date,
                                      String teamPlayedFor, String teamPlayedAgainst) {
        this.name = name;
        this.runs = runs;
        this.strikeRate = strikeRate;
        this.isNotOut = isNotOut;
        this.fours = fours;
        this.sixes = sixes;
        this.format = format;
        this.venue = venue;
        this.date = date;
        this.teamPlayedFor = teamPlayedFor;
        this.teamPlayedAgainst = teamPlayedAgainst;
    }

    public String getName() {
        return name;
    }

    public int getRuns() {
        return runs;
    }

    public BigDecimal getStrikeRate() {
        return strikeRate;
    }

    public boolean isNotOut() {
        return isNotOut;
    }

    public int getFours() {
        return fours;
    }

    public int getSixes() {
        return sixes;
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

    public String getTeamPlayedFor() {
        return teamPlayedFor;
    }

    public String getTeamPlayedAgainst() {
        return teamPlayedAgainst;
    }
}

