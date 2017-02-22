package com.addictedartgallery.widgets.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.addictedartgallery.R;

public class TrelloThankYou {

    public static void show(Context context)
    {
        final Dialog thankYouDialog = new Dialog(context);
        thankYouDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        thankYouDialog.setCancelable(false);
        thankYouDialog.setContentView(R.layout.dialog_trello_thank_you);

        Button okButton = (Button)thankYouDialog.findViewById(R.id.dialog_trello_thank_you_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thankYouDialog.cancel();
            }
        });

        thankYouDialog.show();
    }
}
