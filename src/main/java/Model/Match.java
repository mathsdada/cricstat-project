package Model;

import java.util.ArrayList;

public class Match {
    private String mUrl;
    private int mId;
    private String mTitle;
    private String mFormat;
    private String mVenue;
    private Long mDate; /* Epoch */
    private String mStatus;
    private String mOutcome;
    private String mWinningTeam;
    private ArrayList<Team> mTeams;
    private ArrayList<InningsScore> mInningsScores;
    private ArrayList<HeadToHead> mHeadToHeadList;

    public Match(String url, String id, String title, String format, String venue, String status, String outcome) {
        mUrl = url;
        mId = Integer.parseInt(id);
        mTitle = title;
        mFormat = format;
        mVenue = venue;
        mStatus = status;
        mOutcome = outcome;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    public String getVenue() {
        return mVenue;
    }

    public void setVenue(String venue) {
        mVenue = venue;
    }

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long date) {
        mDate = date;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getOutcome() {
        return mOutcome;
    }

    public void setOutcome(String outcome) {
        mOutcome = outcome;
    }

    public String getWinningTeam() {
        return mWinningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        mWinningTeam = winningTeam;
    }

    public ArrayList<Team> getTeams() {
        return mTeams;
    }

    public void setTeams(ArrayList<Team> teams) {
        mTeams = teams;
    }

    public ArrayList<InningsScore> getInningsScores() {
        return mInningsScores;
    }

    public void setInningsScores(ArrayList<InningsScore> inningsScores) {
        mInningsScores = inningsScores;
    }

    public ArrayList<HeadToHead> getHeadToHeadList() {
        return mHeadToHeadList;
    }

    public void setHeadToHeadList(ArrayList<HeadToHead> headToHeadList) {
        mHeadToHeadList = headToHeadList;
    }
}

