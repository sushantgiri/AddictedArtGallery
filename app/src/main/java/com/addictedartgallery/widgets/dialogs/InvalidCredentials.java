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
public class InvalidCredentials {

    public static void show(Context context)
    {

        final Dialog invalidCredentialsDialog = new Dialog(context);
        invalidCredentialsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        invalidCredentialsDialog.setCancelable(false);
        invalidCredentialsDialog.setContentView(R.layout.dialog_invalid_credentials);

        TextView invalidCredentialsHeader = (TextView)invalidCredentialsDialog.findViewById(R.id.dialog_invalid_credentials_header);
        invalidCredentialsHeader.setText(Html.fromHtml(context.getString(R.string.InvalidCredentialsHeader)));

        TextView invalidCredentialsText = (TextView)invalidCredentialsDialog.findViewById(R.id.dialog_invalid_credentials_text);
        invalidCredentialsText.setText(Html.fromHtml(context.getString(R.string.InvalidCredentialsBody)));

        Button okButton = (Button)invalidCredentialsDialog.findViewById(R.id.dialog_invalid_credentials_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidCredentialsDialog.dismiss();
            }
        });

        invalidCredentialsDialog.show();

    }
}
