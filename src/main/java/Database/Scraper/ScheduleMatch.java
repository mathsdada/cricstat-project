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
        Document internationalScheduleDocument = Common.getDocument("https://www.cricbuzz.com/cricket-schedule/upcoming-series/domestic");
        Element dateMatchElements = internationalScheduleDocument
                .selectFirst("div#domestic-list.cb-bg-white") // this picks all dates
                .selectFirst("div.cb-col-100.cb-col"); // this picks first date which is our preference
        String date = dateMatchElements.select("div.cb-lv-grn-strip.text-bold").text();
        if (date.equals(dateRequested)) {
            Elements matchElements = dateMatchElements.select("div.cb-ovr-flo.cb-col-60.cb-col.cb-mtchs-dy-vnu");
            for (Element matchElement: matchElements) {
                Element matchUrlElement = matchElement.selectFirst("a");
                System.out.println(matchUrlElement.text());
                Pair<String, String> seriesTitleUrl = Database.Scraper.Match.getSeriesUrl(matchUrlElement.attr("href"));
                String seriesTitle = seriesTitleUrl.getFirst();
                String serieUrl = Configuration.HOMEPAGE + seriesTitleUrl.getSecond();
                System.out.println(serieUrl);
                Match match = Database.Scraper.Match.build(matchElement,
                        Database.Scraper.Series.getSeriesFormat(Common.getDocument(serieUrl)),
                        null);
                if (match != null) {
                    matches.add(new Database.Model.ScheduleMatch(seriesTitle, match));
                }
            }
        }
        return matches;
    }
}