package top.rafii2198.FMElements;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.List;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WynncraftRemoveBadgePathPlaceholder extends Placeholder {

    public WynncraftRemoveBadgePathPlaceholder() {
        super("wynncraft-remove-badge-path");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String path = dps.values.get("path");
        return path.replaceAll("nextgen/badges/(.*)\\.svg", "$1");
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return List.of("path");
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.translate(
                "we-helper.editor.dynamicvariabletextfield.variables.wynncraft-wynncraft-remove-badge-path");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.translate(
                "we-helper.editor.dynamicvariabletextfield.variables.wynncraft-wynncraft-remove-badge-path.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.translate("we-helper.editor.dynamicvariabletextfield.categories.we-helper");
    }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        dps.values.put("path", "nextgen/badges/rank_hero.svg");
        return dps;
    }
}
