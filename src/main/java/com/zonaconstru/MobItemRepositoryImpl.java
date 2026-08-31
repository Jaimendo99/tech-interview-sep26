package com.zonaconstru;

import com.zonaconstru.dataSources.MobItemDB;
import com.zonaconstru.dataSources.HttpItemClient;
import com.zonaconstru.models.MobItem;
import com.zonaconstru.utils.Result;

import java.io.IOException;
import java.util.List;

public class MobItemRepositoryImpl implements MobItemRepository {
    private final HttpItemClient client;
    private final MobItemDB db;

    public MobItemRepositoryImpl(HttpItemClient client, MobItemDB db) {
        this.client = client;
        this.db = db;
    }

    @Override
    public Result<List<MobItem>, Exception> getAll() {
        try {
            List<MobItem> items = client.getItems();
            return new Result.Ok<>(items);
        } catch (IOException | InterruptedException e) {
            return new Result.Err<>(e);
        }
    }

    @Override
    public Result<String, Exception> save(MobItem item) {
        try {
            db.save(item);
            return new Result.Ok<>("Mob saved");
        } catch (Exception e) {
            return new Result.Err<>(e);
        }
    }

    @Override
    public Result<MobItem, Exception> getById(String id) {
        try {
            MobItem item = db.getById(id);
            if (item == null) {
                return new Result.Err<>(new Exception("Mob not found"));
            }
            return new Result.Ok<>(item);
        } catch (Exception e) {
            return new Result.Err<>(e);
        }
    }
}
