package top.rafii2198.Utilities;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.RemoteData.Types.WynncraftNewsApi;

public class HttpManager {
    private static final Gson GSON = new Gson();

    private static HttpResponse<String> fetch(String uri) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =
                HttpRequest.newBuilder().GET().uri(URI.create(uri)).build();
        LoggerUtils.info("Requesting JSON from: " + uri);
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static ModrinthProject[] getModrinthProject(String uri) {
        HttpResponse<String> data;
        try {
            data = fetch(uri);
            if (data.statusCode() != 200) return null;
            return GSON.fromJson(data.body(), ModrinthProject[].class);
        } catch (Exception e) {
            LoggerUtils.error(e.getMessage());
            return null;
        }
    }

    public static WynncraftNewsApi[] getWynncraftNews(String uri) {
        HttpResponse<String> data;
        try {
            data = fetch(uri);
            if (data.statusCode() != 200) return null;
            return GSON.fromJson(data.body(), WynncraftNewsApi[].class);
        } catch (Exception e) {
            LoggerUtils.error(e.getMessage());
            return null;
        }
    }
}
