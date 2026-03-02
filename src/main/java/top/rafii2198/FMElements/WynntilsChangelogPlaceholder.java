package top.rafii2198.FMElements;

import com.wynntils.utils.type.ErrorOr;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.RemoteManager;
import top.rafii2198.RemoteData.Types.ModrinthProject;

public class WynntilsChangelogPlaceholder extends Placeholder {

    public WynntilsChangelogPlaceholder() {
        super("wynntils-changelog");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        ErrorOr<ModrinthProject[]> data = RemoteManager.Wynntils.get();
        if (data == null) return "Loading...";
        if (data.hasError()) return "Error while loading Wynntils Changelog \n" + data.getError();
        return Arrays.stream(data.getValue())
                .filter(v -> v.getVersion_type().equals("release"))
                .map(ModrinthProject::getChangelog)
                .findFirst()
                .orElse("");
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return null;
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.get("we-helper.editor.dynamicvariabletextfield.variables.wynntils-changelog");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.get("we-helper.editor.dynamicvariabletextfield.variables.wynntils-changelog.desc"));
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
