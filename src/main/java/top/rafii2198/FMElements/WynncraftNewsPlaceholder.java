package top.rafii2198.FMElements;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.WynncraftNews;

public class WynncraftNewsPlaceholder extends Placeholder {

    public WynncraftNewsPlaceholder() {
        super("wynncraft-news");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        return WynncraftNews.getNews();
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
