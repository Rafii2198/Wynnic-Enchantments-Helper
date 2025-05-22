package top.rafii2198.RemoteData;

import org.jsoup.Jsoup;
import top.rafii2198.RemoteData.Types.WynncraftNewsApi;
import top.rafii2198.Utilities.HttpManager;

public class WynncraftNews {
    private static final String ENDPOINT = "https://api.wynncraft.com/v3/latest-news";
    private static final WynncraftNewsApi[] Json = HttpManager.getWynncraftNews(ENDPOINT);
    private static final String News = formatNews();

    private static String formatNews() {
        if (Json == null) return "-1";
        StringBuilder FormattedNews = new StringBuilder();

        // Combine Lines
        for (WynncraftNewsApi news : Json) {
            // News content is written in HTML which needs parsing to Markdown
            // Formatting and links are handled with regex, extracting the rest of text is done via Jsoup

            String LINKS = "<a.*href=\"(http.*/)\".*>(.*)</a>";
            String APO = "&#039;";
            String BOLD = "<b>|</b>";

            String content = news.getContent()
                    .replaceAll(LINKS, "[$2]($1)")
                    .replaceAll(APO, "'")
                    .replaceAll(BOLD, "**");

            content = Jsoup.parse(content).body().text();

            FormattedNews.append(String.format(
                    "%%#AAAAAA%%_%s at %s_%%#%%\n### **[%s](%s)**\n%s\n\n---\n\n\n",
                    news.getAuthor(), news.getDate(), news.getTitle(), news.getForumThread(), content));
        }
        return FormattedNews.toString().replaceAll("\\n---\\n\\n\\n$", "");
    }

    public static String getNews() {
        return News;
    }
}
