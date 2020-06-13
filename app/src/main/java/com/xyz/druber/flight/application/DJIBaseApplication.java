package com.xyz.druber.flight.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;
import dji.sdk.sdkmanager.BluetoothProductConnector;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Main application
 * Created by SANDEEP
 */

public class DJIBaseApplication extends MultiDexApplication {

    public static final String TAG = DJIBaseApplication.class.getName();

    private static BaseProduct product;
    private static BluetoothProductConnector bluetoothConnector = null;
    private static Bus bus = new Bus(ThreadEnforcer.ANY);
    private static Application app = null;
    private static DJIBaseApplication instance;
    private static String firmwareVersion;
    private static Boolean isNewFirmwareVersion;
    public static final String FLAG_CONNECTION_CHANGE = "dji_sdk_connection_change";
    private static FlightController flightController;

    /**
     * Gets instance of the specific product connected after the
     * API KEY is successfully validated. Please make sure the
     * API_KEY has been added in the Manifest
     */
    private static synchronized BaseProduct getProductInstance() {
        if (null == product) {
            product = DJISDKManager.getInstance().getProduct();
        }
        return product;
    }


    public static synchronized BluetoothProductConnector getBluetoothProductConnector() {
        if (null == bluetoothConnector) {
            bluetoothConnector = DJISDKManager.getInstance().getBluetoothProductConnector();
        }
        return bluetoothConnector;
    }

    private static boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof Aircraft;
    }

    private static boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof HandHeld;
    }

    public static synchronized Aircraft getAircraftInstance() {
        if (!isAircraftConnected()) {
            return null;
        }
        return (Aircraft) getProductInstance();
    }
    public void setContext(Application application) {
        app = application;
    }


    public static synchronized HandHeld getHandHeldInstance() {
        if (!isHandHeldConnected()) {
            return null;
        }
        return (HandHeld) getProductInstance();
    }

    public static DJIBaseApplication getInstance() {
        return instance;
    }

    public static Bus getEventBus() {
        return bus;
    }

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        MultiDex.install(this);
        com.secneo.sdk.Helper.install(this);
        app = this;
        instance=this;
    }


    public static synchronized FlightController getFlightController() {
        if (flightController == null) {
            if (getProductInstance() instanceof Aircraft) {
                flightController = ((Aircraft) getProductInstance()).getFlightController();
            }
        }
        return flightController;
    }
    public void getFirmwareVersion() {
        if (getFlightController() != null) {
            getFlightController().getFirmwareVersion(new CommonCallbacks.CompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    firmwareVersion = s;
                    String[] strArray = firmwareVersion.split("\\.");
                    int[] intArray = new int[strArray.length];
                    for (int i = 0; i < strArray.length; i++) {
                        intArray[i] = Integer.parseInt(strArray[i]);
                    }
                    isNewFirmwareVersion = intArray[0] >= 3 && intArray[1] >= 2 && intArray[2] >= 10;
                }

                @Override
                public void onFailure(DJIError djiError) {

                }
            });
        }
    }
    public static synchronized Boolean isFirmwareNewVersion() {
        /**
         * Check for flight controller version 3.2.10
         */
        return isNewFirmwareVersion;
    }


    public static synchronized dji.sdk.camera.Camera getCameraInstance() {

        if (getProductInstance() == null) return null;

        dji.sdk.camera.Camera camera = null;

        if (getProductInstance() instanceof Aircraft){
            camera = ((Aircraft) getProductInstance()).getCamera();

        } else if (getProductInstance() instanceof HandHeld) {
            camera = ((HandHeld) getProductInstance()).getCamera();
        }

        return camera;
    }




}