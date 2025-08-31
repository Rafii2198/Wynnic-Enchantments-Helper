package top.rafii2198.wynntils.core;

import com.wynntils.core.consumers.overlays.ContainerOverlay;
import com.wynntils.core.consumers.overlays.Overlay;
import com.wynntils.core.consumers.overlays.OverlayPosition;
import com.wynntils.core.consumers.overlays.OverlaySize;
import com.wynntils.utils.render.type.HorizontalAlignment;
import com.wynntils.utils.render.type.VerticalAlignment;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.resources.language.I18n;

public class WEContainerOverlay<T extends WEOverlay> extends ContainerOverlay {
    protected WEContainerOverlay(
            OverlayPosition position,
            OverlaySize size,
            GrowDirection growDirection,
            HorizontalAlignment horizontalAlignment,
            VerticalAlignment verticalAlignment) {
        super(position, size, growDirection, horizontalAlignment, verticalAlignment);
    }

    @Override
    protected List<T> getPreviewChildren() {
        return List.of();
    }

    @Override
    public String getTranslation(String keySuffix) {
        return I18n.get("we-helper." + getTypeName().toLowerCase(Locale.ROOT) + "." + getTranslationKeyName() + "."
                        + keySuffix)
                + " §2(WE)";
    }

    @Override
    public String getTypeName() {
        return "we-overlay";
    }

    @Override
    public void addChild(Overlay overlay) {
        super.addChild(overlay);
    }
}
