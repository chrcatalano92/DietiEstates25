package com.demo.dietiestates25.support_classes.keyboard_manager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.textfield.TextInputEditText;

public class KeyboardEventHelper {
    private KeyboardActionListener listener;
    private InputMethodManager imm;

    public KeyboardEventHelper(Context context) { initImm(context); }

    public KeyboardEventHelper(Activity activity) { initImm(activity); }

    private void initImm(Context context) { this.imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); }

    public void setKeyboardActionListener(KeyboardActionListener listener) { this.listener = listener; }

    public void keyboardManager(TextInputEditText editText) {
        editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            String txtV = textView.getText().toString().trim();

            if(listener != null) {
                return listener.onEditorAction(textView, actionId, keyEvent, txtV);
            }

            return false;
        });
    }

    public void moveToNextEditText(TextInputEditText nextEditText) {
        if(nextEditText != null) {
            nextEditText.post(() -> {
                nextEditText.requestFocus();
                showKeyboard(nextEditText);
            });
        }
    }

    public void closeKeyboard(View view) {
        if(imm != null && view.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideKeyboardForSearchView(SearchView searchView) {
        if(imm != null && searchView.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            searchView.clearFocus();
        }
    }

    public void showKeyboard(View view) {
        if(imm != null && view != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
