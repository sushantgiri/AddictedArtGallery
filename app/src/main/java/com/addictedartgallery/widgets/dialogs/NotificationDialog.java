package com.addictedartgallery.widgets.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.addictedartgallery.Dashboard;
import com.addictedartgallery.R;

public class NotificationDialog {

    public static void show(final Activity activity, String response)
    {
        final Dialog messageDialog= new Dialog(activity);
        messageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        messageDialog.setCancelable(false);
        messageDialog.setContentView(R.layout.dialog_notification);

        TextView messageText = (TextView)messageDialog.findViewById(R.id.dialog_notification_text);
        messageText.setText(response);

        Button okButton  = (Button)messageDialog.findViewById(R.id.dialog_notification_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   messageDialog.cancel();
                 ((Dashboard) (activity)).reApiCall();
            }
        });

        messageDialog.show();


    }
}
