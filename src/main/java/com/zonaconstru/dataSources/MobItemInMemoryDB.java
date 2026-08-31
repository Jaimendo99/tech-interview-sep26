package com.zonaconstru.dataSources;

import com.zonaconstru.models.MobItem;

import java.util.ArrayList;
import java.util.List;

public class MobItemInMemoryDB implements MobItemDB {

    private final List<MobItem> items;

    public MobItemInMemoryDB(List<MobItem> items) {
        this.items = new ArrayList<>(items);
    }

    @Override
    public void save(MobItem item) {
        items.add(item);
    }

    @Override
    public MobItem getById(String id) {
        return items.stream()
                .filter(item -> item.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        items.removeIf(item -> item.id().equals(id));
    }

    @Override
    public void update(MobItem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id().equals(item.id())) {
                items.set(i, item);
                return;
            }
        }
    }

    @Override
    public void deleteAll() {
        items.clear();
    }

    @Override
    public List<MobItem> getAll() {
        return new ArrayList<>(items);
    }

    @Override
    public void saveAll(List<MobItem> items) {
        this.items.addAll(items);
    }
}
