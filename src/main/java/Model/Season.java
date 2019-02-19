package Model;

import java.util.ArrayList;

public class Season {
    private String mYear;
    private ArrayList<Series> mSeriesList;

    public Season(String  year, ArrayList<Series> seriesList) {
        mYear = year;
        mSeriesList = seriesList;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

    public ArrayList<Series> getSeriesList() {
        return mSeriesList;
    }

    public void setSeriesList(ArrayList<Series> seriesList) {
        mSeriesList = seriesList;
    }
}
