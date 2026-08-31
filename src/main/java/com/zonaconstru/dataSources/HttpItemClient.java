package com.zonaconstru.dataSources;

import com.zonaconstru.models.MobItem;

import java.io.IOException;
import java.util.List;

public interface HttpItemClient {
    List<MobItem> getItems() throws IOException, InterruptedException;
}
