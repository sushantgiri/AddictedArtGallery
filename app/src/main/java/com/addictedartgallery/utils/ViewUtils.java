package com.addictedartgallery.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class ViewUtils {

    //Constants For Push Notifications
    public static final String MESSAGE_RECEIVED = "messageReceived";
    public static final String MESSAGE = "message";



    //Check if internet connection is available

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }


    //Cleans the directory before adding images

    @SuppressWarnings("ConstantConditions")
    public static File getDirectory(Context context)
    {
        File internalFile = new File(context.getExternalFilesDir(null).getPath());
        if (!internalFile.exists() || internalFile.listFiles().length == 0) {
            return null;

        }
        return internalFile;
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    public static void cleanDirectory(Context context) {
        File internalFile = new File(context.getExternalFilesDir(null).getPath());
        deleteDirectory(internalFile);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ForLoopReplaceableByForEach"})
    private static void deleteDirectory(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }else{
                file.delete();
            }

        }
    }


    //Get MD5 Key from String

    public static String getMd5Key(String password) {
        StringBuffer hexString;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            hexString = new StringBuffer();
            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            System.out.println("Error in MD5 hashing: " + e);
            return null;
        }

    }


    //Opens Browser

    public static void openBrowser(Context context, String url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    //Remove UnderLine in Links

    public static void stripUnderlines(String content,TextView textView) {
        Spannable s = (Spannable) Html.fromHtml(content);
        for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }
        textView.setText(s);
    }

     //Validate Email and Password

    public static boolean validateUser(EditText emailText, EditText passwordText) {
        boolean isValid = true;
        String email = emailText.getText().toString().trim();
        if (TextUtils.isEmpty(email))
            isValid = false;

        String password = passwordText.getText().toString().trim();
        if (TextUtils.isEmpty(password))
            isValid = false;

        return isValid;

    }


    //Get Device Information

    public static String getInfo(Activity a)
    {
        String s = "";


        s += "\n Device: " + Build.MODEL + " ("
                + Build.PRODUCT + ")";
        s += "\n Brand: " + Build.MANUFACTURER;

        s += "\n Screen Size: "
                + a.getWindow().getWindowManager().getDefaultDisplay()
                .getWidth()+"*" + a.getWindow().getWindowManager().getDefaultDisplay()
                .getHeight() + " (" +setRealDeviceSizeInPixels(a) +")";



        s += "\n OS: " + getOS();

        s += "\n Platform: Android";
        try {
            PackageInfo pInfo = a.getPackageManager().getPackageInfo(
                    a.getPackageName(), PackageManager.GET_META_DATA);
            s += "\nApplication Version: " + pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }

        return s;

    }

    //Get OS

    private static String getOS()
    {
        StringBuilder builder = new StringBuilder();

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue;

            try {
                fieldValue = field.getInt(new Object());
                if (fieldValue == Build.VERSION.SDK_INT) {
                    builder.append(fieldName).append(" (").append(Build.VERSION.RELEASE).append(")");

                }
            } catch (Exception e) {
                return null;

            }


        }

        return builder.toString();
    }

    private static String setRealDeviceSizeInPixels(Activity context) {
        int mWidthPixels;
        int mHeightPixels;

        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);


        // since SDK_INT = 1;
        mWidthPixels = displayMetrics.widthPixels;
        mHeightPixels = displayMetrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(mWidthPixels/dm.xdpi,2);
        double y = Math.pow(mHeightPixels/dm.ydpi,2);
        double screenInches= Math.sqrt(x+y);
        return String.format(Locale.US,"%.2f", screenInches);
    }

    //Check if application is in background

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }




}
