package Database.Scraper;

import Database.Common.Configuration;
import Database.Common.Pair;
import Database.Model.Match;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ScheduleMatch {
    public static ArrayList<Database.Model.ScheduleMatch> build(String dateRequested) {
        ArrayList<Database.Model.ScheduleMatch> matches = new ArrayList<>();
        String[] categories = new String[]{"international", "league", "women", "domestic"};
        for (String category : categories) {
            Document categoryScheduleDocument = Common.getDocument("https://www.cricbuzz.com/cricket-schedule/upcoming-series/"+category);
            Element dateMatchElements = categoryScheduleDocument
                    .selectFirst("div#"+category+"-list.cb-bg-white") // this picks all dates
                    .selectFirst("div.cb-col-100.cb-col"); // this picks first date which is our preference
            String date = dateMatchElements.select("div.cb-lv-grn-strip.text-bold").text().toLowerCase();
            if (date.equals(dateRequested)) {
                Elements matchElements = dateMatchElements.select("div.cb-ovr-flo.cb-col-60.cb-col.cb-mtchs-dy-vnu");
                for (Element matchElement : matchElements) {
                    Element matchUrlElement = matchElement.selectFirst("a");
                    Pair<String, String> seriesTitleUrl = Database.Scraper.Match.getSeriesUrl(matchUrlElement.attr("href"));
                    String seriesTitle = seriesTitleUrl.getFirst();
                    String serieUrl = Configuration.HOMEPAGE + seriesTitleUrl.getSecond();
                    Match match = Database.Scraper.Match.build(matchElement,
                            Database.Scraper.Series.getSeriesFormat(Common.getDocument(serieUrl)),
                            null, Database.Scraper.Match.MatchType.SCHEDULE);
                    if (match != null) {
                        matches.add(new Database.Model.ScheduleMatch(seriesTitle, match, category));
                    }
                }
            }
        }
        return matches;
    }
}
