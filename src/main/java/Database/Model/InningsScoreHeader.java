package Database.Model;

import Database.Common.StringUtils;

public class InningsScoreHeader {
    private int mRuns;
    private int mWickets;
    private int mBalls;

    public InningsScoreHeader(String mRuns, String mWickets, String mOvers) {
        this.mRuns = Integer.parseInt(mRuns);
        this.mWickets = Integer.parseInt(mWickets);
        this.mBalls = StringUtils.getBallsFromOversStr(mOvers);
    }

    public int getRuns() {
        return mRuns;
    }

    public int getWickets() {
        return mWickets;
    }

    public int getBalls() {
        return mBalls;
    }
}
