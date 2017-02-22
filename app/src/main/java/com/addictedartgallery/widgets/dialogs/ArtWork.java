package com.addictedartgallery.widgets.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.addictedartgallery.R;
import com.addictedartgallery.model.ArList;
import com.addictedartgallery.utils.ViewUtils;

@SuppressWarnings("deprecation")
public class ArtWork {


    public static void show(final Context context, final ArList arList)
    {
        final Dialog artWorkDialog = new Dialog(context);
        artWorkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        artWorkDialog.setCancelable(true);
        artWorkDialog.setContentView(R.layout.dialog_artwork);

        TextView imageName = (TextView)artWorkDialog.findViewById(R.id.artwork_dialog_image_name);
        imageName.setText(arList.getName());

        TextView artistName = (TextView)artWorkDialog.findViewById(R.id.artwork_dialog_artist_name);
        artistName.setText(arList.getArtistName());

        TextView artistOtherDetails = (TextView)artWorkDialog.findViewById(R.id.artwork_dialog_artist_details);
        artistOtherDetails.setText(Html.fromHtml(arList.getHtml()));

        ImageView closeButton = (ImageView)artWorkDialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artWorkDialog.cancel();
            }
        });

        Button viewOnlineButton = (Button) artWorkDialog.findViewById(R.id.artwork_dialog_view_online);
        viewOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.openBrowser(context,context.getString(R.string.BuyArt)+arList.getPage());
            }
        });


        artWorkDialog.show();


    }
}
