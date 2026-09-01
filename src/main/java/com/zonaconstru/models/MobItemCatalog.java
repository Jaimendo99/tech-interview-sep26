package com.zonaconstru.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "revision", "updatedAt", "tiers", "items" })
public record MobItemCatalog(
        int revision,
        String updatedAt,
        List<String> tiers,
        List<MobItem> items
) {}
