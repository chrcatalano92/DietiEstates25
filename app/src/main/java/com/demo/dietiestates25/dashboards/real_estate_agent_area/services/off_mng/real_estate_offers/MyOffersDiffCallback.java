package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.real_estate_offers;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class MyOffersDiffCallback extends DiffUtil.Callback {
    private final List<OfferData> oldList;
    private final List<OfferData> newList;

    public MyOffersDiffCallback(List<OfferData> oldList, List<OfferData> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
