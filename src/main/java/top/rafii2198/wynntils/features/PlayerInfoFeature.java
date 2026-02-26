package top.rafii2198.wynntils.features;

import com.wynntils.core.consumers.features.ProfileDefault;
import com.wynntils.core.consumers.overlays.annotations.RegisterOverlay;
import com.wynntils.core.persisted.config.Category;
import com.wynntils.core.persisted.config.ConfigCategory;
import com.wynntils.core.persisted.config.ConfigProfile;
import top.rafii2198.wynntils.core.WEFeature;
import top.rafii2198.wynntils.overlays.PlayerInfoOverlay;
import top.rafii2198.wynntils.overlays.TeamInfoOverlay;

@ConfigCategory(Category.OVERLAYS)
public class PlayerInfoFeature extends WEFeature {
    @RegisterOverlay
    public final PlayerInfoOverlay playerInfoOverlay = new PlayerInfoOverlay();

    @RegisterOverlay
    public final TeamInfoOverlay teamInfoOverlay = new TeamInfoOverlay();

    public PlayerInfoFeature() {
        super(new ProfileDefault.Builder()
                .enabledFor(new ConfigProfile[] {ConfigProfile.DEFAULT})
                .build());
    }
}
