package com.demo.dietiestates25;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.dietiestates25.databinding.ActivityAdvancedSearchBinding;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.Filter;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.FilterAdapter;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.FilterUtils;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.MyViewModel;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.StringPickerHelper;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.ViewFilterHelper;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdvancedSearchActivity extends AppCompatActivity {
    private ViewFilterHelper viewFilterHelper;
    public static final int CHOICE_MODE_SINGLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewFilterHelper = new ViewFilterHelper(this);

        ActivityAdvancedSearchBinding binding = ActivityAdvancedSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.advanced_search);

        ListView filtersList = findViewById(R.id.filtersList);
        FrameLayout filterContainer = findViewById(R.id.filtersContainer);
        Button showBtnResult = findViewById(R.id.showResultBtn);

        filtersViewInitializer();
        filtersManager(filtersList, filterContainer, showBtnResult);
    }

    private void filtersViewInitializer() {
        viewFilterHelper.setClassifyPropertyFilterV();
        viewFilterHelper.setPriceFilterV();
        viewFilterHelper.setRoomsFilterV();
        viewFilterHelper.setEnergyFilterV();
        viewFilterHelper.setPositionFilterV();
        viewFilterHelper.setRadiusFilterV();
    }

    private void filtersManager(ListView filtersList, FrameLayout filterContainer, Button showBtnResult) {
        List<Filter> filters = new ArrayList<>();
        FilterAdapter adapter;
        MyViewModel myViewModel = FilterUtils.setMyViewModel(this);

        String filter1Title = getFilterTitle(R.string.listing_type);
        Filter filter1 = createClassifyPropertyFilter(myViewModel, filter1Title, R.array.filter1_options_sailAndRent);
        FilterUtils.addFilter(filters, filter1);

        String filter2Title = getFilterTitle(R.string.price);
        Filter filter2 = createPricesPropertyFilter(myViewModel, filter2Title);
        FilterUtils.addFilter(filters, filter2);

        String filter3Title = getFilterTitle(R.string.rooms);
        Filter filter3 = createRoomsPropertyFilter(myViewModel, filter3Title);
        FilterUtils.addFilter(filters, filter3);

        String filter4Title = getFilterTitle(R.string.energy_class);
        String[] data = StringPickerHelper.energyClassData();
        Filter filter4 = createEnergyClassPropertyFilter(myViewModel, filter4Title, data);
        FilterUtils.addFilter(filters, filter4);

        String filter5Title = getFilterTitle(R.string.position_filter);
        Filter filter5 = createPositionPropertyFilter(myViewModel, filter5Title);
        FilterUtils.addFilter(filters, filter5);

        String filter6Title = getFilterTitle(R.string.radius);
        String[] dataRadius = new String[]{"-", "<= 100 m", "200 m", "500 m","1 km", "2 km", "5 km", ">= 10 km"};
        Filter filter6 = createRadiusPropertyFilter(myViewModel, filter6Title, dataRadius);
        FilterUtils.addFilter(filters, filter6);

        adapter = new FilterAdapter(this, filters);
        filtersList.setAdapter(adapter);
        filtersList.setChoiceMode(CHOICE_MODE_SINGLE);

        FilterUtils.onClickFilter(this, filtersList, filterContainer, filters, showBtnResult, viewFilterHelper);
    }

    private Filter createClassifyPropertyFilter(MyViewModel myViewModel, String filterTitle, int filterOptionsId) {
        View filterV = viewFilterHelper.getClassifyPropertyFilterV();
        MyViewModel contentFilter = FilterUtils.contentSpinnerNewFilter(this, filterV, myViewModel, filterOptionsId);

        return FilterUtils.newFilter(filterTitle, filterV, contentFilter);
    }

    private Filter createPricesPropertyFilter(MyViewModel myViewModel, String filterTitle) {
        View filterV = viewFilterHelper.getPriceFilterV();
        MyViewModel contentFilter = FilterUtils.contentSeekBarAndEditTxtNewFilter(filterV, myViewModel);

        return FilterUtils.newFilter(filterTitle, filterV, contentFilter);
    }

    private Filter createRoomsPropertyFilter(MyViewModel myViewModel, String filterTitle) {
        View filterV = viewFilterHelper.getRoomsFilterV();
        MyViewModel contentFilter = FilterUtils.contentNumberPickerAndSwitchNewFilter(filterV, myViewModel);

        return FilterUtils.newFilter(filterTitle, filterV, contentFilter);
    }

    private Filter createEnergyClassPropertyFilter(MyViewModel myViewModel, String filterTitle, String[] data) {
        View filterV = viewFilterHelper.getEnergyFilterV();
        MyViewModel contentFilter = FilterUtils.contentEnergyClassPickerNewFilter(filterV, data, myViewModel);

        return FilterUtils.newFilter(filterTitle, filterV, contentFilter);
    }

    private Filter createPositionPropertyFilter(MyViewModel myViewModel, String filterTitle) {
        View filterV = viewFilterHelper.getPositionFilterV();
        MyViewModel contentFilter = FilterUtils.contentEditTxtNewFilter(filterV, myViewModel);

        return FilterUtils.newFilter(filterTitle, filterV, contentFilter);
    }

    private Filter createRadiusPropertyFilter(MyViewModel myViewModel, String filterTitle, String[] data) {
        View filterV = viewFilterHelper.getRadiusFilterV();
        MyViewModel contentFilter = FilterUtils.contentRadiusPickerNewFilter(filterV, data, myViewModel);

        return FilterUtils.newFilter(filterTitle, filterV, contentFilter);
    }

    private String getFilterTitle(int filterTitleId) { return ResourceUtils.getXMLString(this, filterTitleId); }
}