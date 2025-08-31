package top.rafii2198.wynntils.core;

import com.wynntils.core.consumers.features.Feature;
import java.util.Locale;
import net.minecraft.client.resources.language.I18n;

public class WEFeature extends Feature {

    @Override
    public String getStorageJsonName() {
        return "feature.we-helper." + getTranslationKeyName();
    }

    @Override
    public String getTranslation(String keySuffix) {
        return I18n.get("we-helper." + getTypeName().toLowerCase(Locale.ROOT) + "." + getTranslationKeyName() + "."
                        + keySuffix)
                + " §2(WE)";
    }

    @Override
    public String getTranslatedDescription() {
        return "§2WYNNIC ENCHANTMENTS§r\n"
                + I18n.get("we-helper.we-feature." + getTranslationKeyName() + ".description");
    }

    @Override
    public String getTypeName() {
        return "we-feature";
    }
}
