package top.rafii2198.Utilities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wynntils.utils.MathUtils;
import com.wynntils.utils.colors.CustomColor;
import com.wynntils.utils.render.buffered.CustomRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import top.rafii2198.wynntils.core.type.FlatBarTexture;

public class WERenderUtils {

    public static void drawColoredFlatProgressBar(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            FlatBarTexture texture,
            CustomColor color,
            float x1,
            float y1,
            float x2,
            float y2,
            float progress) {

        drawColoredBarBackground(
                poseStack,
                bufferSource,
                texture.getResource(),
                color,
                x1,
                y1,
                x2,
                y2,
                texture.X1(),
                texture.backgroundY1(),
                texture.X2(),
                texture.backgroundY2());

        float progressLeft = progress;
        while (progressLeft > 0) {
            drawColoredBarForeground(
                    poseStack,
                    bufferSource,
                    texture.getResource(),
                    color,
                    x1,
                    y1,
                    x2,
                    y2,
                    texture.X1(),
                    texture.foregroundY1(),
                    texture.X2(),
                    texture.foregroundY2(),
                    progressLeft);
            progressLeft -= 1;
            if (progressLeft < 0) break;
            color = color.hueShift(0.085f);
        }
    }

    private static void drawColoredBarBackground(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            ResourceLocation resourceLocation,
            CustomColor color,
            float x1,
            float y1,
            float x2,
            float y2,
            float textureX1,
            float textureY1,
            float textureX2,
            float textureY2) {
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer buffer = bufferSource.getBuffer(CustomRenderType.getPositionColorTextureQuad(resourceLocation));

        buffer.addVertex(matrix, x1, y1, 0).setUv(textureX1, textureY1).setColor(color.asInt());
        buffer.addVertex(matrix, x1, y2, 0).setUv(textureX1, textureY2).setColor(color.asInt());
        buffer.addVertex(matrix, x2, y2, 0).setUv(textureX2, textureY2).setColor(color.asInt());
        buffer.addVertex(matrix, x2, y1, 0).setUv(textureX2, textureY1).setColor(color.asInt());
    }

    private static void drawColoredBarForeground(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            ResourceLocation resourceLocation,
            CustomColor color,
            float x1,
            float y1,
            float x2,
            float y2,
            float textureX1,
            float textureY1,
            float textureX2,
            float textureY2,
            float progress) {
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer buffer = bufferSource.getBuffer(CustomRenderType.getPositionColorTextureQuad(resourceLocation));

        float adjustedProgress = MathUtils.clamp(progress, 0, 1);
        float tx2 = textureX2 - (1f - adjustedProgress) * (textureX2 - textureX1);
        x2 = x2 - (1f - adjustedProgress) * (x2 - x1);

        buffer.addVertex(matrix, x1, y1, 0).setUv(textureX1, textureY1).setColor(color.asInt());
        buffer.addVertex(matrix, x1, y2, 0).setUv(textureX1, textureY2).setColor(color.asInt());
        buffer.addVertex(matrix, x2, y2, 0).setUv(tx2, textureY2).setColor(color.asInt());
        buffer.addVertex(matrix, x2, y1, 0).setUv(tx2, textureY1).setColor(color.asInt());
    }
}
