package com.addictedartgallery.widgets.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.addictedartgallery.R;


public class FontEditText extends EditText {

    String typeface;

    public FontEditText(Context context) {
       this(context,null);
    }

    public FontEditText(Context context, AttributeSet attrs) {
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
        final TypedArray typedArray =context.obtainStyledAttributes(attributeSet, R.styleable.FontEditText,0,0);
        typeface = typedArray.getString(R.styleable.FontEditText_typeface);
        typedArray.recycle();
    }
}
