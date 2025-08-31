package top.rafii2198.wynntils.overlays;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wynntils.core.components.Models;
import com.wynntils.core.components.Services;
import com.wynntils.core.consumers.overlays.ContainerOverlay;
import com.wynntils.core.consumers.overlays.OverlayPosition;
import com.wynntils.core.consumers.overlays.OverlaySize;
import com.wynntils.core.persisted.Persisted;
import com.wynntils.core.persisted.config.Config;
import com.wynntils.core.text.StyledText;
import com.wynntils.models.players.event.HadesRelationsUpdateEvent;
import com.wynntils.models.players.event.PartyEvent;
import com.wynntils.services.hades.HadesUser;
import com.wynntils.services.hades.event.HadesUserEvent;
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
import it.crystalnest.fancy_entity_renderer.api.Rotation;
import it.crystalnest.fancy_entity_renderer.api.entity.player.FancyPlayerWidget;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Pose;
import net.neoforged.bus.api.SubscribeEvent;
import top.rafii2198.Utilities.WERenderUtils;
import top.rafii2198.wynntils.core.WEContainerOverlay;
import top.rafii2198.wynntils.core.WEOverlay;
import top.rafii2198.wynntils.core.type.FlatBarTexture;

public class TeamInfoOverlay extends WEContainerOverlay<TeamInfoOverlay.TeamMemberOverlay> {
    private static final HadesUser DUMMY_USER_1 =
            new HadesUser("WE Player 1", new CappedValue(12432, 13120), new CappedValue(65, 123));
    private static final HadesUser DUMMY_USER_2 =
            new HadesUser("WE Player 2", new CappedValue(4561, 9870), new CappedValue(98, 170));

    @Persisted
    public final Config<Integer> maxPartyMembers = new Config<>(4);

    @Persisted
    private final Config<FlatBarTexture> texture = new Config<>(FlatBarTexture.CLEAR);

    @Persisted
    private final Config<Float> ratio = new Config<>(70f);

    @Persisted
    private final Config<Boolean> renderPlayer = new Config<>(true);

    @Persisted
    private final Config<Float> degrees = new Config<>(-25f);

    @Persisted(i18nKey = "overlay.wynntils.textOverlay.fontScale")
    private final Config<Float> fontScale = new Config<>(0.5f);

    private final FancyPlayerWidget renderWidget =
            new FancyPlayerWidget(0, 0, 1, 1).setMoving(true).setPose(Pose.STANDING);

    public TeamInfoOverlay() {
        super(
                new OverlayPosition(
                        40,
                        22,
                        VerticalAlignment.TOP,
                        HorizontalAlignment.LEFT,
                        OverlayPosition.AnchorSection.MIDDLE_LEFT),
                new OverlaySize(60, 16.5f),
                ContainerOverlay.GrowDirection.DOWN,
                HorizontalAlignment.LEFT,
                VerticalAlignment.TOP);
    }

    @Override
    protected List<TeamMemberOverlay> getPreviewChildren() {
        return List.of(
                new TeamMemberOverlay(DUMMY_USER_1, getWidth(), getHeight()),
                new TeamMemberOverlay(DUMMY_USER_2, getWidth(), getHeight()));
    }

