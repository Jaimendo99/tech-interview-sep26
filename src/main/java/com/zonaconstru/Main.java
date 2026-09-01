package com.zonaconstru;

import com.zonaconstru.dataSources.MobItemInMemoryDB;
import com.zonaconstru.dataSources.HttpItemClientImp;
import com.zonaconstru.dataSources.MobWebhookNotifier;
import com.zonaconstru.models.MobItem;
import com.zonaconstru.models.Sorter;

import java.net.http.HttpClient;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ItemService itemService = new ItemServiceImp(new MobItemRepositoryImpl(
                new HttpItemClientImp(HttpClient.newHttpClient(), "http://localhost:3000/"),
                new MobItemInMemoryDB(List.of())
        ), new MobWebhookNotifier(HttpClient.newHttpClient()));

        List<MobItem> mobs =  itemService.getAll(Sorter.NAME_AS);
        for  (MobItem item : mobs) {
            MobItem mob = itemService.saveNew(item);
            System.out.println(mob);
        }
        System.out.println("saved all items");

        while (!Thread.currentThread().isInterrupted()) {
            List<MobItem> items = itemService.getAll(Sorter.NAME_AS);

            for (MobItem item : items) {
                MobItem mob = itemService.get(item.id());
                if (mob == null) {
                    itemService.notifyItem(items);
                    itemService.saveNew(item);
                }
            }
            try {
                Thread.sleep(1000); // wait 10 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
