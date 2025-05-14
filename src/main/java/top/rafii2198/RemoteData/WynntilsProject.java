package top.rafii2198.RemoteData;

import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.Utilities.HttpManager;

public class WynntilsProject {
    private static final String PROJECT = "https://api.modrinth.com/v2/project/dU5Gb9Ab/version";
    private static final ModrinthProject[] Json = HttpManager.getModrinthProject(PROJECT);

    public static String getChangelog(){
        return Json[0].getChangelog();
    }
}
