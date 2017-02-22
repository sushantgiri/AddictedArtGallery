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
public class FacebookLoginError {

    public static void show(Context context)
    {
        final Dialog facebookDialog = new Dialog(context);
        facebookDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        facebookDialog.setCancelable(false);
        facebookDialog.setContentView(R.layout.dialog_facebook_login_error);

        TextView facebookBody = (TextView)facebookDialog.findViewById(R.id.dialog_facebook_error_text);
        facebookBody.setText(Html.fromHtml(context.getString(R.string.FacebookBody)));

        Button okButton = (Button)facebookDialog.findViewById(R.id.dialog_facebook_error_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookDialog.cancel();
            }
        });

        facebookDialog.show();

    }
}
