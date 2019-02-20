package rest.Response;

import java.math.BigDecimal;

public class TeamStatsBattingCommonResponse {
    private final String batsman;
    private final int innings;
    private final int runs;
    private final int balls;
    private final BigDecimal strikeRate;
    private final int highScore;
    private final int fours;
    private final int sixes;
    private final int ducks;
    private final int fifties;
    private final int hundreds;

    public TeamStatsBattingCommonResponse(String batsman, int innings, int runs, int balls, BigDecimal strikeRate, int highScore, int fours, int sixes, int ducks, int fifties, int hundreds) {
        this.batsman = batsman;
        this.innings = innings;
        this.runs = runs;
        this.balls = balls;
        this.strikeRate = strikeRate;
        this.highScore = highScore;
        this.fours = fours;
        this.sixes = sixes;
        this.ducks = ducks;
        this.fifties = fifties;
        this.hundreds = hundreds;
    }

    public String getBatsman() {
        return batsman;
    }

    public int getInnings() {
        return innings;
    }

    public int getRuns() {
        return runs;
    }

    public int getBalls() {
        return balls;
    }

    public BigDecimal getStrikeRate() {
        return strikeRate;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getFours() {
        return fours;
    }

    public int getSixes() {
        return sixes;
    }

    public int getDucks() {
        return ducks;
    }

    public int getFifties() {
        return fifties;
    }

    public int getHundreds() {
        return hundreds;
    }
}
