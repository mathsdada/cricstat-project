package Model;

import Common.Pair;

public class HeadToHead {
    private int mInningsNum;
    private Pair<Model.Player, Team> mBatsman;
    private Pair<Model.Player, Team> mBowler;
    private HeadToHeadData mHeadToHeadData;

    public HeadToHead(int inningsNumber, Pair<Model.Player, Team> batsman, Pair<Model.Player, Team> bowler) {
        mInningsNum = inningsNumber;
        mBatsman = batsman;
        mBowler = bowler;
        mHeadToHeadData = null;
    }

    public Pair<Player, Team> getBatsman() {
        return mBatsman;
    }

    public Pair<Player, Team> getBowler() {
        return mBowler;
    }

    public HeadToHeadData getHeadToHeadData() {
        return mHeadToHeadData;
    }

    public void setHeadToHeadData(HeadToHeadData headToHeadData) {
        if (headToHeadData == null) return;
        if (mHeadToHeadData == null) {
            mHeadToHeadData = headToHeadData;
        } else {
            HeadToHeadData.add(mHeadToHeadData, headToHeadData);
        }
    }

    public int getInningsNum() {
        return mInningsNum;
    }
}
