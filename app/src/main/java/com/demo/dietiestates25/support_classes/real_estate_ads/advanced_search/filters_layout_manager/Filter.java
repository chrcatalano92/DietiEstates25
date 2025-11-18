package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager;

import android.view.View;

public class Filter {
    private final String name;
    private final View contentView;
    private final MyViewModel filterData;

    public Filter(String name, View contentView, MyViewModel filterData) {
        this.name = name;
        this.contentView = contentView;
        this.filterData = filterData;
    }

    public String getName() {
        return name;
    }

    public View getContentView() {
        return contentView;
    }

    public MyViewModel getFilterData() {
        return filterData;
    }
}
