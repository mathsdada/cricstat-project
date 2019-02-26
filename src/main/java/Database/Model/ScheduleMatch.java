package Database.Model;

public class ScheduleMatch {
    private String mSeries;
    private Match mMatch;
    private String mCategory;

    public ScheduleMatch(String series, Match match, String category) {
        mSeries = series;
        mMatch = match;
        mCategory = category;
    }

    public String getSeries() {
        return mSeries;
    }

    public Match getMatch() {
        return mMatch;
    }

    public String getCategory() {
        return mCategory;
    }
}
