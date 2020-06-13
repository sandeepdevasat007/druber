package com.xyz.druber.flight;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import dji.common.error.DJIError;
import dji.keysdk.FlightControllerKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.ActionCallback;

class DjiActionperform {

    public static void performAction(Context context) {
        FlightControllerKey var1 = FlightControllerKey.create(FlightControllerKey.START_GO_HOME);
        if (KeyManager.getInstance() != null) {
            KeyManager.getInstance().performAction(var1, new ActionCallback() {
                public void onSuccess() {

                    onReturnHomeActionResult((DJIError) null);
                }

                public void onFailure(@NonNull DJIError var1) {
                    onReturnHomeActionResult(var1);
                }
            });
        }
    }
    @MainThread
    @Keep
    private static void onReturnHomeActionResult(@Nullable DJIError var1) {

    }
}
