package rest.Response;

import java.util.ArrayList;

public class ScheduleResponse {
    private final String title;
    private final String format;
    private final String venue;
    private final Long date;
    private final ArrayList<ScheduleTeam> teams;
    private final String series;

    public ScheduleResponse(String title, String format, String venue, Long date, ArrayList<ScheduleTeam> teams, String series) {
        this.title = title;
        this.format = format;
        this.venue = venue;
        this.date = date;
        this.teams = teams;
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public String getFormat() {
        return format;
    }

    public String getVenue() {
        return venue;
    }

    public Long getDate() {
        return date;
    }

    public ArrayList<ScheduleTeam> getTeams() {
        return teams;
    }

    public String getSeries() {
        return series;
    }

    public static class ScheduleTeam {
        private final String name;
        private final ArrayList<SchedulePlayer> squad;

        public ScheduleTeam(String name, ArrayList<SchedulePlayer> squad) {
            this.name = name;
            this.squad = squad;
        }

        public String getName() {
            return name;
        }

        public ArrayList<SchedulePlayer> getSquad() {
            return squad;
        }
    }

    public static class SchedulePlayer {
        private final String name;
        private final String role;
        private final String batting_style;
        private final String bowling_style;

        public SchedulePlayer(String name, String role, String batting_style, String bowling_style) {
            this.name = name;
            this.role = role;
            this.batting_style = batting_style;
            this.bowling_style = bowling_style;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public String getBatting_style() {
            return batting_style;
        }

        public String getBowling_style() {
            return bowling_style;
        }
    }
}
