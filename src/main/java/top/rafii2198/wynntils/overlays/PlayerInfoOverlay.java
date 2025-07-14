package top.rafii2198.wynntils.overlays;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.wynntils.core.components.Models;
import com.wynntils.core.consumers.overlays.OverlayPosition;
import com.wynntils.core.consumers.overlays.OverlaySize;
import com.wynntils.core.persisted.Persisted;
import com.wynntils.core.persisted.config.Config;
import com.wynntils.core.text.StyledText;
import com.wynntils.utils.MathUtils;
import com.wynntils.utils.colors.CommonColors;
import com.wynntils.utils.render.RenderUtils;
import com.wynntils.utils.render.buffered.BufferedFontRenderer;
import com.wynntils.utils.render.type.HorizontalAlignment;
import com.wynntils.utils.render.type.TextShadow;
import com.wynntils.utils.render.type.VerticalAlignment;
import com.wynntils.utils.type.CappedValue;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import top.rafii2198.Utilities.WERenderUtils;
import top.rafii2198.wynntils.core.WEOverlay;
import top.rafii2198.wynntils.core.type.FlatBarTexture;

public class PlayerInfoOverlay extends WEOverlay {

    @Persisted
    private final Config<FlatBarTexture> texture = new Config<>(FlatBarTexture.CLEAR);

    @Persisted
    private final Config<Float> ratio = new Config<>(70f);

    @Persisted
    private final Config<Boolean> renderPlayer = new Config<>(true);

    @Persisted
    private final Config<Boolean> rotatePlayer = new Config<>(true);

    @Persisted
    private final Config<Float> degrees = new Config<>(-25f);

    public PlayerInfoOverlay() {
        super(
                new OverlayPosition(
                        290,
                        -500,
                        VerticalAlignment.TOP,
                        HorizontalAlignment.RIGHT,
                        OverlayPosition.AnchorSection.TOP_RIGHT),
                new OverlaySize(119, 30));
    }

    @Override
    public void render(
            GuiGraphics guiGraphics, MultiBufferSource multiBufferSource, DeltaTracker deltaTracker, Window window) {
        if (!Models.WorldState.onWorld()) return;
        float calculatedRatio = MathUtils.clamp(ratio.get(), 0, 100) / 100;

        if (renderPlayer.get()) {
            RenderUtils.drawRect(
                    guiGraphics.pose(),
                    CommonColors.BLACK,
                    getRenderX() - getHeight(),
                    getRenderY(),
                    1,
                    getHeight(),
                    getHeight());
            RenderUtils.createRectMask(
                    guiGraphics.pose(), getRenderX() - getHeight(), getRenderY(), getHeight(), getHeight());
            renderPlayerEntity(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false));
            RenderUtils.clearMask();
        }

        CappedValue HealthValue = Models.CharacterStats.getHealth().orElse(CappedValue.EMPTY);
        CappedValue ManaValue = Models.CharacterStats.getMana().orElse(CappedValue.EMPTY);

        WERenderUtils.drawColoredFlatProgressBar(
                guiGraphics.pose(),
                multiBufferSource,
                texture.get(),
                CommonColors.RED,
                getRenderX(),
                getRenderY(),
                getRenderX() + getWidth(),
                getRenderY() + getHeight() * calculatedRatio,
                (float) HealthValue.getProgress());
        WERenderUtils.drawColoredFlatProgressBar(
                guiGraphics.pose(),
                multiBufferSource,
                texture.get(),
                CommonColors.CYAN,
                getRenderX(),
                getRenderY() + getHeight() * calculatedRatio,
                getRenderX() + getWidth(),
                getRenderY() + getHeight(),
                (float) ManaValue.getProgress());

