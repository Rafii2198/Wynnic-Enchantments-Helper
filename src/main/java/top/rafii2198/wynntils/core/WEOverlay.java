package top.rafii2198.wynntils.core;

import com.mojang.blaze3d.platform.Window;
import com.wynntils.core.consumers.overlays.Overlay;
import com.wynntils.core.consumers.overlays.OverlayPosition;
import com.wynntils.core.consumers.overlays.OverlaySize;
import com.wynntils.utils.render.type.HorizontalAlignment;
import com.wynntils.utils.render.type.VerticalAlignment;
import java.util.Locale;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;

public class WEOverlay extends Overlay {

    protected WEOverlay(
            OverlayPosition position,
            OverlaySize size,
            HorizontalAlignment horizontalAlignmentOverride,
            VerticalAlignment verticalAlignmentOverride) {
        super(position, size, horizontalAlignmentOverride, verticalAlignmentOverride);
    }

    @Override
    public void render(
            GuiGraphics guiGraphics, MultiBufferSource multiBufferSource, DeltaTracker deltaTracker, Window window) {}

    protected WEOverlay(OverlayPosition position, OverlaySize size) {
        super(position, size);
    }

    protected WEOverlay(OverlayPosition position, float width, float height) {
        super(position, width, height);
    }

    @Override
    public String getTranslation(String keySuffix) {
        return I18n.get("we-helper." + getTypeName().toLowerCase(Locale.ROOT) + "." + getTranslationKeyName() + "."
                        + keySuffix)
                + " §2(WE)";
    }
}
