package com.addictedartgallery.widgets.dialogs;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.addictedartgallery.R;
import com.addictedartgallery.utils.ViewUtils;

@SuppressWarnings("deprecation")
public class NoArtWork {


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void show(Context context)
    {
        final Dialog noArtWorkDialog = new Dialog(context);
        noArtWorkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noArtWorkDialog.setCancelable(false);
        noArtWorkDialog.setContentView(R.layout.dialog_no_artwork);

        TextView noArtWorkBody = (TextView)noArtWorkDialog.findViewById(R.id.dialog_no_artwork_text);
        ViewUtils.stripUnderlines(context.getString(R.string.NoArtworkBody),noArtWorkBody);
        noArtWorkBody.setMovementMethod(LinkMovementMethod.getInstance());

        Button cancelButton = (Button)noArtWorkDialog.findViewById(R.id.dialog_no_artwork_close);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noArtWorkDialog.cancel();
            }
        });

        noArtWorkDialog.show();
    }
}
