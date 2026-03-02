package top.rafii2198.RemoteData.Types;

import java.util.List;

public class WynncraftNewsApi {
    private results results;
    private List<results.id> id;

    private static class results {
        private static class id {
            private String title;
            private String banner;
            private String recap;
            private boolean visible;
            private boolean pinned;
            private String published_at;
        }
    }

    public String formattedNews() {
        StringBuilder builder = new StringBuilder();
        id.forEach(a -> {
            builder.append(a.title);
            builder.append("\n");
            builder.append(a.banner);
            builder.append("\n");
            builder.append(a.recap);
            builder.append("\n");
            builder.append(a.published_at);
        });
        return builder.toString();
    }
}
