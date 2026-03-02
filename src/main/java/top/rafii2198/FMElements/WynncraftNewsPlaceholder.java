package top.rafii2198.FMElements;

import com.wynntils.utils.type.ErrorOr;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import top.rafii2198.RemoteData.RemoteManager;
import top.rafii2198.RemoteData.Types.WynncraftNewsApi;

public class WynncraftNewsPlaceholder extends Placeholder {

    public WynncraftNewsPlaceholder() {
        super("wynncraft-news");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        ErrorOr<WynncraftNewsApi[]> data = RemoteManager.WynncraftNews.get();
        if (data == null) return "Loading...";
        if (data.hasError()) return "Error while loading Wynncraft News \n" + data.getError();

        StringBuilder FormattedNews = new StringBuilder();
        for (WynncraftNewsApi news : data.getValue()) {
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

    @Override
    public @Nullable List<String> getValueNames() {
        return null;
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.get("we-helper.editor.dynamicvariabletextfield.variables.wynncraft-news");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.get("we-helper.editor.dynamicvariabletextfield.variables.wynncraft-news.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("we-helper.editor.dynamicvariabletextfield.categories.we-helper");
    }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        return new DeserializedPlaceholderString(this.getIdentifier(), null, "");
    }
}
