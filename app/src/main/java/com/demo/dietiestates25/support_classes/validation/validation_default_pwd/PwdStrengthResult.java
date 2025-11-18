package com.demo.dietiestates25.support_classes.validation.validation_default_pwd;

public class PwdStrengthResult {
    public final int progress;
    public final int colorRes;
    public final int txtRes;

    public PwdStrengthResult(int progress, int colorRes, int txtRes) {
        this.progress = progress;
        this.colorRes = colorRes;
        this.txtRes = txtRes;
    }
}
