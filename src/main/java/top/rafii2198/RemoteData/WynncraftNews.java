package top.rafii2198.RemoteData;

import top.rafii2198.RemoteData.Types.WynncraftNewsApi;
import top.rafii2198.Utilities.HttpManager;

public class WynncraftNews {
    private static final String ENDPOINT = "https://api.wynncraft.com/v3/latest-news";
    private static final WynncraftNewsApi[] Json = HttpManager.getWynncraftNews(ENDPOINT);

    public static String getFormattedNews() {
        String FormattedNews = "";
        for (WynncraftNewsApi news : Json) {
            FormattedNews += String.format("## %s\n### %s\n%s\n<hr>", news.getTitle(), news.getDate(), news.getContent());
        }
        return FormattedNews;
    }
}
