package com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.support_component_filters_layout;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.MyViewModel;

public class SeekBarHelper {
    private SeekBarHelper(){}

    public static void seekBar(AppCompatSeekBar seekBar, TextView seekBarValue, int maxValue, MyViewModel model) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekBarValue.setVisibility(View.VISIBLE);
                seekBarValue.setText("€ " + progress + " / € " + maxValue);

                model.setPickerPrice(Math.max(progress, 0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not used
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not used
            }
        });
    }
}
