package top.rafii2198;

import de.keksuccino.fancymenu.customization.placeholder.PlaceholderRegistry;
import de.keksuccino.fancymenu.platform.Services;
import net.fabricmc.api.ModInitializer;
import top.rafii2198.FMElements.WynncraftNewsPlaceholder;
import top.rafii2198.FMElements.WynnicChangelogPlaceholder;
import top.rafii2198.FMElements.WynnicVersionPlaceholder;
import top.rafii2198.FMElements.WynntilsChangelogPlaceholder;


public class Wehelper implements ModInitializer {
	public static final String MOD_ID = "we-helper";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		if (Services.PLATFORM.isOnClient()){
			PlaceholderRegistry.register(new WynncraftNewsPlaceholder());
			PlaceholderRegistry.register(new WynnicChangelogPlaceholder());
			PlaceholderRegistry.register(new WynnicVersionPlaceholder());
			PlaceholderRegistry.register(new WynntilsChangelogPlaceholder());
		}
	}
}