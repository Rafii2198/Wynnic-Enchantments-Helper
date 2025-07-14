package top.rafii2198.FMElements;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.WynntilsProject;

public class WynntilsVersionPlaceholder extends Placeholder {
    public WynntilsVersionPlaceholder() {
        super("wynntils-version");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        return WynntilsProject.getVersion();
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return List.of();
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.get("we-helper.editor.dynamicvariabletextfield.variables.wynntils-version");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.get("we-helper.editor.dynamicvariabletextfield.variables.wynntils-version.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("we-helper.editor.dynamicvariabletextfield.categories.we-helper");
    }

    @Override
    public @NotNull DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        return dps;
    }
}
