package com.addictedartgallery.notifications;


import android.os.AsyncTask;

import com.addictedartgallery.model.ProfileData;
import com.addictedartgallery.utils.Preferences;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("FieldCanBeLocal")
public class SNSPush extends AsyncTask<Container, Void, Void> {

    private AmazonSNSClient amazonSNSClient;
    private Preferences preferences;
    private ProfileData profileData;
    private String token;
    private BasicAWSCredentials credentials;
    private String subscriptionId;


    @Override
    protected Void doInBackground(Container... containers) {
        Container container = containers[0];
        profileData = container.profileData;
        preferences = container.preferences;
        token = container.preferences.getRegistrationToken();
        credentials = new BasicAWSCredentials(profileData.getSubscribeId(), profileData.getSubscribeSecret());
        amazonSNSClient = new AmazonSNSClient(credentials);
        amazonSNSClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        try {
            registerWithSNS(token);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public AmazonSNSClient getAmazonSNSClient()
    {
        return amazonSNSClient;
    }

    public String getSubscriptionId()
    {
        return subscriptionId;
    }



    private void registerWithSNS(String token) {
        String endpointArn = preferences.getEndpointArn();

        boolean updateNeeded = false;
        boolean createNeeded = (null == endpointArn);

        if (createNeeded) {
            endpointArn = createEndpoint(token);
            createNeeded = false;
        }
        System.out.println("Retrieving platform endpoint data...");

        try {
            GetEndpointAttributesRequest request =
                    new GetEndpointAttributesRequest()
                            .withEndpointArn(endpointArn);

            GetEndpointAttributesResult result =
                    amazonSNSClient.getEndpointAttributes(request);

            updateNeeded = !result.getAttributes().get("Token").equals(token)
                    || !result.getAttributes().get("Enabled").equalsIgnoreCase("true");

        } catch (NotFoundException exception) {
            createNeeded = true;
        }

        if (createNeeded)
            createEndpoint(token);

        System.out.println("updateNeeded = " + updateNeeded);

        if (updateNeeded) {
            System.out.println("Updating platform endpoint " + endpointArn);
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("Token", token);
            attributes.put("Enabled", "true");
            SetEndpointAttributesRequest endpointAttributesRequest =
                    new SetEndpointAttributesRequest()
                            .withEndpointArn(endpointArn)
                            .withAttributes(attributes);
            amazonSNSClient.setEndpointAttributes(endpointAttributesRequest);

        }

        String finalTopic = "arn:aws:sns:ap-southeast-1:725614431109:" + profileData.getSnstopic();
        System.out.println("Topic Arn:"+finalTopic);
        subscriptionId = amazonSNSClient.subscribe(new SubscribeRequest()
                .withEndpoint(endpointArn)
                .withProtocol("application")
                .withTopicArn(finalTopic)).getSubscriptionArn();
        System.out.println("Id" + subscriptionId);

        SubscribeRequest subscribeRequest = new SubscribeRequest(finalTopic, "application", endpointArn);
        SubscribeResult result = amazonSNSClient.subscribe(subscribeRequest);

        if (result != null)
            preferences.setAlreadySubscribed(true);
    }





    private String createEndpoint(String token) {
        String endpointArn;
        try {
            System.out.println("Creating platform endpoint with token " + token);
            CreatePlatformEndpointRequest request =
                    new CreatePlatformEndpointRequest()
                            .withPlatformApplicationArn("arn:aws:sns:ap-southeast-1:725614431109:app/GCM/Addicted_AndroidNotificationsDev")
                            .withToken(token);
            CreatePlatformEndpointResult result = amazonSNSClient
                    .createPlatformEndpoint(request);
            endpointArn = result.getEndpointArn();

        } catch (InvalidParameterException exception) {
            String message = exception.getMessage();
            System.out.println("Exception message: " + message);
            Pattern pattern = Pattern.compile(".*Endpoint (arn:aws:sns[^ ]+) already exists " +
                    "with the same token.*");
            Matcher matcher = pattern.matcher(message);

            if (matcher.matches())
                endpointArn = matcher.group(1);
            else
                throw exception;

        }

        preferences.setEndpointArn(endpointArn);
        return endpointArn;
    }


}
