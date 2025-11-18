package com.demo.dietiestates25.support_classes.view;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class ViewHelper {
    private ViewHelper(){}

    public static void cardViewOnClick(CardView c, String menuItem, ClickHandler handler) {
        c.setOnClickListener(view -> handler.handlerMenuItemClick(menuItem));
    }

    public static void txtViewOnClick(TextView txtV, String menuItem, ClickHandler handler) {
        txtV.setOnClickListener(view -> handler.handlerMenuItemClick(menuItem));
    }

    public static void setupCardViewClickListener(CardView c1, CardView c2, CardView c3, CardView c4, ClickHandler handler) {
        cardViewOnClick(c1, "c1", handler);
        cardViewOnClick(c2, "c2", handler);
        cardViewOnClick(c3, "c3", handler);
        cardViewOnClick(c4, "c4", handler);
    }
}
