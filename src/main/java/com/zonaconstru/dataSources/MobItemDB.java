package com.zonaconstru.dataSources;

import com.zonaconstru.models.MobItem;

import java.util.List;

public interface MobItemDB {
    void save(MobItem item);
    MobItem getById(String id);
    void delete(String id);
    void update(MobItem item);
    void deleteAll();
    List<MobItem> getAll();
    void saveAll(List<MobItem> items);
}
