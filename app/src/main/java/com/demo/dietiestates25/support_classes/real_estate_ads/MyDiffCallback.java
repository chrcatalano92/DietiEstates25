package com.demo.dietiestates25.support_classes.real_estate_ads;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * La classe ha la Sola Responsabilità (SRP) di definire le regole per calcolare la differenza tra due liste di 'RealEstateAd'. Non fa altro.
 * BASSO ACCOPPIAMENTO: è accoppiato solo con 'DiffUtil.Callback' (necessario) e con il DTO
 */
public class MyDiffCallback extends DiffUtil.Callback {
    private final List<RealEstateAd> oldList;
    private final List<RealEstateAd> newList;

    public MyDiffCallback(List<RealEstateAd> oldList, List<RealEstateAd> newList) {
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
        // Confronta gli ID unici degli immobili
        long oldId = oldList.get(oldItemPosition).getIdProperty();
        long newId = newList.get(newItemPosition).getIdProperty();
        return oldId == newId;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
