package top.rafii2198.FMElements;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.List;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.WynnicEnchantmentsProject;

public class WynnicChangelogPlaceholder extends Placeholder {

    public WynnicChangelogPlaceholder() {
        super("we-changelog");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        return WynnicEnchantmentsProject.getChangelog();
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return null;
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.translate("we-helper.editor.dynamicvariabletextfield.variables.we-changelog");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.translate("we-helper.editor.dynamicvariabletextfield.variables.we-changelog.desc"));
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
