package Database.Model;

import Database.Common.StringUtils;

import java.math.BigDecimal;

public class PlayerBowlingScore {
    private Player mPlayer;
    private int mBalls;
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
        mBalls = StringUtils.getBallsFromOversStr(overs);
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

    public int getBalls() {
        return mBalls;
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

    public String toString() {
        return "Bowling Score: \nName : " + mPlayer +
               "\nB : " + mBalls +
               "\nM : " + mMaidens +
               "\nR : " + mRuns +
               "\nW : " + mWickets +
               "\nNB : " + mNoBalls +
               "\nWD : " + mWides +
               "\nECO : " + mEconomy;
    }
}
