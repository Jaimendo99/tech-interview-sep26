package com.zonaconstru;

import com.zonaconstru.models.MobItem;
import com.zonaconstru.models.NotificationResponse;
import com.zonaconstru.models.Sorter;
import com.zonaconstru.utils.Result;

import java.util.ArrayList;
import java.util.List;


public class ItemServiceImp implements ItemService {
    private final MobItemRepository itemRepository;
    private final MobNotifier notifier;

    public ItemServiceImp(MobItemRepository itemRepository, MobNotifier notifier) {
        this.itemRepository = itemRepository;
        this.notifier = notifier;
    }

    @Override
    public List<MobItem> getAll(Sorter sorter) {
        Result<List<MobItem>, Exception> result = itemRepository.getAll();

        if (result instanceof Result.Ok<List<MobItem>, Exception> ok) {
            return sortItems(ok.value(), sorter);
        }

        if (result instanceof Result.Err<List<MobItem>, Exception> err) {
            throw new RuntimeException(err.error());
        }

        throw new IllegalStateException("Unknown result");
    }

    private List<MobItem> sortItems(List<MobItem> items, Sorter sorter) {
        return switch (sorter) {
            case NAME_AS -> {
                List<MobItem> sortedItems = new ArrayList<>(items);

                //TODO: implement sorting

                yield sortedItems;
            }
            case NAME_DES -> items;
            case ID_AS -> items;
            case ID_DES -> items;
        };
    }

    @Override
    public MobItem saveNew(MobItem item) {
        Result<String, Exception> result = itemRepository.save(item);
        if (result instanceof Result.Ok<String, Exception> ok) {
            return item;
        }
        if (result instanceof Result.Err<String, Exception> err) {
            throw new RuntimeException(err.error());
        }
        throw new IllegalStateException("Unknown result");
    }

    @Override
    public MobItem get(String id) {
        Result<MobItem, Exception> result = itemRepository.getById(id);
        if (result instanceof Result.Ok<MobItem, Exception> ok) {
            return ok.value();
        }
        if (result instanceof Result.Err<MobItem, Exception> err) {
            if (err.error().getMessage().equals("Mob not found")){
                return null;
            }else{
                throw new RuntimeException(err.error());
            }
        }
        throw new IllegalStateException("Unknown result");
    }

    @Override
    public NotificationResponse notifyItem(List<MobItem> items) {
        //TODO: add webhook to send notification
        String webhook = "";
        try{
            notifier.notify(items, webhook);
            return new NotificationResponse("Webhook sent succesfuly", 200);
        } catch (Exception e) {
            return new NotificationResponse("Webhook failed", 500);
        }
    }
}
