package top.rafii2198.FancyMenu.Placeholders;

import com.wynntils.utils.type.ErrorOr;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.RemoteManager;
import top.rafii2198.RemoteData.Types.WynncraftNewsApi;

public class WynncraftNewsPlaceholder extends Placeholder {

    private String cachedString = "";
    private WynncraftNewsApi cachedResponse;

    public WynncraftNewsPlaceholder() {
        super("wynncraft-news");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        ErrorOr<WynncraftNewsApi> data = RemoteManager.WynncraftNews.get();
        if (data == null) return "Loading...";
        if (data.hasError()) return "Error while loading Wynncraft News \n" + data.getError();
        if (cachedResponse == null || cachedString == null || cachedResponse != data.getValue()) {
            cachedResponse = data.getValue();
            cachedString = data.getValue().getFormattedNews();
        }
        return cachedString;
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return null;
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.get("we-helper.fm.placeholder.wynncraft-news");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.get("we-helper.fm.placeholder.wynncraft-news.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("we-helper.fm.placeholder.categories.we-helper");
    }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        return new DeserializedPlaceholderString(this.getIdentifier(), null, "");
    }
}
