package Model;

import java.math.BigDecimal;

public class PlayerBattingScore {
    private Player mBatsman;
    private String mStatus;
    private int mRuns;
    private int mBalls;
    private int mFours;
    private int mSixes;
    private BigDecimal mStrikeRate;
    private Player mBowler;

    public PlayerBattingScore(Player player, String status, String runs, String balls, String fours, String sixes, String strikeRate, Player bowler) {
        mBatsman = player;
        mStatus = status;
        mRuns = Integer.parseInt(runs);
        mBalls = Integer.parseInt(balls);
        mFours = Integer.parseInt(fours);
        mSixes = Integer.parseInt(sixes);
        mStrikeRate = new BigDecimal(strikeRate);
        mBowler = bowler;
    }

    public Player getBatsman() {
        return mBatsman;
    }

    public String getStatus() {
        return mStatus;
    }

    public int getRuns() {
        return mRuns;
    }

    public int getBalls() {
        return mBalls;
    }

    public int getFours() {
        return mFours;
    }

    public int getSixes() {
        return mSixes;
    }

    public BigDecimal getStrikeRate() {
        return mStrikeRate;
    }

    public Player getBowler() {
        return mBowler;
    }

    @Override
    public String toString() {
        return "Batting Score: \nName : " + mBatsman +
                "\nS : " + mStatus +
                "\nR : " + mRuns +
                "\nB : " + mBalls +
                "\n4s : " + mFours +
                "\n6s : " + mSixes +
                "\nSR : " + mStrikeRate;
    }
}
