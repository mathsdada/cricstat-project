package Model;

import java.math.BigDecimal;

public class PlayerBowlingScore {
    private Player mPlayer;
    private BigDecimal mOvers;
    private int mMaidens;
    private int mRuns;
    private int mWickets;
    private int mNoBalls;
    private int mWides;
    private BigDecimal mEconomy;

    public PlayerBowlingScore(Player player,
                              String overs, String maidens, String wickets, String noBalls, String wides,
                              String runs, String economy) {
        mPlayer = player;
        mOvers = new BigDecimal(overs);
        mMaidens = Integer.parseInt(maidens);
        mRuns = Integer.parseInt(runs);
        mWickets = Integer.parseInt(wickets);
        mNoBalls = Integer.parseInt(noBalls);
        mWides = Integer.parseInt(wides);
        mEconomy = new BigDecimal(economy);
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public BigDecimal getOvers() {
        return mOvers;
    }

    public int getMaidens() {
        return mMaidens;
    }

    public int getRuns() {
        return mRuns;
    }

    public int getWickets() {
        return mWickets;
    }

    public int getNoBalls() {
        return mNoBalls;
    }

    public int getWides() {
        return mWides;
    }

    public BigDecimal getEconomy() {
        return mEconomy;
    }

    @Override
    public String toString() {
        return "Bowling Score: \nName : " + mPlayer +
               "\nO : " + mOvers +
               "\nM : " + mMaidens +
               "\nR : " + mRuns +
               "\nW : " + mWickets +
               "\nNB : " + mNoBalls +
               "\nWD : " + mWides +
               "\nECO : " + mEconomy;
    }
}
