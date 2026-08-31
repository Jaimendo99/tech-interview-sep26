package com.zonaconstru;

import com.zonaconstru.models.MobItem;
import com.zonaconstru.models.NotificationResponse;
import com.zonaconstru.models.Sorter;

import java.util.List;

public interface ItemService {
    List<MobItem> getAll(Sorter sorter);
    MobItem saveNew(MobItem item);
    NotificationResponse notifyItem(MobItem item);
}
