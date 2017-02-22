package com.addictedartgallery.widgets.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.addictedartgallery.R;
import com.addictedartgallery.activities.Login;
import com.addictedartgallery.utils.CustomDialog;
import com.addictedartgallery.utils.Preferences;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.UnsubscribeRequest;

public class LogOut {

    public static void show(final Activity activity, final Preferences preferences, final AmazonSNSClient amazonSNSClient, final String subscriptionId)
    {
        final Dialog logOutDialog = new Dialog(activity);
        logOutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logOutDialog.setCancelable(true);
        logOutDialog.setContentView(R.layout.dialog_logout);

        final CustomDialog customDialog = new CustomDialog(activity);

        Button logOutButton = (Button)logOutDialog.findViewById(R.id.dialog_logout_logoutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(amazonSNSClient !=null && subscriptionId !=null) {
                    new AsyncTask<Void, Void, String>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            customDialog.show();
                        }

                        @Override
                        protected String doInBackground(Void... params) {
                            try {
                                  UnsubscribeRequest request = new UnsubscribeRequest(subscriptionId);
                                  amazonSNSClient.unsubscribe(request);
                            }catch (Exception e)
                            {
                                return e.getMessage();
                            }

                            return "success";
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            logOutDialog.cancel();
                            customDialog.dismiss();
                            System.out.println("Success--->"+result);

                            preferences.clearData();
                            activity.startActivity(new Intent(activity,Login.class));
                            activity.finish();

                        }
                    }.execute();
                }else{
                    logOutDialog.cancel();
                    preferences.clearData();
                    activity.startActivity(new Intent(activity,Login.class));
                    activity.finish();

                }



            }
        });

        Button backButton = (Button)logOutDialog.findViewById(R.id.dialog_logout_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutDialog.cancel();
            }
        });

        logOutDialog.show();

    }
}
