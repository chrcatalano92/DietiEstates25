package com.demo.dietiestates25.support_classes;

import android.widget.ImageButton;

import androidx.annotation.NonNull;

public class ViewHelpMsg {
    private ViewHelpMsg(){}

    public interface LongPressHandler {
        void handleLongPress(CharSequence contentDescription);
    }

    public static void longPress(@NonNull ImageButton imgBtn, @NonNull LongPressHandler handler) {
        imgBtn.setOnLongClickListener(view -> {
            CharSequence description = imgBtn.getContentDescription();

            if(description != null && !description.toString().isEmpty()) {
                handler.handleLongPress(description);
                return true;
            }
            return false;
        });
    }
}
