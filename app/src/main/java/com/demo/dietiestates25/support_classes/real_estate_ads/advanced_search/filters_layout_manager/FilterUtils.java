package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.CustomSpinnerArrayAdapter;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.EditTextDoublePriceHelper;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.EditTextHelper;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.NumberPickerHelper;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.SeekBarHelper;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.SpinnerHelper;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.StringPickerHelper;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout.SwitchHelper;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

/* È una classe Factory e Utils (dovrebbe essere public final class).
La sua Sola Responsabilità (SRP) è costruire e configurare l'interfaccia dei filtri di ricerca avanzata.
 - Factory: Ha metodi create...Filter che assemblano una View, un MyViewModel e un Filter (un DTO).
 - Utils: Ha metodi helper (getViewFilter, addFilter) e metodi per collegare i listener (onClickFilter). */

/**
 * Classe Factory/Utils STATICA (final) per costruire e gestire
 * i componenti della UI della Ricerca Avanzata.
 * SRP: Assembla i filtri e collega i listener.
 */
public final class FilterUtils {
    private FilterUtils() {}

    public static View getViewFilter(LayoutInflater inflater, int layoutFilterId) {
        return inflater.inflate(layoutFilterId, null);
    }

    public static void addFilter(List<Filter> filters, Filter filter) {
        filters.add(filter);
    }

    public static Filter newFilter(String filterTitle, View filterView, MyViewModel filterData) {
        return new Filter(filterTitle, filterView, filterData);
    }

    public static MyViewModel setMyViewModel(Context context) {
        return new ViewModelProvider((ViewModelStoreOwner) context).get(MyViewModel.class);
    }

    public static MyViewModel contentSpinnerNewFilter(Context context, View filterV, MyViewModel viewModel, int filterOptionsId) {
        CharSequence[] items = context.getResources().getTextArray(filterOptionsId);
        CustomSpinnerArrayAdapter adapter = new CustomSpinnerArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items);

        Spinner spinner = filterV.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        SpinnerHelper.spinner(context, spinner, viewModel);

        return (viewModel.getClassifyProperty() != null) ? viewModel : null;
    }

    public static MyViewModel contentSeekBarAndEditTxtNewFilter(View filterV, MyViewModel viewModel) {
        AppCompatSeekBar seekBar = filterV.findViewById(R.id.seekbar);
        TextView seekbarValue = filterV.findViewById(R.id.seekbar_value);
        TextInputEditText eTxtValue = filterV.findViewById(R.id.priceEditTxt);

        SeekBarHelper.seekBar(seekBar, seekbarValue,100000, viewModel);

        EditTextDoublePriceHelper.doublePriceEditText(eTxtValue, viewModel);

        return (viewModel.getPickerPrice() != null || viewModel.getFieldPrice() != null) ? viewModel : null;
    }

    public static MyViewModel contentNumberPickerAndSwitchNewFilter(View filterV, MyViewModel viewModel) {
        NumberPicker numPicker = filterV.findViewById(R.id.numberPicker);
        ImageButton btnDecPick = filterV.findViewById(R.id.btnDecrementPicker);
        ImageButton btnIncPick = filterV.findViewById(R.id.btnIncrementPicker);
        MaterialSwitch matSwitch = getMaterialSwitch(filterV);
        matSwitch.setText(R.string.more_rooms);

        NumberPickerHelper.numberPicker(numPicker, viewModel);
        NumberPickerHelper.decrementPicker(btnDecPick, numPicker, viewModel);
        NumberPickerHelper.incrementPicker(btnIncPick, numPicker, viewModel);

        SwitchHelper.materialSwitch(matSwitch, viewModel::setMoreThanTenRooms);

        return (viewModel.getRooms() != null || viewModel.getMoreThanTenRooms() != null) ? viewModel : null;
    }

    public static MyViewModel contentEnergyClassPickerNewFilter(View filterV, String[] data, MyViewModel viewModel) {
        NumberPicker picker = getStringPicker(filterV);
        StringPickerHelper.energyClassPicker(picker, data, viewModel);

        return (viewModel.getEnergyClass() != null) ? viewModel : null;
    }

    public static MyViewModel contentRadiusPickerNewFilter(View filterV, String[] data, MyViewModel viewModel) {
        NumberPicker picker = getStringPicker(filterV);
        StringPickerHelper.radiusPicker(picker, data, viewModel);
        MaterialSwitch matSwitch = getMaterialSwitch(filterV);
        matSwitch.setText(R.string.use_accurate_location);

        SwitchHelper.materialSwitch(matSwitch, viewModel::setAccuratePosition);

        return (viewModel.getRadius() != null) ? viewModel : null;
    }

    public static MyViewModel contentEditTxtNewFilter(View filterV, MyViewModel viewModel) {
        TextInputEditText editText = filterV.findViewById(R.id.cityEditTxt);
        EditTextHelper.cityEditText(editText, viewModel);

        return (viewModel.getCity() != null) ? viewModel : null;
    }

    private static NumberPicker getStringPicker(View filterV) {
        return filterV.findViewById(R.id.stringPicker);
    }

    public static MaterialSwitch getMaterialSwitch(View filterV) {
        return filterV.findViewById(R.id.matSwitch);
    }

    public static void onClickFilter(Context context, ListView filtersList, FrameLayout filterContainer, List<Filter> filters, Button showBtnResult, ViewFilterHelper viewFilterHelper) {
        filtersList.setOnItemClickListener((adapterView, view, position, id) -> {
            Filter selectedFilter = filters.get(position);

            displayFilterOnView(position, selectedFilter, filterContainer, filtersList);

            filterManager(context, showBtnResult, selectedFilter, viewFilterHelper);
        });
    }

    private static void displayFilterOnView(int position, Filter selectedFilter, FrameLayout filterContainer, ListView filtersList) {
        filterContainer.removeAllViews();

        View filterV = selectedFilter.getContentView();

        if(filterV != null) {
            filterContainer.addView(filterV);
        }
        else {
            filterContainer.removeAllViews();
        }

        filtersList.setItemChecked(position, true);
    }

    private static void filterManager(Context context, Button showBtnResult, Filter selectedFilter, ViewFilterHelper viewFilterHelper) {
        showBtnResult.setOnClickListener(view -> {
            MyViewModel filterData = selectedFilter.getFilterData();

            if(filterData != null) {
                new FiltersResult(context, filterData, viewFilterHelper);
            }
        });
    }
}
