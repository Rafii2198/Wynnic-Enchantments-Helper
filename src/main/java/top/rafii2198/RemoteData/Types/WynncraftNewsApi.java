package top.rafii2198.RemoteData.Types;

import com.google.gson.annotations.SerializedName;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class WynncraftNewsApi {
    private Results results;

    private class Results {
        @SerializedName("1")
        private Id a;

        @SerializedName("2")
        private Id b;

        @SerializedName("3")
        private Id c;

        @SerializedName("4")
        private Id d;

        @SerializedName("5")
        private Id e;

        @SerializedName("6")
        private Id f;

        @SerializedName("7")
        private Id g;

        @SerializedName("8")
        private Id h;

        @SerializedName("9")
        private Id i;

        @SerializedName("10")
        private Id j;

        private class Id {
            private int pk;
            private String title;
            private String recap;
            private boolean visible;
            private boolean pinned;
            private String published_at;
        }
    }

    public String getFormattedNews() {
        List<Results.Id> all_ids = List.of(
                results.a, results.b, results.c, results.d, results.e, results.f, results.g, results.h, results.i,
                results.j);
        List<String> formattedNews = new java.util.ArrayList<>(List.of());
        all_ids.stream().filter(id -> id != null && id.visible).forEachOrdered(id -> {
            ZonedDateTime published = ZonedDateTime.parse(id.published_at, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            formattedNews.add(MessageFormat.format(
                    """
                            *%#b0b0b0%{0}%#%*
                            ### [{1}]({2})
                            {3}
                            """,
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(published), id.title, id.pk, id.recap));
        });

        return String.join("\n\n---\n\n", formattedNews);
    }
}
