package com.demo.dietiestates25.support_classes.keyboard_manager;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.textfield.TextInputEditText;

public class SetupKeyboardEvent {
    private final KeyboardEventHelper keyboardHelper;
    private static final int IME_ACTION_NEXT = EditorInfo.IME_ACTION_NEXT;
    private static final int IME_ACTION_DONE = EditorInfo.IME_ACTION_DONE;
    public SetupKeyboardEvent(Context context){
        this.keyboardHelper = new KeyboardEventHelper(context);
    }

    public void closeKeyboardWithActionNext(TextInputEditText editText, TextInputEditText nextEditText) {
        closeKeyboardWithAction(editText, nextEditText);
    }

    public void closeKeyboardWithActionDone(TextInputEditText editText) {
        closeKeyboardWithAction(editText, null);
    }

    private void closeKeyboardWithAction(TextInputEditText currEditText, TextInputEditText nextEditText) {
        keyboardHelper.setKeyboardActionListener((txtV, actionId, keyEvent, currTxt) -> {
            boolean isEnterKey = keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;

            if((actionId == IME_ACTION_NEXT || isEnterKey) && currTxt.endsWith(".")) {
                keyboardHelper.closeKeyboard(txtV);

                keyboardHelper.moveToNextEditText(nextEditText);

                return true;
            }
            return false;
        });

        currEditText.setImeOptions(nextEditText != null ? IME_ACTION_NEXT : IME_ACTION_DONE);
        keyboardHelper.keyboardManager(currEditText);
    }
}
