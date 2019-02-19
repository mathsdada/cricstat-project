package Model;

import java.sql.*;
import java.util.ArrayList;

public class Series {
    private int mId;
    private String mTitle;
    private ArrayList<Match> mMatches;


    public Series(String id, String title, ArrayList<Match> matches) {
        mId = Integer.parseInt(id);
        mTitle = title;
        mMatches = matches;
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

    public ArrayList<Match> getMatches() {
        return mMatches;
    }

    public void setMatches(ArrayList<Match> matches) {
        mMatches = matches;
    }
}
