package com.demo.dietiestates25.support_classes.profile_helper;

import android.net.Uri;

public interface OnProfileChangesListener {
    void onProfileImageChanged(Uri imgUri);

    void onSaveChanges(int fieldId, String newTxt);
}
