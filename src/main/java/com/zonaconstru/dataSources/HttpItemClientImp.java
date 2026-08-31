package com.zonaconstru.dataSources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonaconstru.models.MobItem;
import com.zonaconstru.models.MobItemCatalog;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpItemClientImp implements HttpItemClient {

    private final HttpClient client;
    private final String baseUrl;
    private final ObjectMapper mapper;

    public HttpItemClientImp(HttpClient client, String baseUrl) {
        this.client = client;
        this.baseUrl = baseUrl;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<MobItem> getItems() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseUrl))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        MobItemCatalog catalog = mapper.readValue(response.body(), MobItemCatalog.class);
        return catalog.items();
    }
}
