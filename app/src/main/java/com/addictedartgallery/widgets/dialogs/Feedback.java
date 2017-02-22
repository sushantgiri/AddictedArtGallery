package com.addictedartgallery.widgets.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.addictedartgallery.R;
import com.addictedartgallery.rest.TrelloInterface;
import com.addictedartgallery.utils.ViewUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("deprecation")
public class Feedback {

    public static void show(final Activity activity, final String userName, final TrelloInterface trelloService) {
        final Dialog feedbackDialog = new Dialog(activity);
        feedbackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feedbackDialog.setCancelable(false);
        feedbackDialog.setContentView(R.layout.dialog_feedback);

        TextView feedbackBody = (TextView) feedbackDialog.findViewById(R.id.dialog_report_text);
        feedbackBody.setText(Html.fromHtml(activity.getString(R.string.FeedbackBody)));

        final EditText feedbackText = (EditText) feedbackDialog.findViewById(R.id.report_issue_text);
        final Button submitButton = (Button) feedbackDialog.findViewById(R.id.submit_report_issue);

        ImageView feedbackClose = (ImageView)feedbackDialog.findViewById(R.id.issueButtonClose);
        feedbackClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackDialog.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = feedbackText.getText().toString();
                if (description.isEmpty()) {
                    feedbackDialog.dismiss();
                    TrelloNoIssue.show(activity);
                    return;
                }
                submitButton.setEnabled(false);
                submitButton.setTextColor(ContextCompat.getColor(activity, R.color.pink));
                Call<ResponseBody> call = trelloService.reportIssues("Android Issue", "Description: " + description.trim() + "\n" + "[" + userName + "]" + "\n" + ViewUtils.getInfo(activity), null, "5811e48c6672408160b596f9");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        submitButton.setEnabled(true);
                        submitButton.setTextColor(ContextCompat.getColor(activity, R.color.white));
                        if (response.isSuccessful()) {
                            feedbackDialog.dismiss();
                            TrelloThankYou.show(activity);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        submitButton.setEnabled(true);
                        submitButton.setTextColor(ContextCompat.getColor(activity, R.color.white));
                        feedbackDialog.dismiss();
                        NoInternet.show(activity);
                    }
                });


            }
        });

        feedbackDialog.show();


    }
}
