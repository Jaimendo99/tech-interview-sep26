package com.zonaconstru;

import com.zonaconstru.models.MobItem;
import com.zonaconstru.utils.Result;

import java.util.List;

public interface MobItemRepository {
    Result<List<MobItem>, Exception> getAll();
    Result<String, Exception> save(MobItem item);
    Result<MobItem, Exception> getById(String id);
}
