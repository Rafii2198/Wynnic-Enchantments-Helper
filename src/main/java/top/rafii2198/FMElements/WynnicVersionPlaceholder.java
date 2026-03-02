package top.rafii2198.FMElements;

import com.wynntils.utils.type.ErrorOr;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.RemoteManager;
import top.rafii2198.RemoteData.Types.ModrinthProject;

public class WynnicVersionPlaceholder extends Placeholder {

    public WynnicVersionPlaceholder() {
        super("we-version");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString deserializedPlaceholderString) {
        ErrorOr<ModrinthProject[]> data = RemoteManager.WynnicEnchantments.get();
        if (data == null) return "Loading...";
        if (data.hasError()) return "Error while loading Wynnic Enchantments version \n" + data.getError();
        return data.getValue()[0].getVersion_number();
    }

    @Override
    public @Nullable List<String> getValueNames() {
        return null;
    }

    @Override
    public @NotNull String getDisplayName() {
        return I18n.get("we-helper.editor.dynamicvariabletextfield.variables.we-version");
    }

    @Override
    public @Nullable List<String> getDescription() {
        return List.of(I18n.get("we-helper.editor.dynamicvariabletextfield.variables.we-version.desc"));
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
