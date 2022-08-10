package com.github.jacobbrewer1.api;

import com.github.jacobbrewer1.entities.Bin;
import com.github.jacobbrewer1.entities.Person;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public abstract class BcpHelper {
    private final String endpoint;

    protected final Gson gson;

    protected BcpHelper(String endpoint, Gson gson) {
        this.endpoint = endpoint;
        this.gson = gson;
    }

    protected Bin[] getBinsRequest(Person person) throws IOException {
        String response = doRequest(String.valueOf(person.getUprn()));
        return gson.fromJson(response, Bin[].class);
    }

    private String doRequest(String uprn) throws IOException {
        HttpUrl.Builder httpUrl = Objects.requireNonNull(HttpUrl.parse(endpoint)).newBuilder();

        httpUrl.addQueryParameter("UPRN", uprn);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(httpUrl.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
