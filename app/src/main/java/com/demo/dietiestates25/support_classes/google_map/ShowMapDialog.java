package com.demo.dietiestates25.support_classes.google_map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class ShowMapDialog extends Dialog implements OnMapReadyCallback {
    private final DialogManager dialogManager;
    private GoogleMap gMap;
    private final String address;
    private final FragmentActivity activity;

    public ShowMapDialog(Context context, String address) {
        super(context);

        this.dialogManager = new DialogManager(context);
        this.activity = (FragmentActivity) context;
        this.address = address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MDialogManagementHelper.mapDialog(this, address, activity, gMap);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        this.gMap = gMap;

        LocationHelper.getLocationFromAddress(dialogManager, getContext(), gMap, address, activity, 15, true);
    }
}
