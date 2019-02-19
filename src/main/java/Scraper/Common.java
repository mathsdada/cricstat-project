package Scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Common {
    static Document getDocument(String url) {
        return getDocumentInternal(url, 0);
    }

    // Internal Functions. Do not expose these function to outside files
    private static Document getDocumentInternal(String url, int retryCount) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Retrying Link : " + url + " Retry Count : " + retryCount);
            return getDocumentInternal(url, retryCount+1);
        }
        return document;
    }
}