    @SubscribeEvent
    public void onHadesUserAdded(HadesUserEvent.Added event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onHadesUserRemoved(HadesUserEvent.Removed event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(HadesRelationsUpdateEvent.PartyList event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(PartyEvent.Invited event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(PartyEvent.Listed event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(PartyEvent.PriorityChanged event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(PartyEvent.OtherDisconnected event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(PartyEvent.OtherJoined event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(PartyEvent.OtherLeft event) {
        updateChildren();
    }

    @SubscribeEvent
    public void onPartyChange(PartyEvent.OtherReconnected event) {
        updateChildren();
    }

    private void updateChildren() {
        this.clearChildren();

        // War users take priority
        List<HadesUser> warUsers = Models.War.getHadesUsers();

        if (!warUsers.isEmpty()) {
            warUsers.forEach(hadesUser ->
                    this.addChild(new TeamInfoOverlay.TeamMemberOverlay(hadesUser, getWidth(), getHeight())));
            return;
        }

        List<HadesUser> hadesUsers = Services.Hades.getHadesUsers().toList();
        List<String> partyMembers = Models.Party.getPartyMembers();

        List<HadesUser> hadesUsingPartyMembers = hadesUsers.stream()
                .filter(hadesUser -> partyMembers.contains(hadesUser.getName()))
                .sorted(Comparator.comparing(element -> partyMembers.indexOf(element.getName())))
                .collect(Collectors.toList());
        hadesUsingPartyMembers =
                hadesUsingPartyMembers.subList(0, Math.min(hadesUsingPartyMembers.size(), maxPartyMembers.get()));

        for (HadesUser hadesUser : hadesUsingPartyMembers) {
            this.addChild(new TeamInfoOverlay.TeamMemberOverlay(hadesUser, getWidth(), getHeight()));
        }
    }

    public final class TeamMemberOverlay extends WEOverlay {
        private final HadesUser hadesUser;

        private TeamMemberOverlay(HadesUser hadesUser, float width, float height) {
            super(
                    new OverlayPosition(
                            0,
                            0,
                            VerticalAlignment.TOP,
                            HorizontalAlignment.LEFT,
                            OverlayPosition.AnchorSection.TOP_LEFT),
                    new OverlaySize(width, height));
            this.hadesUser = hadesUser;
        }

        @Override
        public void render(
                GuiGraphics guiGraphics, MultiBufferSource bufferSource, DeltaTracker deltaTracker, Window window) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            if (renderPlayer.get()) {
                BufferedRenderUtils.drawRect(
                        guiGraphics.pose(),
                        bufferSource,
                        CommonColors.BLACK,
                        getRenderX() - this.getHeight(),
                        getRenderY(),
                        1,
                        getHeight(),
                        getHeight());
                RenderUtils.createRectMask(
                        guiGraphics.pose(), getRenderX() - getHeight(), getRenderY(), getHeight(), getHeight());

                renderWidget.copyPlayer(hadesUser.getUuid());
                renderWidget.setHeight((int) (getHeight() * 0.8 * FancyPlayerWidget.PLAYER_RENDER_HEIGHT));
                renderWidget.setX((int) (getRenderX() - getHeight() / 2));
                renderWidget.setY((int) (getRenderY() + getHeight() * 0.2));
                renderWidget.setBodyRotation(Rotation.createFromDeg(0, degrees.get(), 0));
                renderWidget.render(guiGraphics, 0, 0, 0);

                RenderUtils.clearMask();
            }

            renderBarsAndText(guiGraphics, bufferSource);
            poseStack.popPose();
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
                    (float) hadesUser.getHealth().getProgress());
            WERenderUtils.drawColoredFlatProgressBar(
                    guiGraphics.pose(),
                    multiBufferSource,
                    texture.get(),
                    new CustomColor(0, 182, 255),
                    getRenderX(),
                    getRenderY() + getHeight() * calculatedRatio + 1f,
                    getRenderX() + getWidth() - 1,
                    getRenderY() + getHeight() - 1,
                    (float) hadesUser.getMana().getProgress());

            BufferedFontRenderer.getInstance()
                    .renderAlignedTextInBox(
                            guiGraphics.pose(),
                            multiBufferSource,
                            new StyledText[] {
                                StyledText.fromString(
                                        String.valueOf(hadesUser.getHealth().current()))
                            },
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
                            new StyledText[] {
                                StyledText.fromString(
                                        String.valueOf(hadesUser.getHealth().max()))
                            },
                            getRenderX() + (font * 2),
                            getRenderX() + getWidth() - 1 - (font * 2),
                            getRenderY() + 2,
                            getRenderY() + getHeight() * calculatedRatio + 1,
                            0,
                            CommonColors.WHITE,
                            HorizontalAlignment.LEFT,
                            VerticalAlignment.MIDDLE,
                            TextShadow.OUTLINE,
                            font);

            BufferedFontRenderer.getInstance()
                    .renderAlignedTextInBox(
                            guiGraphics.pose(),
                            multiBufferSource,
                            new StyledText[] {
                                StyledText.fromString(
                                        String.valueOf(hadesUser.getMana().current()))
                            },
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
                            new StyledText[] {
                                StyledText.fromString(
                                        String.valueOf(hadesUser.getMana().max()))
                            },
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

            BufferedFontRenderer.getInstance()
                    .renderAlignedTextInBox(
                            guiGraphics.pose(),
                            multiBufferSource,
                            new StyledText[] {StyledText.fromUnformattedString(hadesUser.getName())},
                            getRenderX(),
                            getRenderX() + getWidth(),
                            getRenderY(),
                            getRenderY() - fontScale.get() / 2f,
                            0,
                            hadesUser.getRelationColor(),
                            HorizontalAlignment.LEFT,
                            VerticalAlignment.BOTTOM,
                            TextShadow.NORMAL,
                            fontScale.get() / 1.5f);
        }
    }
}
