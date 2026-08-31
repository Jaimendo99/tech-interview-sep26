package com.zonaconstru;

import com.zonaconstru.models.MobItem;
import com.zonaconstru.models.NotificationResponse;
import com.zonaconstru.models.Sorter;
import com.zonaconstru.utils.Result;

import java.util.List;

public class ItemServiceImp implements ItemService {
    private final MobItemRepository itemRepository;

    public ItemServiceImp(MobItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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
            case NAME_AS -> items; //TODO: implements sorting by name ascending
            case NAME_DES -> items;//TODO: implements sorting by name descending
            case ID_AS -> items;//TODO: implements sorting by id ascending
            case ID_DES -> items;//TODO: implements sorting by id descending
        };
    }

    @Override
    public MobItem saveNew(MobItem item) {
        return null;
    }

    @Override
    public NotificationResponse notifyItem(MobItem item) {
        return null;
    }
}
