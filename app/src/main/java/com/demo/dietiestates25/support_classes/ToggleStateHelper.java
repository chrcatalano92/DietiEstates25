package com.demo.dietiestates25.support_classes;

import com.demo.dietiestates25.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class ToggleStateHelper {
    private ToggleStateHelper(){}

    public static void toggleState(MaterialButtonToggleGroup toggleGroup, MaterialButton btnYes, MaterialButton btnNo) {
        toggleGroup.addOnButtonCheckedListener(((group, checkedId, isChecked) -> {
            if(checkedId == R.id.btnYes) {
                btnNo.setChecked(false);
            }
            else if (checkedId == R.id.btnNo) {
                btnYes.setChecked(false);
            }
        }));
    }

    public static boolean selectedClicked(MaterialButton btnYes) {
        return btnYes.isChecked();
    }
}
