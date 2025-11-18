package com.demo.dietiestates25.support_classes.dialog_utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class DialogHelper {
    public static final String TAG = "DialogHelper";
    private final Map<Integer, DialogBtnClickListener> additionalButtons = new HashMap<>();
    private final Context context;
    private AlertDialog dialog;
    private View dialogView;
    private int layoutResId;
    private boolean backgroundTransparent = false;
    private boolean cancelable = true;
    private int positiveBtnId = -1;
    private int negativeBtnId = -1;
    private DialogBtnClickListener positiveBtnListener;
    private DialogBtnClickListener negativeBtnListener;

    public interface DialogBtnClickListener {
        void onClick(View dialogView);
    }

    public DialogHelper(Context context) {
        this.context = context;
    }

    public DialogHelper setLayout(int layoutResId) {
        this.layoutResId = layoutResId;
        return this;
    }

    public void setDialogContainerView(ViewGroup root) { this.dialogView = LayoutInflater.from(context).inflate(layoutResId, root); }

    public void setBackgroundTransparent(boolean backgroundTransparent) { this.backgroundTransparent = backgroundTransparent; }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setCustomPositiveBtn(int btnId, DialogBtnClickListener listener) {
        this.positiveBtnId = btnId;
        this.positiveBtnListener = listener;
    }

    public void setCustomNegativeBtn(int btnId, DialogBtnClickListener listener) {
        this.negativeBtnId = btnId;
        this.negativeBtnListener = listener;
    }

    public void addAdditionalBtn(int btnId, DialogBtnClickListener listener) { this.additionalButtons.put(btnId, listener); }

    public void show() {
        dialog = dialogInit(dialogView);

        if(backgroundTransparent && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        setupCustomPositiveNegativeButtons(dialogView);
        setupAdditionalButtons(dialogView);

        dialog.show();
    }

    private AlertDialog dialogInit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(cancelable);

        return builder.create();
    }

    private void setupCustomPositiveNegativeButtons(View dialogView) {
        setupSingleBtn(dialogView, positiveBtnId, positiveBtnListener);
        setupSingleBtn(dialogView, negativeBtnId, negativeBtnListener);
    }

    private void setupAdditionalButtons(View dialogView) {
        for(Map.Entry<Integer, DialogBtnClickListener> entry : additionalButtons.entrySet()) {
            setupSingleBtn(dialogView, entry.getKey(), entry.getValue());
        }
    }

    private void setupSingleBtn(View dialogView, int btnId, DialogBtnClickListener listener) {
        if(btnId != -1) {
            Button btn = dialogView.findViewById(btnId);

            if(btn != null && listener != null) {
                btn.setOnClickListener(view -> listener.onClick(dialogView));
            }
        }
    }

    public void dismiss() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error dismissing dialog: " + e.getMessage());
        }
    }

    public View getRootView() {
        return dialogView;
    }

    public View findViewById(int id) {
        if(dialogView != null) {
            return dialogView.findViewById(id);
        }
        return null;
    }
}
