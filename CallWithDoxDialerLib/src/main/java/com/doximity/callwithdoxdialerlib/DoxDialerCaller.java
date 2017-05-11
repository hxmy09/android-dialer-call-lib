package com.doximity.callwithdoxdialerlib;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

/**
 * Created by yinanoliver on 5/11/17.
 */

public class DoxDialerCaller {

    private static final String DOX_DIALER_PACKAGE_NAME = "com.doximity.doxdialer";
    private static DoxDialerCaller mInstance;

    private DoxDialerCaller() {}

    /**
     * @return The DoxDialerCaller instance.
     */
    public static DoxDialerCaller shared() {
        if (mInstance == null) {
            mInstance = new DoxDialerCaller();
        }
        return mInstance;
    }

    /**
     * @param phoneNumber The phone number to dial, as a String.
     *                    It may be given in most reasonable formats, e.g.:
     *                    using numbers only: 6502333444
     *                    formatted: (650)233-3444
     *                    with a leading international area code: +1(650)233-3444
     * @param context     The Context parameter, it's used to start intent to launch the Doximity Dialer app.
     * @return true if Doximity Dialer app is successfully launched or Play Store link is successfully launched, otherwise false.
     */

    public boolean dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(DOX_DIALER_PACKAGE_NAME);
        if (intent == null) {
            // Doximity Dialer app is not installed, redirect to Play Store
            try {
                //Play Store is installed
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + DOX_DIALER_PACKAGE_NAME));
                context.startActivity(intent);
                return true;
            } catch (android.content.ActivityNotFoundException e) {
                //Play Store is not installed on user's device, open as a web link
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + DOX_DIALER_PACKAGE_NAME));
                context.startActivity(intent);
                return true;
            }
        } else {
            // Doximity Dialer app is installed
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }

    /**
     * @param context The Context parameter, it's used to get the drawable resource.
     * @return The Doximity dialer icon drawable.
     */
    public Drawable getDialerIcon(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(R.drawable.doximity_dialer_icon);
        } else {
            return context.getResources().getDrawable(R.drawable.doximity_dialer_icon);
        }
    }
}