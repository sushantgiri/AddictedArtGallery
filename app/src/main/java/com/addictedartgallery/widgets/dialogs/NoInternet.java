package com.addictedartgallery.widgets.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.addictedartgallery.R;

@SuppressWarnings("deprecation")
public class NoInternet {

    public static void show(Context context)
    {
        final Dialog noInternetDialog = new Dialog(context);
        noInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noInternetDialog.setCancelable(false);
        noInternetDialog.setContentView(R.layout.dialog_no_internet);

        TextView noInternetBody = (TextView)noInternetDialog.findViewById(R.id.dialog_no_internet_text);
        noInternetBody.setText(Html.fromHtml(context.getString(R.string.NoInternetBody)));

        Button okButton = (Button)noInternetDialog.findViewById(R.id.dialog_no_internet_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternetDialog.cancel();
            }
        });

        noInternetDialog.show();
    }
}
