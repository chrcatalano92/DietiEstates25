package com.demo.dietiestates25.support_classes;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.demo.dietiestates25.R;

public class SlidePanelHelper {
    private static final int DEFAULT_ANIMATION_DURATION = 300;
    private SlidePanelHelper(){}

    public static void slidePanelManager(LinearLayout sp, ImageButton btn, int openImgId, int closeImgId) {
        final boolean[] isPanelOpen = {false};

        btn.setOnClickListener(view -> {
            if(isPanelOpen[0]) {
                closePanel(sp, btn);
            }
            else {
                openPanel(sp, btn);
            }
            isPanelOpen[0] = !isPanelOpen[0];
        });
        updateToggleBtn(btn, isPanelOpen, openImgId, closeImgId);
    }

    private static void openPanel(LinearLayout sp, ImageButton imgBtn) {
        sp.setVisibility(View.VISIBLE);
        sp.setTranslationX(-sp.getWidth());
        sp.animate()
                .translationX(0)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();

        imgBtn.animate()
                .rotation(-180f)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();
    }

    private static void closePanel(LinearLayout sp, ImageButton imgBtn) {
        sp.animate()
                .translationX(-sp.getWidth())
                .withEndAction( () -> sp.setVisibility(View.GONE))
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();

        imgBtn.animate()
                .rotation(0f)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();
    }

    private static void updateToggleBtn(ImageButton btn, boolean[] isPanelOpen, int openImgId, int closeImgId) {
        btn.setImageResource(isPanelOpen[0] ? closeImgId : openImgId);
        btn.setRotation(isPanelOpen[0] ? 180f : 0f);
    }

    public static void toggleImgVisibility(LinearLayout slidePanel, ImageButton imgBtn) {
        final boolean[] isImgVisible = {slidePanel.getVisibility() == View.GONE};

        imgBtn.setOnClickListener(view -> {
            if(isImgVisible[0]) {
                visibilityManager(slidePanel, imgBtn, View.GONE, R.drawable.HideImg);
            }
            else {
                visibilityManager(slidePanel, imgBtn, View.VISIBLE, R.drawable.ShowImg);
            }
            isImgVisible[0] = !isImgVisible[0];
        });
    }

    public static void visibilityManager(LinearLayout sp, ImageButton imgBtn, int visibility, int imgId) {
        sp.setVisibility(visibility);
        imgBtn.setImageResource(imgId);
    }
}
