package rest.Response;

import java.math.BigDecimal;

public class TeamStatsBowlingCommonResponse {
    private final String bowler;
    private final int innings;
    private final int runs;
    private final int balls;
    private final BigDecimal average;
    private final BigDecimal strikeRate;
    private final int highScore;
    private final int fours;
    private final int sixes;
    private final int ducks;
    private final int fifties;
    private final int hundreds;
    private final int notOuts;

    public TeamStatsBowlingCommonResponse(String bowler, int innings, int runs, int balls, BigDecimal average, BigDecimal strikeRate, int highScore, int fours, int sixes, int ducks, int fifties, int hundreds, int notOuts) {
        this.bowler = bowler;
        this.innings = innings;
        this.runs = runs;
        this.balls = balls;
        this.average = average;
        this.strikeRate = strikeRate;
        this.highScore = highScore;
        this.fours = fours;
        this.sixes = sixes;
        this.ducks = ducks;
        this.fifties = fifties;
        this.hundreds = hundreds;
        this.notOuts = notOuts;
    }

    public String getBowler() {
        return bowler;
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

    public BigDecimal getAverage() {
        return average;
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

    public int getNotOuts() {
        return notOuts;
    }
}
