package com.demo.dietiestates25.support_classes.google_map;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.utils.GoogleMapsUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Objects;

public class MDialogManagementHelper {
    private MDialogManagementHelper(){}

    public static void mapDialog(Dialog dialog, String address, FragmentActivity activity, GoogleMap gMap) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_map_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView addressTitle = dialog.findViewById(R.id.txtAddressMap);
        addressTitle.setText(address);

        SupportMapFragment smf = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map_container);

        ImageButton closeBtn = dialog.findViewById(R.id.close_btn);
        SupportMapFragment finalSmf = smf;
        closeBtn.setOnClickListener(view -> {
            final SupportMapFragment localSmf = finalSmf;

            dialog.setOnDismissListener(dialogInterface -> {
                if(gMap != null) {
                    gMap.clear();
                    gMap.setOnMarkerClickListener(null);
                    gMap.setOnCameraMoveListener(null);
                }

                if(localSmf != null) {
                    activity.getSupportFragmentManager().beginTransaction().remove(localSmf).commit();
                }

                GoogleMapsUtils.stopBlinking();
            });
            dialog.dismiss();
        });

        if(smf == null) {
            smf = SupportMapFragment.newInstance();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, smf)
                    .commit();
        }
        else {
            smf.getMapAsync((OnMapReadyCallback) dialog);
        }
    }
}
