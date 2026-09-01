package com.zonaconstru.models;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// TODO: Complete the record
@JsonPropertyOrder({ "id", "name", "category", "imageUrl", "sprite", "tier", "order" })
public record MobItem(
        String id,
        String name,
        String category,
        String imageUrl,
        int sprite,
        int order
) {}
