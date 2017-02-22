package com.addictedartgallery.widgets.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.addictedartgallery.R;

public class TrelloNoIssue {

    public static void show(Context context)
    {
        final Dialog noIssueDialog = new Dialog(context);
        noIssueDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noIssueDialog.setCancelable(false);
        noIssueDialog.setContentView(R.layout.dialog_trello_no_issue);

        Button okButton = (Button)noIssueDialog.findViewById(R.id.dialog_trello_no_issue_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noIssueDialog.cancel();
            }
        });

        noIssueDialog.show();
    }


}
