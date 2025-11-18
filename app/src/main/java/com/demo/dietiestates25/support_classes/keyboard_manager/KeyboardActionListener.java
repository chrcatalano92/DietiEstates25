package com.demo.dietiestates25.support_classes.keyboard_manager;

import android.view.KeyEvent;
import android.widget.TextView;

public interface KeyboardActionListener {
    boolean onEditorAction(TextView txtV, int actionId, KeyEvent keyEvent, String currTxt);
}
