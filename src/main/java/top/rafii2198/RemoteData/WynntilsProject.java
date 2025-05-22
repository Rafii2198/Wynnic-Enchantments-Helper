package top.rafii2198.RemoteData;

import net.minecraft.MinecraftVersion;
import net.minecraft.client.resource.language.I18n;
import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.Utilities.HttpManager;

public class WynntilsProject {
    private static final String PROJECT = "https://api.modrinth.com/v2/project/dU5Gb9Ab/version?game_versions=[%22"
            + MinecraftVersion.CURRENT.getName() + "%22]";
    private static final ModrinthProject[] Json = HttpManager.getModrinthProject(PROJECT);

    public static String getChangelog() {
        return Json != null ? Json[0].getChangelog() : I18n.translate("we-helper.error.modrinth.changelog");
    }

    public static String getVersion() {
        return Json != null ? Json[0].getVersion_number().replaceAll("v", "") : "-1";
    }
}
