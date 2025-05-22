package top.rafii2198.RemoteData;

import net.minecraft.client.resource.language.I18n;
import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.Utilities.HttpManager;

public class WynnicEnchantmentsProject {
    private static final String PROJECT = "https://api.modrinth.com/v2/project/WMjBYFp1/version";
    private static final ModrinthProject[] Json = HttpManager.getModrinthProject(PROJECT);

    public static String getChangelog() {
        return Json != null ? Json[0].getChangelog() : I18n.translate("we-helper.error.modrinth.changelog");
    }

    public static String getVersion() {
        return Json != null ? Json[0].getVersion_number() : "-1";
    }
}
