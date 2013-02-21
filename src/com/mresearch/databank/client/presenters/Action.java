package com.mresearch.databank.client.presenters;

import com.mresearch.databank.shared.IShowPlaceParameters;

public interface Action {
	void performAction(String action,IShowPlaceParameters params);
}
