package Model;

import java.util.ArrayList;

public class InningsScore {
    private int mInningsNum;
    private Team mBattingTeam;
    private Team mBowlingTeam;
    private InningsScoreHeader mScoreHeader;
    private ArrayList<PlayerBattingScore> mPlayerBattingScores;
    private ArrayList<PlayerBowlingScore> mPlayerBowlingScores;

    public InningsScore(int inningsNum, Team battingTeam, Team bowlingTeam) {
        mInningsNum = inningsNum;
        mBattingTeam = battingTeam;
        mBowlingTeam = bowlingTeam;
    }

    public int getInningsNum() {
        return mInningsNum;
    }

    public Team getBattingTeam() {
        return mBattingTeam;
    }

    public Team getBowlingTeam() {
        return mBowlingTeam;
    }

    public InningsScoreHeader getScoreHeader() {
        return mScoreHeader;
    }

    public void setScoreHeader(InningsScoreHeader scoreHeader) {
        this.mScoreHeader = scoreHeader;
    }

    public ArrayList<PlayerBattingScore> getPlayerBattingScores() {
        return mPlayerBattingScores;
    }

    public void setPlayerBattingScores(ArrayList<PlayerBattingScore> playerBattingScores) {
        mPlayerBattingScores = playerBattingScores;
    }

    public ArrayList<PlayerBowlingScore> getPlayerBowlingScores() {
        return mPlayerBowlingScores;
    }

    public void setPlayerBowlingScores(ArrayList<PlayerBowlingScore> playerBowlingScores) {
        mPlayerBowlingScores = playerBowlingScores;
    }
}