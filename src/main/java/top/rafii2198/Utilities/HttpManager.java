package top.rafii2198.Utilities;

import com.google.gson.Gson;
import top.rafii2198.RemoteData.Types.ModrinthProject;
import top.rafii2198.RemoteData.Types.WynncraftNewsApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpManager {
    private static final Gson GSON = new Gson();

    private static HttpResponse<String> fetchData(String uri){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uri)).build();
        LoggerUtils.info("Requesting JSON from: " + uri);
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static ModrinthProject[] getModrinthProject(String uri){
        HttpResponse<String> data = fetchData(uri);

        if (data.statusCode() != 200) return new ModrinthProject[0];
        LoggerUtils.info(String.valueOf(data.statusCode()));
        return GSON.fromJson(data.body(), ModrinthProject[].class);
    }
    public static WynncraftNewsApi[] getWynncraftNews(String uri){
        HttpResponse<String> data = fetchData(uri);
        if (data.statusCode() != 200) return new WynncraftNewsApi[0];
        return GSON.fromJson(data.body(), WynncraftNewsApi[].class);
    }
}
