package top.rafii2198.Utilities;

import com.wynntils.utils.MathUtils;
import com.wynntils.utils.colors.CustomColor;
import com.wynntils.utils.render.RenderUtils;
import com.wynntils.utils.render.pipelines.CustomRenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import top.rafii2198.wynntils.core.type.FlatBarTexture;

public class WERenderUtils {

    public static void drawColoredFlatProgressBar(
            GuiGraphics guiGraphics,
            FlatBarTexture texture,
            CustomColor color,
            float x1,
            float y1,
            float x2,
            float y2,
            float progress) {

        RenderUtils.drawTexturedRect(
                guiGraphics,
                CustomRenderPipelines.PROGRESS_BAR_PIPELINE,
                texture.getIdentifier(),
                color,
                x1,
                y1,
                x2,
                y2,
                0,
                texture.backgroundY1(),
                texture.textureWidth(),
                texture.backgroundY2() - texture.backgroundY1(),
                texture.textureWidth(),
                texture.textureHeight());

        while (progress > 0) {
            RenderUtils.drawTexturedRect(
                    guiGraphics,
                    CustomRenderPipelines.PROGRESS_BAR_PIPELINE,
                    texture.getIdentifier(),
                    color,
                    x1,
                    y1,
                    x2 * MathUtils.clamp(progress, 0, 1),
                    y2,
                    0,
                    texture.foregroundY1(),
                    texture.textureWidth() * MathUtils.clamp(progress, 0, 1),
                    texture.foregroundY2() - texture.foregroundY1(),
                    texture.textureWidth(),
                    texture.textureHeight());

            progress -= 1;
            color = color.hueShift(0.085f);
        }
    }
}
