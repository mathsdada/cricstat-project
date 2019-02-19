package Model;

import java.util.ArrayList;

public class Team {
    private String mName;
    private String mShortName;
    private ArrayList<Player> mSquad;

    public Team(String mName, String mShortName) {
        this.mName = mName;
        this.mShortName = mShortName;
        mSquad = new ArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public String getShortName() {
        return mShortName;
    }

    public void addPlayer(Player player) {
        mSquad.add(player);
    }

    public ArrayList<Player> getSquad() {
        return mSquad;
    }
}
