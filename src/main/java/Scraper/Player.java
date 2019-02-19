package Scraper;

import Common.Configuration;
import Common.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Player {
    static Model.Player build(Element playerElement, HashMap<String, Model.Player> playerCacheMap) {
        // Ex :- href="/profiles/7776/marcus-harris"
        String playerUrl = playerElement.attr("href");
        String playerName = StringUtils.correctPlayerName(playerElement.text()).toLowerCase();

        String playerId = playerUrl.split(Pattern.quote("/"))[2];
        if(!playerCacheMap.containsKey(playerId)) {
            HashMap<String, String> profileMap = getProfileMap(Configuration.HOMEPAGE + playerUrl);
            playerCacheMap.put(playerId,
                    new Model.Player(playerId, playerName, profileMap.get("role"), profileMap.get("batting style"), profileMap.get("bowling style")));
        }
        return playerCacheMap.get(playerId);
    }
    private static HashMap<String, String> getProfileMap(String playerUrl) {
        HashMap<String, String> profileMap = new HashMap<>();
        profileMap.put("role",  null);
        profileMap.put("batting style", null);
        profileMap.put("bowling style", null);

        Document playerDoc = Common.getDocument(playerUrl);
        Elements keyElements = playerDoc.select("div.cb-col.cb-col-40.text-bold.cb-lst-itm-sm");
        Elements valElements = playerDoc.select("div.cb-col.cb-col-60.cb-lst-itm-sm");
        for (int i=0; (i<keyElements.size()) && (i<valElements.size()); i++) {
            String key = keyElements.get(i).text().strip().toLowerCase();
            String val = valElements.get(i).text().strip().toLowerCase();
            if(profileMap.containsKey(key)) {
                profileMap.put(key, val);
            }
        }
        return profileMap;
    }
}
