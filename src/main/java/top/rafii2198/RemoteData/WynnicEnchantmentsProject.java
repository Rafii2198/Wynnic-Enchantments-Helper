package top.rafii2198.RemoteData;

import com.google.gson.Gson;
import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.Utilities.HttpManager;
import top.rafii2198.Utilities.LoggerUtils;

public class WynnicEnchantmentsProject {
    private static final String PROJECT = "https://api2322.modrinth.com/v2/project/WMjBYFp1/version";
    private static final ModrinthProject[] Json = HttpManager.getModrinthProject(PROJECT);

    public static String getChangelog(){
        LoggerUtils.info(new Gson().toJson(Json));
        return Json[0].getChangelog();
    }

    public static String getVersion(){
        LoggerUtils.info(new Gson().toJson(Json));
        return Json[0].getVersion_number();
    }
}
