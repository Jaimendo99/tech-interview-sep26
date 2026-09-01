package com.zonaconstru;

import com.zonaconstru.models.MobItem;
import com.zonaconstru.models.NotificationResponse;
import com.zonaconstru.models.Sorter;

import java.util.List;

public interface ItemService {
    List<MobItem> getAll(Sorter sorter);
    MobItem saveNew(MobItem item);
    MobItem get(String id);
    NotificationResponse notifyItem(List<MobItem> items);
}
