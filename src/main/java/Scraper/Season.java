package Scraper;

import Common.Configuration;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Season {
    public static Model.Season build(String year) {
        /* Each worker thread fills seriesArrayList with Series.. So protect it with Mutex */
        ArrayList<Model.Series> seriesArrayList = new ArrayList<>();
        final Object seriesListMutex = new Object();
        Elements seriesElements = getSeriesElements(year);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (Element seriesElement : seriesElements) {
            Runnable worker = new SeriesWorkerThread(seriesElement, seriesArrayList, seriesListMutex);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
        return new Model.Season(year, seriesArrayList);
    }

    private static Elements getSeriesElements(String year) {
        String seasonUrl = Configuration.HOMEPAGE + "/cricket-scorecard-archives/" + year;
        Document seasonDoc = Common.getDocument(seasonUrl);
        return seasonDoc.select("a.text-hvr-underline");
    }

    private static class SeriesWorkerThread implements Runnable {
        private Element mSeriesElement;
        private ArrayList<Model.Series> mSeriesArrayList;
        private final Object mSeriesArrayListMutex;

        SeriesWorkerThread(Element seriesElement, ArrayList<Model.Series> seriesArrayList, Object seriesListMutex) {
            mSeriesElement = seriesElement;
            mSeriesArrayList = seriesArrayList;
            mSeriesArrayListMutex = seriesListMutex;
        }

        @Override
        public void run() {
            Model.Series series = Series.build(mSeriesElement);
            if (series != null) {
                synchronized (mSeriesArrayListMutex) {
                    mSeriesArrayList.add(series);
                }
            }
        }
    }

}
