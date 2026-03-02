package top.rafii2198.RemoteData;

import com.google.gson.Gson;
import com.wynntils.utils.type.ErrorOr;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import org.jspecify.annotations.Nullable;
import top.rafii2198.Utilities.LoggerUtils;

public class RemoteJson<T> {
    private final Class<T[]> CLASS;
    private final HttpRequest REQUEST;
    private final HttpClient HTTP_CLIENT;
    private final Gson gson = new Gson();

    private Instant LAST_FETCH_TIME;
    private ErrorOr<String> RAW_JSON_STRING;
    private T[] Data;

    RemoteJson(String fetchUrl, HttpClient client, Class<T[]> clazz) {
        this.REQUEST = HttpRequest.newBuilder()
                .uri(URI.create(fetchUrl))
                .GET()
                .header("Accept", "application/json")
                .build();
        this.HTTP_CLIENT = client;
        this.CLASS = clazz;
        Request(true);
    }

    public void Request(boolean force) {
        if (force
                || (LAST_FETCH_TIME == null
                        || Duration.between(LAST_FETCH_TIME, Instant.now()).toMinutes() >= 10)) {
            LAST_FETCH_TIME = Instant.now();

            LoggerUtils.info("Fetching JSON from: " + REQUEST.uri().toString());
            CompletableFuture<HttpResponse<String>> future =
                    HTTP_CLIENT.sendAsync(REQUEST, HttpResponse.BodyHandlers.ofString());

            future.thenApply(HttpResponse::body)
                    .thenAccept(data -> {
                        RAW_JSON_STRING = ErrorOr.of(data);
                        Data = gson.fromJson(data, CLASS);
                    })
                    .exceptionally(e -> {
                        RAW_JSON_STRING = ErrorOr.error(e.getMessage());
                        LoggerUtils.error(MessageFormat.format(
                                "Error while fetching {0}:\n{1}", REQUEST.uri().toString(), e.getMessage()));
                        return null;
                    });
        }
    }

    @Nullable public ErrorOr<T[]> get() {
        Request(false);
        if (RAW_JSON_STRING == null) return null;
        if (RAW_JSON_STRING.hasError()) return ErrorOr.error(RAW_JSON_STRING.getError());
        return ErrorOr.of(Data);
    }
}
