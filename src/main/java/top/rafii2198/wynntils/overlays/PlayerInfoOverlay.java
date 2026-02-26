package top.rafii2198.wynntils.overlays;

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
import com.wynntils.utils.mc.McUtils;
import com.wynntils.utils.render.FontRenderer;
import com.wynntils.utils.render.RenderUtils;
import com.wynntils.utils.render.type.HorizontalAlignment;
import com.wynntils.utils.render.type.TextShadow;
import com.wynntils.utils.render.type.VerticalAlignment;
import com.wynntils.utils.type.CappedValue;
import it.crystalnest.fancy_entity_renderer.api.Rotation;
import it.crystalnest.fancy_entity_renderer.api.entity.player.FancyPlayerWidget;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Pose;
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

    private final FancyPlayerWidget renderWidget =
            new FancyPlayerWidget(0, 0, 1, 1).copyLocalPlayer().setMoving(true);

    private CappedValue health;
    private CappedValue mana;
    private float progressH;
    private float progressM;

    public PlayerInfoOverlay() {
        super(
                new OverlayPosition(
                        10,
                        22,
                        VerticalAlignment.TOP,
                        HorizontalAlignment.RIGHT,
                        OverlayPosition.AnchorSection.BOTTOM_LEFT),
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
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Window window) {
        if (!Models.WorldState.onWorld()) return;

        if (renderPlayer.get()) {

            RenderUtils.drawRect(
                    guiGraphics,
                    CommonColors.BLACK,
                    getRenderX() - getHeight(),
                    getRenderY(),
                    getHeight(),
                    getHeight());
            RenderUtils.enableScissor(
                    guiGraphics, (int) (getRenderX() - getHeight()), (int) getRenderY(), (int) getHeight(), (int)
                            getHeight());
            renderPlayerEntity(guiGraphics);
            RenderUtils.disableScissor(guiGraphics);
        }

        renderBarsAndText(guiGraphics);
    }

    private void renderPlayerEntity(GuiGraphics guiGraphics) {
        renderWidget.setHeight((int) (getHeight() * 0.8 * FancyPlayerWidget.PLAYER_RENDER_HEIGHT));
        renderWidget.setX((int) (getRenderX() - getHeight() / 2));
        renderWidget.setY((int) (getRenderY() + getHeight() * 0.2));
        if (rotatePlayer.get()) {
            renderWidget.setBodyRotation(Rotation.fromDeg(0, degrees.get(), 0));
        } else {
            renderWidget.setBodyRotation(0, McUtils.player().getYRot(), 0);
        }
        renderWidget.setAllowedPoses(List.of(Pose.STANDING, Pose.CROUCHING));
        renderWidget.mimicLocalPlayer();
        renderWidget.render(guiGraphics, 0, 0, 0);
    }

    private void renderBarsAndText(GuiGraphics guiGraphics) {
        float calculatedRatio = MathUtils.clamp(ratio.get(), 0, 100) / 100;
        float font = fontScale.get() * (calculatedRatio + getHeight() / 100);

        RenderUtils.drawRect(
                guiGraphics, CommonColors.BLACK, getRenderX() - 1f, getRenderY(), getWidth() + 1f, getHeight());

        WERenderUtils.drawColoredFlatProgressBar(
                guiGraphics,
                texture.get(),
                new CustomColor(255, 0, 54),
                getRenderX(),
                getRenderY() + 1,
                getWidth() - 1f,
                getHeight() * calculatedRatio,
                progressH);
        WERenderUtils.drawColoredFlatProgressBar(
                guiGraphics,
                texture.get(),
                new CustomColor(0, 182, 255),
                getRenderX(),
                getRenderY() + getHeight() * calculatedRatio + 2f,
                getWidth() - 1,
                getHeight() * (1 - calculatedRatio) - 3f,
                progressM);

        FontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics,
                        new StyledText[] {StyledText.fromString(String.valueOf(health.current()))},
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

        FontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics,
                        new StyledText[] {StyledText.fromString(String.valueOf(health.max()))},
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

        FontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics,
                        new StyledText[] {StyledText.fromString(String.valueOf(mana.current()))},
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

        FontRenderer.getInstance()
                .renderAlignedTextInBox(
                        guiGraphics,
                        new StyledText[] {StyledText.fromString(String.valueOf(mana.max()))},
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
}
