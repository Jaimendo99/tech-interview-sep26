package com.zonaconstru.models;

import java.util.List;

public record MobItemCatalog(
        int revision,
        String updatedAt,
        List<String> tiers,
        List<MobItem> items
) {}
