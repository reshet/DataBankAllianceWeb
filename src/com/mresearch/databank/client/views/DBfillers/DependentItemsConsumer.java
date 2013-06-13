package com.mresearch.databank.client.views.DBfillers;

import java.util.ArrayList;
import java.util.List;

public interface DependentItemsConsumer {
	void updateItems(ArrayList<String> items_names,ArrayList<Long> items_ids);
}
