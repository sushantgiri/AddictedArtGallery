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
public class NoCredentials {

    public static void show(Context context)
    {
        final Dialog validationDialog = new Dialog(context);
        validationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        validationDialog.setCancelable(false);
        validationDialog.setContentView(R.layout.dialog_no_credentials);

        TextView validationText = (TextView)validationDialog.findViewById(R.id.dialog_validation_text);
        validationText.setText(Html.fromHtml(context.getString(R.string.ValidationText)));

        Button okButton = (Button)validationDialog.findViewById(R.id.dialog_validation_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationDialog.dismiss();
            }
        });

        validationDialog.show();
    }
}