        //        RenderUtils.drawColoredProgressBar(
        //                guiGraphics.pose(),
        //                Texture.UNIVERSAL_BAR,
        //                CommonColors.RED,
        //                getRenderX(),
        //                getRenderY(),
        //                getRenderX() + getWidth(),
        //                getRenderY() + getHeight() * calculatedRatio,
        //                0,
        //                texture.getTextureY1(),
        //                Texture.UNIVERSAL_BAR.width(),
        //                texture.getTextureY2(),
        //                (float) HealthValue.getProgress());
        //
        //        RenderUtils.drawColoredProgressBar(
        //                guiGraphics.pose(),
        //                Texture.UNIVERSAL_BAR,
        //                CommonColors.LIGHT_BLUE,
        //                getRenderX(),
        //                getRenderY() + getHeight() * calculatedRatio,
        //                getRenderX() + getWidth(),
        //                getRenderY() + getHeight(),
        //                0,
        //                texture.getTextureY1(),
        //                Texture.UNIVERSAL_BAR.width(),
        //                texture.getTextureY2(),
        //                (float) ManaValue.getProgress());

        BufferedFontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics.pose(),
                        multiBufferSource,
                        StyledText.fromString(String.format("%s ❤ %s", HealthValue.current(), HealthValue.max())),
                        getRenderX(),
                        getRenderX() + getWidth(),
                        getRenderY(),
                        getRenderY() + getHeight() * calculatedRatio,
                        0,
                        CommonColors.WHITE,
                        HorizontalAlignment.CENTER,
                        VerticalAlignment.MIDDLE,
                        TextShadow.OUTLINE,
                        calculatedRatio + getHeight() / 100);

        BufferedFontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics.pose(),
                        multiBufferSource,
                        StyledText.fromString(String.format("%s ✺ %s", ManaValue.current(), ManaValue.max())),
                        getRenderX(),
                        getRenderX() + getWidth(),
                        getRenderY() + getHeight() * calculatedRatio,
                        getRenderY() + getHeight(),
                        0,
                        CommonColors.WHITE,
                        HorizontalAlignment.CENTER,
                        VerticalAlignment.MIDDLE,
                        TextShadow.OUTLINE,
                        1 - calculatedRatio + getHeight() / 100);
    }

    private void renderPlayerEntity(GuiGraphics guiGraphics, float tick) {
        Minecraft instance = Minecraft.getInstance();
        LivingEntity playerEntity = instance.player;
        RenderedPlayerProperties properties = null;
        if (rotatePlayer.get()) {
            properties =
                    new RenderedPlayerProperties(playerEntity, degrees.get().floatValue());
            properties.apply(playerEntity);
        }

        float scale = (float) (getHeight() * 0.8);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(getRenderX() - getHeight() / 2, getRenderY() + getHeight() * 1.75, 50.0);
        guiGraphics.pose().scale(scale, scale * -1, scale);
        guiGraphics.flush();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = instance.getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        guiGraphics.drawSpecial((MultiBufferSource bufferSource) ->
                dispatcher.render(playerEntity, 0, 0, 0, tick, guiGraphics.pose(), bufferSource, 0xF000F0));
        if (properties != null) {
            properties.restore(playerEntity);
        }
        guiGraphics.flush();
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    private record RenderedPlayerProperties(
            float yBodyRot, float yHeadRot, float yBodyRot0, float yHeadRot0, boolean isInvisible, float degrees) {

        public RenderedPlayerProperties(LivingEntity player, float degrees) {
            this(player.yBodyRot, player.yHeadRot, player.yBodyRotO, player.yHeadRotO, player.isInvisible(), degrees);
        }

        public void restore(LivingEntity player) {
            player.yBodyRot = this.yBodyRot;
            player.yHeadRot = this.yHeadRot;
            player.yBodyRotO = this.yBodyRot0;
            player.yHeadRotO = this.yHeadRot0;
            player.setInvisible(this.isInvisible);
        }

        public void apply(LivingEntity player) {
            player.yBodyRot = this.degrees;
            player.yHeadRot = this.degrees;
            player.yBodyRotO = this.degrees;
            player.yHeadRotO = this.degrees;
            player.setInvisible(false);
        }
    }
}
