package com.xyz.druber.flight.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.widget.ProgressBar;

import com.dji.ux.druber.R;

/**
 Created by SANDEEP
 **/


class DialogUtils {

    private Dialog progressDialog = null;
    private Context mContext;

    public void showPopupProgressSpinner(Boolean isShowing) {
        if (isShowing == true) {
            progressDialog = new Dialog(mContext);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.popup_progressbar);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            ProgressBar progressBar = (ProgressBar) progressDialog
                    .findViewById(R.id.progressBar2);
            progressBar.getIndeterminateDrawable().setColorFilter(
                    Color.parseColor("#ff6700"),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            progressDialog.show();
        } else if (isShowing == false) {
            progressDialog.dismiss();
        }
    }

        public static ProgressDialog showProgressDialog(Context context, String message){
            ProgressDialog m_Dialog = new ProgressDialog(context);
            m_Dialog.setMessage(message);
            m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_Dialog.setCancelable(false);
            m_Dialog.show();
            return m_Dialog;


    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}
