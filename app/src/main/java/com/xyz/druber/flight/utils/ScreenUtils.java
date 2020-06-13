package com.xyz.druber.flight.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dji.ux.druber.R;
import com.iigo.library.CubeLoadingView;
import com.irozon.sneaker.Sneaker;
import com.xyz.druber.flight.JobsActivity;

/**
 * Created by SANDEEP
 **/


public class ScreenUtils {
    private static ProgressDialog progressDialog;
    private static AlertDialog alertDialog;
    Context mContext;
    private static Dialog sDialog;

    public static void getSneaker(Context context, String errMsg) {
        Sneaker.with((Activity) context)
                .setMessage(errMsg, Color.WHITE)
                .setDuration(2000)
                .autoHide(true)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .sneakError();
    }

    public static void getSnackbar(CoordinatorLayout coordinatorLayout, String msg) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        View snackbar_view = snackbar.getView();
        final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbar_view.getLayoutParams();
        params.setMargins(params.leftMargin + 10,
                params.topMargin,
                params.rightMargin + 10,
                params.bottomMargin + 50);

        snackbar_view.setLayoutParams(params);
        snackbar_view.setBackgroundColor(Color.parseColor("#000000"));
        TextView snackbar_text = (TextView) snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbar_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            snackbar_text.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();
    }

    public static ProgressDialog createProgressDialoge(Context context, String title) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(title);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void dissmisProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static void inprogressDialog(final Context mContext, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((JobsActivity) mContext).getTabLayout().getTabAt(1).select();
                ((JobsActivity) mContext).getViewPager().setCurrentItem(1);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public static void showProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    public static void showToast(Context context, String res) {
        Toast toast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void createCubeProgressDialog(Context context) {
        sDialog = new Dialog(context);
        sDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sDialog.setContentView(R.layout.cube_loading_layout);
        sDialog.setCancelable(false);
        CubeLoadingView cubeLoadingView = sDialog.findViewById(R.id.cload);
        cubeLoadingView.start();

    }




    public static void showAlertDialog(Context context, String msg, String title) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public static void showCubeDialogProgress() {
        if (sDialog != null)
            sDialog.show();
    }

    public static void dismissCubeDialogProgress() {
        if (sDialog != null)
            sDialog.dismiss();
    }


    public static String getSingleArrayJson(String coords) {
        coords.replace("^\\[|\\]$", "");

        return coords.toString();
    }

    public static String getDoubleArrayJson(String coords) {
        coords.replace("\\[|\\]", " ");
//        coords.replaceAll("^\\[|\\]$", "").replaceAll("^\\[|\\]$", "");
        Log.d("KLKKL", "onClick: " + coords.replace("\\[|\\]", " "));

        return coords.toString();
    }


}