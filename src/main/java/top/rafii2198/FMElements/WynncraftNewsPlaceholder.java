package top.rafii2198.FMElements;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.WynncraftNews;

import java.util.List;

public class WynncraftNewsPlaceholder extends Placeholder {

    public WynncraftNewsPlaceholder(){
        super("wynncraft-news");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        return WynncraftNews.getFormattedNews();
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return null;
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.translate("we-helper.editor.dynamicvariabletextfield.variables.wynncraft-news");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.translate("we-helper.editor.dynamicvariabletextfield.variables.wynncraft-news.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.translate("we-helper.editor.dynamicvariabletextfield.categories.we-helper");
    }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        return dps;
    }
}
