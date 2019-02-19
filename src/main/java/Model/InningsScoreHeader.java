package Model;

import java.math.BigDecimal;

public class InningsScoreHeader {
    private int mRuns;
    private int mWickets;
    private BigDecimal mOvers;

    public InningsScoreHeader(String mRuns, String mWickets, String mOvers) {
        this.mRuns = Integer.parseInt(mRuns);
        this.mWickets = Integer.parseInt(mWickets);
        this.mOvers = new BigDecimal(mOvers);
    }

    public int getRuns() {
        return mRuns;
    }

    public int getWickets() {
        return mWickets;
    }

    public BigDecimal getOvers() {
        return mOvers;
    }
}
