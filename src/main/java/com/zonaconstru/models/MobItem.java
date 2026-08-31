package com.zonaconstru.models;

public record MobItem(
        String id,
        String name,
        String category,
        String tier,
        int order,
        int sprite
) {}
