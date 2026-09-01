package com.zonaconstru.dataSources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonaconstru.MobNotifier;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MobWebhookNotifier implements MobNotifier {
    private final HttpClient client;
    private final ObjectMapper mapper;

    public MobWebhookNotifier(HttpClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void notify(Object data, String destination) {
        try {
            String json = mapper.writeValueAsString(data);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(destination))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
