package top.rafii2198.RemoteData;

import java.net.http.HttpClient;
import java.util.List;
import net.minecraft.SharedConstants;
import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.RemoteData.Types.WynncraftNewsApi;

public final class RemoteManager {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();

    public static final RemoteJson<ModrinthProject> WynnicEnchantments = new RemoteJson<>(
            "https://api.modrinth.com/v2/project/WMjBYFp1/version", HTTP_CLIENT, ModrinthProject[].class);
    public static final RemoteJson<ModrinthProject> Wynntils = new RemoteJson<>(
            "https://api.modrinth.com/v2/project/dU5Gb9Ab/version?game_versions=[%22"
                    + SharedConstants.getCurrentVersion().name() + "%22]",
            HTTP_CLIENT,
            ModrinthProject[].class);
    public static final RemoteJson<WynncraftNewsApi> WynncraftNews =
            new RemoteJson<>("https://api.wynncraft.com/v3/latest-news", HTTP_CLIENT, WynncraftNewsApi[].class);

    public final List<RemoteJson<?>> All_Remotes = List.of(WynnicEnchantments, Wynntils, WynncraftNews);

    public void ForceRequestAll() {
        All_Remotes.forEach(remoteJson -> remoteJson.Request(true));
    }

    public static void init() {}
}
