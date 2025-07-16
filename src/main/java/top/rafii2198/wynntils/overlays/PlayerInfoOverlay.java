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
import com.wynntils.utils.colors.CustomColor;
import com.wynntils.utils.render.RenderUtils;
import com.wynntils.utils.render.buffered.BufferedFontRenderer;
import com.wynntils.utils.render.buffered.BufferedRenderUtils;
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

    @Persisted(i18nKey = "feature.wynntils.gameBarsOverlay.overlay.baseBar.animationTime")
    private final Config<Float> animationTime = new Config<>(2f);

    @Persisted(i18nKey = "overlay.wynntils.textOverlay.fontScale")
    private final Config<Float> fontScale = new Config<>(1f);

    private CappedValue health;
    private CappedValue mana;
    private float progressH;
    private float progressM;

    public PlayerInfoOverlay() {
        super(
                new OverlayPosition(
                        240,
                        -400,
                        VerticalAlignment.TOP,
                        HorizontalAlignment.RIGHT,
                        OverlayPosition.AnchorSection.TOP_RIGHT),
                new OverlaySize(120, 33));
        this.health = CappedValue.EMPTY;
        this.mana = CappedValue.EMPTY;
        this.progressH = 0;
        this.progressM = 0;
    }

    public void tick() {
        if (Models.WorldState.onWorld() && this.userEnabled.get()) {
            health = Models.CharacterStats.getHealth().orElse(CappedValue.EMPTY);
            mana = Models.CharacterStats.getMana().orElse(CappedValue.EMPTY);
            if (animationTime.get() == 0f) {
                progressH = (float) health.getProgress();
                progressM = (float) mana.getProgress();
            } else {
                progressH -= (float) ((animationTime.get() * 0.15f) * (progressH - health.getProgress()));
                progressM -= (float) ((animationTime.get() * 0.15f) * (progressM - mana.getProgress()));
            }
        }
    }

    @Override
    public void render(
            GuiGraphics guiGraphics, MultiBufferSource multiBufferSource, DeltaTracker deltaTracker, Window window) {
        if (!Models.WorldState.onWorld()) return;

        if (renderPlayer.get()) {
            BufferedRenderUtils.drawRect(
                    guiGraphics.pose(),
                    multiBufferSource,
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

        renderBarsAndText(guiGraphics, multiBufferSource);
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

    private void renderBarsAndText(GuiGraphics guiGraphics, MultiBufferSource multiBufferSource) {
        float calculatedRatio = MathUtils.clamp(ratio.get(), 0, 100) / 100;
        float font = fontScale.get() * (calculatedRatio + getHeight() / 100);

        BufferedRenderUtils.drawRect(
                guiGraphics.pose(),
                multiBufferSource,
                CommonColors.BLACK,
                getRenderX() - 1f,
                getRenderY(),
                0,
                getWidth() + 1f,
                getHeight());

        WERenderUtils.drawColoredFlatProgressBar(
                guiGraphics.pose(),
                multiBufferSource,
                texture.get(),
                new CustomColor(255, 0, 54),
                getRenderX(),
                getRenderY() + 1,
                getRenderX() + getWidth() - 1f,
                getRenderY() + getHeight() * calculatedRatio,
                progressH);
        WERenderUtils.drawColoredFlatProgressBar(
                guiGraphics.pose(),
                multiBufferSource,
                texture.get(),
                new CustomColor(0, 182, 255),
                getRenderX(),
                getRenderY() + getHeight() * calculatedRatio + 1f,
                getRenderX() + getWidth() - 1,
                getRenderY() + getHeight() - 1,
                progressM);

        BufferedFontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics.pose(),
                        multiBufferSource,
                        StyledText.fromString(String.valueOf(health.current())),
                        getRenderX() + (font * 2),
                        getRenderX() + getWidth() - 1 - (font * 2),
                        getRenderY() + 2,
                        getRenderY() + getHeight() * calculatedRatio + 1,
                        0,
                        CommonColors.WHITE,
                        HorizontalAlignment.RIGHT,
                        VerticalAlignment.MIDDLE,
                        TextShadow.OUTLINE,
                        font);

        BufferedFontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics.pose(),
                        multiBufferSource,
                        StyledText.fromString(String.valueOf(health.max())),
                        getRenderX() + (font * 2),
                        getRenderX() + getWidth() - 1 - (font * 2),
                        getRenderY() + 2,
                        getRenderY() + getHeight() * calculatedRatio + 1,
                        0,
                        CommonColors.WHITE,
                        HorizontalAlignment.LEFT,
                        VerticalAlignment.MIDDLE,
                        TextShadow.OUTLINE,
                        calculatedRatio + getHeight() / 100);

        BufferedFontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics.pose(),
                        multiBufferSource,
                        StyledText.fromString(String.valueOf(mana.current())),
                        getRenderX() + (font * 2),
                        getRenderX() + getWidth() - 1 - (font * 2),
                        getRenderY() + getHeight() * calculatedRatio + 1,
                        getRenderY() + getHeight(),
                        0,
                        CommonColors.WHITE,
                        HorizontalAlignment.RIGHT,
                        VerticalAlignment.MIDDLE,
                        TextShadow.OUTLINE,
                        (1 - calculatedRatio + getHeight() / 100) * fontScale.get());

        BufferedFontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics.pose(),
                        multiBufferSource,
                        StyledText.fromString(String.valueOf(mana.max())),
                        getRenderX() + (font * 2),
                        getRenderX() + getWidth() - 1 - (font * 2),
                        getRenderY() + getHeight() * calculatedRatio + 1,
                        getRenderY() + getHeight(),
                        0,
                        CommonColors.WHITE,
                        HorizontalAlignment.LEFT,
                        VerticalAlignment.MIDDLE,
                        TextShadow.OUTLINE,
                        (1 - calculatedRatio + getHeight() / 100) * fontScale.get());
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
