package Database.Model;

public class ScheduleMatch {
    private String mSeries;
    private Match mMatch;

    public ScheduleMatch(String series, Match match) {
        mSeries = series;
        mMatch = match;
    }

    public String getSeries() {
        return mSeries;
    }

    public Match getMatch() {
        return mMatch;
    }
}
