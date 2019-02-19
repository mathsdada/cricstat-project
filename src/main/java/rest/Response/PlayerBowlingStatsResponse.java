package rest.Response;

import java.math.BigDecimal;

public class PlayerBowlingStatsResponse {
    private final String name;
    private final int wicketsTaken;
    private final int runsGiven;
    private final BigDecimal oversBowled;
    private final BigDecimal economy;
    private final String format;
    private final String venue;
    private final Long date;
    private final String teamPlayedFor;
    private final String teamPlayedAgainst;

    public PlayerBowlingStatsResponse(String name, int wicketsTaken, int runsGiven, BigDecimal oversBowled,
                                      BigDecimal economy, String format, String venue, Long date,
                                      String teamPlayedFor, String teamPlayedAgainst) {
        this.name = name;
        this.wicketsTaken = wicketsTaken;
        this.runsGiven = runsGiven;
        this.oversBowled = oversBowled;
        this.economy = economy;
        this.format = format;
        this.venue = venue;
        this.date = date;
        this.teamPlayedFor = teamPlayedFor;
        this.teamPlayedAgainst = teamPlayedAgainst;
    }

    public String getName() {
        return name;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }

    public BigDecimal getOversBowled() {
        return oversBowled;
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

    public int getRunsGiven() {
        return runsGiven;
    }

    public BigDecimal getEconomy() {
        return economy;
    }
}

