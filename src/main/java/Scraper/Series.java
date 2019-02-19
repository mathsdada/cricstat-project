package Scraper;

import Common.Configuration;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

class Series {
    static Model.Series build(Element seriesElement) {
        String seriesUrl = seriesElement.attr("href");
        // Validate Url Before proceeding
        if (!seriesUrl.contains("cricket-series") || seriesUrl.contains("qualifier") ||
                seriesUrl.contains("warm-up") || seriesUrl.contains("practice")) {
            return null;
        }
        String seriesId = seriesUrl.split(Pattern.quote("/"))[2];
        String seriesTitle = seriesElement.text().toLowerCase();
        return new Model.Series(seriesId, seriesTitle, getSeriesMatchList(Configuration.HOMEPAGE + seriesUrl));
    }

    private static ArrayList<Model.Match> getSeriesMatchList(String seriesUrl) {
        // Maintain player Cache to avoid repeatedly scraping player profile for each match in a particular series.
        // This is valid only if all matches of a series gets scraped by single thread.
        // TODO This logic need to changed if threading behavior changes
        // Key : Player ID, Value : Player
        ArrayList<Model.Match> matches = new ArrayList<>();
        HashMap<String, Model.Player> playerCacheMap = new HashMap<>();
        Document seriesDocument = Common.getDocument(seriesUrl);
        String seriesFormat = getSeriesFormat(seriesDocument).toLowerCase();

        Elements matchElements = seriesDocument.select("div.cb-col-60.cb-col.cb-srs-mtchs-tm");
        for (Element matchElement: matchElements) {
            Model.Match match = Match.build(matchElement, seriesFormat, playerCacheMap);
            if (match != null) {
                matches.add(match);
            }
        }
        return matches;
    }

    private static String getSeriesFormat(Document seriesDocument) {
        Elements elements = seriesDocument.select("div.cb-col-100.cb-col.cb-nav-main.cb-bg-white").first()
                .select("div");
        return elements.get(1).text().split(Pattern.quote(" . "))[0];
    }

}
