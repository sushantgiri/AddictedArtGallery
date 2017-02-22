package com.addictedartgallery.widgets.dialogs;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.addictedartgallery.Dashboard;
import com.addictedartgallery.R;
import com.addictedartgallery.utils.ViewUtils;

@SuppressWarnings("deprecation")
public class NetworkError {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void show(final Activity activity,final boolean doReApiCall)
    {
        final Dialog networkErrorDialog = new Dialog(activity);
        networkErrorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        networkErrorDialog.setCancelable(false);
        networkErrorDialog.setContentView(R.layout.dialog_network_error);

        TextView networkErrorBody = (TextView)networkErrorDialog.findViewById(R.id.dialog_network_error_text);
        networkErrorBody.setText(Html.fromHtml(activity.getString(R.string.NetworkErrorBody)));

        Button retryButton = (Button)networkErrorDialog.findViewById(R.id.dialog_network_error_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ViewUtils.isOnline(activity)) {
                    networkErrorDialog.cancel();
                    if(doReApiCall)
                    ((Dashboard) (activity)).reApiCall();
                }
            }
        });

        networkErrorDialog.show();
    }
}
