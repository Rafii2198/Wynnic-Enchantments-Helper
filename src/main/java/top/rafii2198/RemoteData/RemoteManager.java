package top.rafii2198.RemoteData;

import java.net.http.HttpClient;
import java.util.List;
import net.minecraft.SharedConstants;
import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.RemoteData.Types.WynncraftNewsApi;

public final class RemoteManager {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    public static final RemoteJson.Array<ModrinthProject> WynnicEnchantments = new RemoteJson.Array<>(
            "https://api.modrinth.com/v2/project/WMjBYFp1/version", HTTP_CLIENT, ModrinthProject[].class);
    public static final RemoteJson.Array<ModrinthProject> Wynntils = new RemoteJson.Array<>(
            "https://api.modrinth.com/v2/project/dU5Gb9Ab/version?game_versions=[%22"
                    + SharedConstants.getCurrentVersion().name() + "%22]",
            HTTP_CLIENT,
            ModrinthProject[].class);
    public static final RemoteJson.Single<WynncraftNewsApi> WynncraftNews = new RemoteJson.Single<>(
            "https://api.wynncraft.com/v3/publisher/articles/list/article", HTTP_CLIENT, WynncraftNewsApi.class);

    public final List<? extends RemoteJson.Base> All_Remotes = List.of(WynnicEnchantments, Wynntils, WynncraftNews);

    public void ForceRequestAll() {
        All_Remotes.forEach(remoteJson -> remoteJson.Request(true));
    }

    public static void init() {}
}
