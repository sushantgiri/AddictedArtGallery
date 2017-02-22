package com.addictedartgallery.utils;


import android.content.Context;

import com.addictedartgallery.widgets.progressbar.CustomProgressBar;

public class CustomDialog {

    private Context context;
    private CustomProgressBar customProgressBar;

    public CustomDialog(Context context)
    {
        this.context = context;
    }


   public void show()
   {
       customProgressBar= CustomProgressBar.create(context)
               .setStyle(CustomProgressBar.Style.SPIN_INDETERMINATE)
               .setCancellable(false)
               .setAnimationSpeed(2)
               .setDimAmount(0.5f);
       customProgressBar.show();

   }

    public void dismiss()
    {
        if(customProgressBar !=null && customProgressBar.isShowing())
            customProgressBar.dismiss();
    }


}
