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

public class RemoteJson {
    public abstract static class Base {
        private final HttpRequest REQUEST;
        private final HttpClient HTTP_CLIENT;
        protected final Gson gson = new Gson();

        private Instant LAST_FETCH_TIME;
        ErrorOr<String> RAW_JSON_STRING;

        Base(String fetchUrl, HttpClient client) {
            this.REQUEST = HttpRequest.newBuilder()
                    .uri(URI.create(fetchUrl))
                    .GET()
                    .header("Accept", "application/json")
                    .build();
            this.HTTP_CLIENT = client;
            Request(true);
        }

        protected void UpdateRawJson(ErrorOr<String> json) {
            RAW_JSON_STRING = json;
        }

        public void Request(boolean force) {
            if (force
                    || (LAST_FETCH_TIME == null
                            || Duration.between(LAST_FETCH_TIME, Instant.now()).toMinutes() >= 15)) {
                LAST_FETCH_TIME = Instant.now();

                LoggerUtils.info("Fetching JSON from: " + REQUEST.uri().toString());
                CompletableFuture<HttpResponse<String>> future =
                        HTTP_CLIENT.sendAsync(REQUEST, HttpResponse.BodyHandlers.ofString());

                future.thenApply(HttpResponse::body)
                        .thenAccept(data -> UpdateRawJson(ErrorOr.of(data)))
                        .exceptionally(e -> {
                            UpdateRawJson(ErrorOr.error(e.getMessage()));
                            LoggerUtils.error(MessageFormat.format(
                                    "Error while fetching {0}:\n{1}",
                                    REQUEST.uri().toString(), e.getMessage()));
                            return null;
                        });
            }
        }
    }

    public static class Array<T> extends Base {
        private final Class<T[]> CLASS;
        private T[] Data;

        Array(String fetchUrl, HttpClient client, Class<T[]> clazz) {
            super(fetchUrl, client);
            this.CLASS = clazz;
        }

        @Override
        protected void UpdateRawJson(ErrorOr<String> json) {
            super.UpdateRawJson(json);
            if (json.hasError()) return;
            Data = gson.fromJson(json.getValue(), CLASS);
        }

        @Nullable public ErrorOr<T[]> get() {
            Request(false);
            if (RAW_JSON_STRING == null) return null;
            if (RAW_JSON_STRING.hasError()) return ErrorOr.error(RAW_JSON_STRING.getError());
            return ErrorOr.of(Data);
        }
    }

    public static class Single<T> extends Base {
        private final Class<T> CLASS;
        private T Data;

        Single(String fetchUrl, HttpClient client, Class<T> clazz) {
            super(fetchUrl, client);
            this.CLASS = clazz;
        }

        @Override
        protected void UpdateRawJson(ErrorOr<String> json) {
            super.UpdateRawJson(json);
            if (json.hasError()) return;
            Data = gson.fromJson(json.getValue(), CLASS);
        }

        @Nullable public ErrorOr<T> get() {
            Request(false);
            if (RAW_JSON_STRING == null) return null;
            if (RAW_JSON_STRING.hasError()) return ErrorOr.error(RAW_JSON_STRING.getError());
            return ErrorOr.of(Data);
        }
    }
}
