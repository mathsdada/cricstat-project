package Model;
import Common.Pair;
import java.util.regex.Pattern;

public class HeadToHeadData {
    private int mBalls;
    private int mRuns;
    private int mWicket;
    private int mDotBalls;
    private int mFours;
    private int mSixes;
    private int mNoBall;
    private static Pair[] ballOutcomePatterns = new Pair[]{
            new Pair<String, HeadToHeadData>("out ",    new HeadToHeadData(1, 0, 1, 1, 0, 0, 0)),
            new Pair<String, HeadToHeadData>("no ball", new HeadToHeadData(1, 0, 0, 0, 0, 0, 1)),
            new Pair<String, HeadToHeadData>("wide",    new HeadToHeadData(0, 0, 0, 0, 0, 0, 0)),
            new Pair<String, HeadToHeadData>("byes",    new HeadToHeadData(1, 0, 0, 0, 0, 0, 0)),
            new Pair<String, HeadToHeadData>("SIX",     new HeadToHeadData(1, 6, 0, 0, 0, 1, 0)),
            new Pair<String, HeadToHeadData>("FOUR",    new HeadToHeadData(1, 4, 0, 0, 1, 0, 0)),
            new Pair<String, HeadToHeadData>("1 run",   new HeadToHeadData(1, 1, 0, 0, 0, 0, 0)),
            new Pair<String, HeadToHeadData>("2 runs",  new HeadToHeadData(1, 2, 0, 0, 0, 0, 0)),
            new Pair<String, HeadToHeadData>("3 runs",  new HeadToHeadData(1, 3, 0, 0, 0, 0, 0)),
            new Pair<String, HeadToHeadData>("no run",  new HeadToHeadData(1, 0, 0, 1, 0, 0, 0))
    };

    public static HeadToHeadData extractHeadToHeadData(String inputStr1, String inputStr2) {
        HeadToHeadData ballOutcome = null;
        inputStr1 = inputStr1.strip();
        inputStr2 = inputStr2.strip();

        for (Pair pattern : HeadToHeadData.ballOutcomePatterns) {
            if(inputStr1.contains((CharSequence) pattern.getFirst())) {
                ballOutcome = HeadToHeadData.copy((HeadToHeadData) pattern.getSecond());
                break;
            }
        }
        if (ballOutcome != null) {
            if (ballOutcome.getNoBall() != 0) {
                HeadToHeadData ballOutcomeExtra = extractHeadToHeadData(inputStr2, "");
                ballOutcome.setRuns(ballOutcomeExtra.getRuns());
            }
            if ((ballOutcome.getWicket() != 0) && inputStr1.contains("Run Out!!")) {
                ballOutcome.setWicket(0);
                if (inputStr1.contains(" completed.")) {
                    HeadToHeadData ballOutcomeExtra = extractHeadToHeadData(inputStr1.split(Pattern.quote("Run Out!!"))[1].split(Pattern.quote(" completed."))[0], "");
                    ballOutcome.setRuns(ballOutcomeExtra.getRuns());
                }
            }
        } else {
            ballOutcome = new HeadToHeadData(0,0,0,0,0,0,0);
        }
        return ballOutcome;
    }

    private HeadToHeadData(int balls, int runs, int wicket, int dotBalls, int fours, int sixes, int noBall) {
        mBalls = balls;
        mRuns = runs;
        mWicket = wicket;
        mDotBalls = dotBalls;
        mFours = fours;
        mSixes = sixes;
        mNoBall = noBall;
    }

    public static HeadToHeadData copy(HeadToHeadData headToHeadData) {
        return new HeadToHeadData(headToHeadData.getBalls(),
                headToHeadData.getRuns(), headToHeadData.getWicket(),
                headToHeadData.getDotBalls(), headToHeadData.getFours(),
                headToHeadData.getSixes(), headToHeadData.getNoBall());
    }

    public static void add(HeadToHeadData dest, HeadToHeadData src) {
        dest.setBalls(dest.getBalls() + src.getBalls());
        dest.setRuns(dest.getRuns()+src.getRuns());
        dest.setWicket(dest.getWicket()+src.getWicket());
        dest.setDotBalls(dest.getDotBalls()+src.getDotBalls());
        dest.setFours(dest.getFours()+src.getFours());
        dest.setSixes(dest.getSixes()+src.getSixes());
        dest.setNoBall(dest.getNoBall()+src.getNoBall());
    }

    public int getBalls() {
        return mBalls;
    }

    public int getRuns() {
        return mRuns;
    }

    public int getWicket() {
        return mWicket;
    }

    public int getDotBalls() {
        return mDotBalls;
    }

    public int getFours() {
        return mFours;
    }

    public int getSixes() {
        return mSixes;
    }

    public int getNoBall() {
        return mNoBall;
    }

    public void setBalls(int balls) {
        mBalls = balls;
    }

    public void setRuns(int runs) {
        mRuns = runs;
    }

    public void setWicket(int wicket) {
        mWicket = wicket;
    }

    public void setDotBalls(int dotBalls) {
        mDotBalls = dotBalls;
    }

    public void setFours(int fours) {
        mFours = fours;
    }

    public void setSixes(int sixes) {
        mSixes = sixes;
    }

    public void setNoBall(int noBall) {
        mNoBall = noBall;
    }

    @Override
    public String toString() {
        return mRuns+ ">"+
                mBalls+ ">"+
                mWicket+ ">"+
                mDotBalls+ ">"+
                mFours+ ">"+
                mSixes+ ">"+
                mNoBall;
    }
}
