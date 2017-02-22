package com.addictedartgallery.widgets.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.addictedartgallery.R;


public class FontButton extends Button {

    String typeface;

    public FontButton(Context context) {
        this(context,null);
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs,context);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context)
    {
        Typeface lightFont = FontCache.getTypeface(typeface,context);
        setTypeface(lightFont);
    }

    private void getAttributes(AttributeSet attributeSet,Context context)
    {
        final TypedArray typedArray =context.obtainStyledAttributes(attributeSet, R.styleable.FontButton,0,0);
        typeface = typedArray.getString(R.styleable.FontButton_setTypeface);
        typedArray.recycle();
    }
}
