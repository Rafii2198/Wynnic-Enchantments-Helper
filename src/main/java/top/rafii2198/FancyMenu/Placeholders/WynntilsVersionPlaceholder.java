package top.rafii2198.FancyMenu.Placeholders;

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

public class WynntilsVersionPlaceholder extends Placeholder {
    public WynntilsVersionPlaceholder() {
        super("wynntils-version");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        ErrorOr<ModrinthProject[]> data = RemoteManager.Wynntils.get();
        if (data == null) return "Loading...";
        if (data.hasError()) return "Error while loading Wynntils version \n" + data.getError();
        return Arrays.stream(data.getValue())
                .filter(v -> v.getVersion_type().equals("release"))
                .map(ModrinthProject::getVersion_number)
                .findFirst()
                .orElse("")
                .replace("v", "");
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return List.of();
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.get("we-helper.fm.placeholder.wynntils-version");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.get("we-helper.fm.placeholder.wynntils-version.desc"));
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
