package com.addictedartgallery.notifications;

import com.addictedartgallery.utils.Preferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class AddictedInstanceIDService extends FirebaseInstanceIdService {

    Preferences preferences;

    @Override
    public void onTokenRefresh() {
        preferences = new Preferences(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        preferences.setRegistrationToken(refreshedToken);
        System.out.println("Registration Token:"+preferences.getRegistrationToken());

    }

}