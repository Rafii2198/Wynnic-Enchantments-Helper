package top.rafii2198.wynntils.features;

import com.wynntils.core.consumers.overlays.RenderState;
import com.wynntils.core.consumers.overlays.annotations.OverlayInfo;
import com.wynntils.core.persisted.config.Category;
import com.wynntils.core.persisted.config.ConfigCategory;
import com.wynntils.mc.event.RenderEvent;
import top.rafii2198.wynntils.core.WEFeature;
import top.rafii2198.wynntils.overlays.PlayerInfoOverlay;
import top.rafii2198.wynntils.overlays.TeamInfoOverlay;

@ConfigCategory(Category.OVERLAYS)
public class PlayerInfoFeature extends WEFeature {
    @OverlayInfo(renderType = RenderEvent.ElementType.GUI, renderAt = RenderState.PRE)
    public final PlayerInfoOverlay playerInfoOverlay = new PlayerInfoOverlay();

    @OverlayInfo(renderType = RenderEvent.ElementType.GUI, renderAt = RenderState.PRE)
    public final TeamInfoOverlay teamInfoOverlay = new TeamInfoOverlay();
}
