package com.addictedartgallery.activities;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.addictedartgallery.R;
import com.addictedartgallery.utils.ViewUtils;


@SuppressWarnings("deprecation")
public class Guide extends AppCompatActivity implements View.OnClickListener {

    TextView firstLineText,secondLineText,thirdLineText,forthLineText,fifthLineText,sixthLineText,notesText;
    Button signInButton;
    ImageView logo,closeButton;
    String intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_guide);

        intent = getIntent().getStringExtra(getString(R.string.Intent));

        firstLineText = (TextView)findViewById(R.id.firstLineText);
        firstLineText.setText(Html.fromHtml(getString(R.string.StepOne),new ImageGetter(),null));

        secondLineText = (TextView) findViewById(R.id.secondLineText);
        ViewUtils.stripUnderlines(getString(R.string.StepTwo),secondLineText);
        secondLineText.setMovementMethod(LinkMovementMethod.getInstance());

        thirdLineText = (TextView) findViewById(R.id.thirdLineText);
        thirdLineText.setText(Html.fromHtml(getString(R.string.StepThree)));

        forthLineText = (TextView)findViewById(R.id.forthLineText);
        forthLineText.setText(Html.fromHtml(getString(R.string.StepFour)));

        fifthLineText = (TextView)findViewById(R.id.fifthLineText);
        fifthLineText.setText(Html.fromHtml(getString(R.string.StepFive)));

        sixthLineText = (TextView)findViewById(R.id.sixthLineText);
        sixthLineText.setText(Html.fromHtml(getString(R.string.StepSix)));

        notesText = (TextView)findViewById(R.id.notes);
        notesText.setText(Html.fromHtml(getString(R.string.Notes)));

        closeButton = (ImageView)findViewById(R.id.guide_close);
        closeButton.setOnClickListener(this);

        signInButton = (Button) findViewById(R.id.OK);
        if(intent == null) {
            signInButton.setOnClickListener(this);
            closeButton.setVisibility(View.GONE);
        }
        else {
            signInButton.setVisibility(View.GONE);
            closeButton.setVisibility(View.VISIBLE);
        }


        logo = (ImageView)findViewById(R.id.imageView);
        logo.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view == signInButton)
        {
            startActivity(new Intent(this,Login.class));
            finish();
        }
        else if(view == logo)
            ViewUtils.openBrowser(this,getString(R.string.LogoMarker));

        else if(view == closeButton)
            finish();


    }


    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;

            if (source.equals("ic_visibility_black_24dp"))
                id = R.mipmap.ic_visibility_black_24dp;
            else
                return null;


            Drawable d = getResources().getDrawable(id);
            d.setBounds(0,0,firstLineText.getLineHeight(),firstLineText.getLineHeight());

            return d;
        }
    }
}
