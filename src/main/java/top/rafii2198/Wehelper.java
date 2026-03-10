package top.rafii2198;

import de.keksuccino.fancymenu.customization.action.ActionRegistry;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderRegistry;
import de.keksuccino.fancymenu.platform.Services;
import net.fabricmc.api.ModInitializer;
import top.rafii2198.FancyMenu.Actions.ForceRequestAction;
import top.rafii2198.FancyMenu.Placeholders.WynncraftNewsPlaceholder;
import top.rafii2198.FancyMenu.Placeholders.WynncraftRemoveBadgePathPlaceholder;
import top.rafii2198.FancyMenu.Placeholders.WynnicChangelogPlaceholder;
import top.rafii2198.FancyMenu.Placeholders.WynnicVersionPlaceholder;
import top.rafii2198.FancyMenu.Placeholders.WynntilsChangelogPlaceholder;
import top.rafii2198.FancyMenu.Placeholders.WynntilsVersionPlaceholder;
import top.rafii2198.RemoteData.RemoteManager;

public class Wehelper implements ModInitializer {
    public static final String MOD_ID = "we-helper";

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        RemoteManager.init();

        if (Services.PLATFORM.isOnClient()) {
            ActionRegistry.register(new ForceRequestAction());

            PlaceholderRegistry.register(new WynncraftNewsPlaceholder());
            PlaceholderRegistry.register(new WynncraftRemoveBadgePathPlaceholder());
            PlaceholderRegistry.register(new WynnicChangelogPlaceholder());
            PlaceholderRegistry.register(new WynnicVersionPlaceholder());
            PlaceholderRegistry.register(new WynntilsChangelogPlaceholder());
            PlaceholderRegistry.register(new WynntilsVersionPlaceholder());
        }
    }
}
