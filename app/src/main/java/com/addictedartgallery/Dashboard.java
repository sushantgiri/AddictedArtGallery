package com.addictedartgallery;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;


import com.addictedartgallery.activities.Guide;
import com.addictedartgallery.activities.Login;
import com.addictedartgallery.model.Profile;
import com.addictedartgallery.notifications.Container;
import com.addictedartgallery.notifications.SNSPush;
import com.addictedartgallery.rest.ApiClient;
import com.addictedartgallery.rest.ApiInterface;
import com.addictedartgallery.rest.TrelloClient;
import com.addictedartgallery.rest.TrelloInterface;
import com.addictedartgallery.utils.Preferences;
import com.addictedartgallery.utils.ViewUtils;
import com.addictedartgallery.widgets.dialogs.ArtWork;
import com.addictedartgallery.widgets.dialogs.Feedback;
import com.addictedartgallery.widgets.dialogs.LogOut;
import com.addictedartgallery.widgets.dialogs.NetworkError;
import com.addictedartgallery.widgets.dialogs.NoArtWork;
import com.addictedartgallery.widgets.dialogs.NotificationDialog;
import com.addictedartgallery.widgets.tooltips.Tooltip;
import com.addictedartgallery.widgets.tooltips.TooltipAnimation;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARTexture2D;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends ARActivity implements AdapterView.OnItemClickListener, ARImageTrackableListener, View.OnClickListener {

    AVLoadingIndicatorView progressBar;
    private FloatingActionButton nextButton, previousButton;
    private FloatingActionButton shopIcon, infoIcon, hamIcon;
    private Button betaButton;
    private ViewGroup root;
    private ImageView logo;

    Call<Profile> profileCall;
    Profile profile;

    int i = 0;
    String destinationImageName;
    File destinationFile;

    int tooltipColor,tooltipSize,tipSizeSmall,tipSizeRegular,tipRadius;
    ListView listView;
    Tooltip tooltip;

    File downloadFolder;
    File downloadedImageFiles[] = null;

    boolean isPresent = false;

    private BroadcastReceiver broadcastReceiver;

    private SNSPush snsPush;

    private ArrayList<String> hamIconList,shopIconList,infoIconList;

    private ApiInterface apiService;
    private TrelloInterface trelloService;
    private Preferences preferences;

    String userName,realm,accessToken;

    private String authenticate;

    private ARImageTrackable trackable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("GAWQE-FDGML-DFTWK-KZMTC-Y47ZG-C757T-ZW3N9-XCWQC-CUA9P-6QAXK-F3P7G-E35ZB-SESS4-89S4M-NB29Z-A");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);

        tooltipProperties();

        progressBar = (AVLoadingIndicatorView) findViewById(R.id.activity_ar_progressBar);

        betaButton = (Button)findViewById(R.id.beta);
        betaButton.setOnClickListener(this);

        nextButton = (FloatingActionButton) findViewById(R.id.next);
        nextButton.setOnClickListener(this);

        previousButton = (FloatingActionButton) findViewById(R.id.previous);
        previousButton.setOnClickListener(this);

        shopIcon = (FloatingActionButton) findViewById(R.id.shopIcon);
        shopIcon.setOnClickListener(this);

        infoIcon = (FloatingActionButton) findViewById(R.id.infoIcon);
        infoIcon.setOnClickListener(this);

        hamIcon = (FloatingActionButton) findViewById(R.id.hamIcon);
        hamIcon.setOnClickListener(this);

        logo = (ImageView)findViewById(R.id.ar_screen_logo);
        logo.setOnClickListener(this);

        root = (ViewGroup) findViewById(R.id.mainLayout);

        hamIconList = new ArrayList<>();
        hamIconList.add(getString(R.string.AboutUs));
        hamIconList.add(getString(R.string.Tutorial));
        hamIconList.add(getString(R.string.LogOut));
        hamIconList.add(getString(R.string.ReportIssue));

        infoIconList = new ArrayList<>();
        infoIconList.add(getString(R.string.AboutThisArtwork));

        shopIconList = new ArrayList<>();
        shopIconList.add(getString(R.string.BuyArtWork));
        shopIconList.add(getString(R.string.ContactUs));

        if(isTablet())
        {
            shopIcon.setSize(FloatingActionButton.SIZE_NORMAL);
            infoIcon.setSize(FloatingActionButton.SIZE_NORMAL);
            hamIcon.setSize(FloatingActionButton.SIZE_NORMAL);
        }else{
            shopIcon.setSize(FloatingActionButton.SIZE_MINI);
            infoIcon.setSize(FloatingActionButton.SIZE_MINI);
            hamIcon.setSize(FloatingActionButton.SIZE_MINI);
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);
        trelloService = TrelloClient.getClient().create(TrelloInterface.class);
        preferences = new Preferences(this);

        userName = preferences.getUsername();
        realm = preferences.getRealm();
        accessToken = preferences.getAccessToken();

        if(userName != null && realm != null && accessToken != null)
        {
            String r1 = ViewUtils.getMd5Key(userName + ":" + realm + ":" + accessToken);
            String r2 = ViewUtils.getMd5Key("GET:https://devapi.addictedgallery.com/en/account/profile:");
            String auth = ViewUtils.getMd5Key(r1 + ":" + accessToken + ":" + r2);
            authenticate = userName + ":" + auth;

            if(ViewUtils.isOnline(this))
            {
                apiCall();
            }else {
                NetworkError.show(this,true);
                progressBar.setVisibility(View.GONE);
            }

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction().equals(ViewUtils.MESSAGE_RECEIVED)){
                        String message = intent.getStringExtra(ViewUtils.MESSAGE);
                        NotificationDialog.show(Dashboard.this,message);
                    }
                }
            };
        }else
        {
            preferences.clearData();
            startActivity(new Intent(this,Login.class));
        }


    }

    public void setup() {
        addImageTrackable();
    }

    private void addImageTrackable() {
        trackable = new ARImageTrackable("marker");
        trackable.loadFromAsset("addictedmarker.jpg");
        trackable.addListener(this);
        ARImageTracker trackableManager = ARImageTracker.getInstance();
        trackableManager.addTrackable(trackable);
    }

    private void tooltipProperties() {
        Resources res = getResources();
        tooltipSize = res.getDimensionPixelOffset(R.dimen.tooltip_width);
        tooltipColor = ContextCompat.getColor(this, R.color.pink);
        tipSizeSmall = res.getDimensionPixelSize(R.dimen.tip_dimen_small);
        tipSizeRegular = res.getDimensionPixelSize(R.dimen.tip_dimen_regular);
        tipRadius = res.getDimensionPixelOffset(R.dimen.tip_radius);
    }

    @SuppressLint("InflateParams")
    private void showTooltip(@NonNull View anchor, @NonNull ArrayList<String> iconList,
                             @Tooltip.Position int position, boolean autoAdjust,
                             @TooltipAnimation.Type int type,
                             int width, int height) {

        listView = (ListView) getLayoutInflater().inflate(R.layout.tooltip_listview, null);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.tooltip_list_item,R.id.tv ,iconList);
        listView.setAdapter(adapter);
        listView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        listView.setOnItemClickListener(this);
        showTooltip(anchor, listView, position, autoAdjust, type, tooltipColor);


    }

    private void showTooltip(@NonNull View anchor, @NonNull View content,
                             @Tooltip.Position int position, boolean autoAdjust,
                             @TooltipAnimation.Type int type,
                             int tipColor) {

        if(tooltip !=null)
            tooltip.dismiss(true);

        tooltip = new Tooltip.Builder(this)
                .anchor(anchor, position)
                .animate(new TooltipAnimation(type, 500))
                .autoAdjust(autoAdjust)
                .content(content)
                .withTip(new Tooltip.Tip(tipSizeRegular, tipSizeRegular, tipColor))
                .into(root)
                .autoCancel(3000)
                .debug(true)
                .show();
    }

    private boolean isTablet()
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (metrics.widthPixels /metrics.density) >=600;
    }

    private void addImageNode(String imageName) {
        String destination = getExternalFilesDir(null) + File.separator + imageName;
        ARTexture2D texture = new ARTexture2D();
        texture.loadFromPath(destination);
        texture.setTextureID(i);
        ARImageNode imageNode = new ARImageNode(texture);
        String width = profile.getData().getArlist().get(i).getWidth();
        String height = profile.getData().getArlist().get(i).getHeight();
        float floatWidth = (Float.parseFloat(width));
        float floatHeight = (Float.parseFloat(height));


        //Generic Pixel calculation: 1mm = 3.779528 px

        //1p

//        double widthPixel = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, floatWidth,getResources().getDisplayMetrics()))*0.75;
//        double heightInPixel = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, floatHeight,getResources().getDisplayMetrics()))*0.75;

        double widthPixel = floatWidth*3.779528*0.75;
        double heightInPixel = floatHeight*3.779528*0.75;

        if (widthPixel > heightInPixel) {
            double scale = 1754 / (widthPixel * 3.78);
            scale = 1 / scale;
            scale = scale / 1.495728;
            imageNode.scaleByUniform((float) scale);
        } else if (heightInPixel > widthPixel) {
            double scale = 1754 / (heightInPixel * 3.78);
            scale = 1 / scale;
            scale = scale / 1.495728;
            imageNode.scaleByUniform((float) scale);
        } else {
            double scale = 1754 / (widthPixel * 3.78);
            scale = 1 / scale;
            scale = scale / 1.495728;
            imageNode.scaleByUniform((float) scale);
        }
        trackable.getWorld().addChild(imageNode);


    }

    public void apiCall()
    {
        profileCall = apiService.getUserProfile(authenticate);
        profileCall.enqueue(profileCallback);
    }

    public void reApiCall()
    {
        ViewUtils.cleanDirectory(this);
        i=0;
        if(trackable.getWorld().getChildren() != null)
            trackable.getWorld().getChildren().clear();
        progressBar.setVisibility(View.VISIBLE);
        apiCall();
    }

    private void requestImage() {
        final String imageUrl = profile.getData().getArlist().get(i).getImage();
        String my_new_str = imageUrl.replaceAll("large", "shadow");
        String finalImageUrl = my_new_str.replaceAll("jpg","png");
        final String imageName = profile.getData().getArlist().get(i).getName();

        destinationImageName = getExternalFilesDir(null) + File.separator + imageName + ".png";

        destinationFile = new File(destinationImageName);
        Ion.with(Dashboard.this)
                .load(finalImageUrl)
                .write(destinationFile)
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        progressBar.setVisibility(View.GONE);
                        if (file != null) {
                            addImageNode(file.getName());
                            hideAll();
                            if(trackable.getWorld() != null && trackable.getWorld().getChildren()!= null && trackable.getWorld().getChildren().size() != 0)
                                trackable.getWorld().getChildren().get(i).setVisible(true);
                        } else {
                            if (destinationFile.exists()) {
                                destinationFile.delete();
                            } else {
                                System.err.println(
                                        "I cannot find '" + destinationFile + "' ('" + destinationFile.getAbsolutePath() + "')");
                            }

                            NetworkError.show(Dashboard.this,false);
                            i--;
                        }

                    }
                });

    }

    private void hideAll() {
        if(trackable.getWorld() != null) {
            List<ARNode> nodes = trackable.getWorld().getChildren();

            if(nodes != null && nodes.size() !=0) {
                for (ARNode node : nodes) {
                    node.setVisible(false);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ViewUtils.MESSAGE_RECEIVED));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        String listItem = (String) listView.getItemAtPosition(position);

        if (tooltip != null)
            tooltip.dismiss(true);

        switch (listItem) {

            case "ABOUT US":
                ViewUtils.openBrowser(this,getString(R.string.About));
                break;

            case "TUTORIAL":
                Intent guideIntent = new Intent(this,Guide.class);
                guideIntent.putExtra(getString(R.string.Intent),getString(R.string.Intent));
                startActivity(guideIntent);
                break;

            case "LOG OUT":
                if(snsPush == null)
                    LogOut.show(this,preferences,null,null);
                else
                LogOut.show(this,preferences,snsPush.getAmazonSNSClient(),snsPush.getSubscriptionId());
                break;

            case "REPORT ISSUE":
                Feedback.show(this,userName,trelloService);
                break;



            case "ABOUT THIS ARTWORK":

                if(profile == null ||profile.getData().getArlist() == null || profile.getData().getArlist().size() == 0)
                {
                    NoArtWork.show(this);
                    return;
                }

                for (int j = 0; j < profile.getData().getArlist().size(); j++) {
                    if(trackable.getWorld() != null && trackable.getWorld().getChildren()!= null && trackable.getWorld().getChildren().size() != 0) {
                        if (trackable.getWorld().getChildren().get(j).getVisible()) {
                            ArtWork.show(Dashboard.this, profile.getData().getArlist().get(j));
                            break;
                        }
                    }
                }
                break;


            case "CHECK AVAILABILITY":

                if(profile == null || profile.getData().getArlist() == null || profile.getData().getArlist().size() == 0)
                {
                    NoArtWork.show(this);
                    return;
                }

                for (int j = 0; j < profile.getData().getArlist().size(); j++) {
                    if(trackable.getWorld() != null && trackable.getWorld().getChildren()!= null && trackable.getWorld().getChildren().size() != 0) {
                        if (trackable.getWorld().getChildren().get(j).getVisible()) {
                            ViewUtils.openBrowser(this, getString(R.string.BuyArt) + profile.getData().getArlist().get(j).getPage());
                            break;
                        }
                    }
                }

                break;

            case "CONTACT US":
                ViewUtils.openBrowser(this,getString(R.string.Contact));
                break;

        }

    }

    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nextButton.setVisibility(View.VISIBLE);
                previousButton.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {

    }

    @Override
    public void didLose(ARImageTrackable arImageTrackable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nextButton.setVisibility(View.GONE);
                previousButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == nextButton) {
            if (progressBar.getVisibility() == View.GONE) {

                if(profile == null ||profile.getData().getArlist() == null || profile.getData().getArlist().size() == 0)
                {
                    NoArtWork.show(this);
                    return;
                }

                i++;
                if (i < profile.getData().getArlist().size()) {

                    downloadFolder = ViewUtils.getDirectory(Dashboard.this);
                    if (downloadFolder != null) {
                        downloadedImageFiles = downloadFolder.listFiles();
                        for (File downloadedImageFile : downloadedImageFiles) {
                            String downloadedImageName = downloadedImageFile.getName();
                            String imageName = profile.getData().getArlist().get(i).getName() + ".png";
                            if (downloadedImageName.equals(imageName)) {
                                isPresent = true;
                                break;
                            }
                        }
                    }

                    if(isPresent) {
                        isPresent = false;
                        hideAll();
                        if(trackable.getWorld() != null && trackable.getWorld().getChildren()!= null && trackable.getWorld().getChildren().size() != 0)
                            trackable.getWorld().getChildren().get(i).setVisible(true);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        requestImage();
                    }


                } else {
                    i = 0;
                    hideAll();
                    if(trackable.getWorld() != null && trackable.getWorld().getChildren()!= null && trackable.getWorld().getChildren().size() != 0)
                        trackable.getWorld().getChildren().get(i).setVisible(true);
                }
            }

        } else if (view == previousButton) {
            if (progressBar.getVisibility() == View.GONE) {

                if(profile == null ||profile.getData().getArlist() == null || profile.getData().getArlist().size() == 0)
                {
                    NoArtWork.show(this);
                    return;
                }

                i--;
                if (i < 0) {
                    i = 0;
                    hideAll();
                    if(trackable.getWorld() != null && trackable.getWorld().getChildren()!= null && trackable.getWorld().getChildren().size() != 0)
                        trackable.getWorld().getChildren().get(i).setVisible(true);
                } else {
                    hideAll();
                    if(trackable.getWorld() != null && trackable.getWorld().getChildren()!= null && trackable.getWorld().getChildren().size() != 0)
                        trackable.getWorld().getChildren().get(i).setVisible(true);
                }

            }
        }  else if (view == shopIcon) {
            if (progressBar.getVisibility() == View.GONE) {
                showTooltip(view, shopIconList, Tooltip.TOP, true,
                        TooltipAnimation.REVEAL, tooltipSize,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        } else if (view == infoIcon) {
            if (progressBar.getVisibility() == View.GONE) {
                showTooltip(view, infoIconList, Tooltip.TOP, true,
                        TooltipAnimation.REVEAL, tooltipSize,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

            }


        } else if (view == hamIcon) {
            if (progressBar.getVisibility() == View.GONE) {
                showTooltip(view, hamIconList, Tooltip.TOP, true,
                        TooltipAnimation.REVEAL, tooltipSize,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }else if(view == betaButton)
        {
            if(progressBar.getVisibility() == View.GONE)
            {
                Feedback.show(this,userName,trelloService);
            }
        }else if(view == logo) {
            if (progressBar.getVisibility() == View.GONE)
                ViewUtils.openBrowser(this, getString(R.string.AddictedGallery));
        }

    }


    private Callback<Profile> profileCallback = new Callback<Profile>() {
        @Override
        public void onResponse(Call<Profile> call, Response<Profile> response) {
            if (response.isSuccessful()) {
                profile = response.body();

                if(preferences.getRegistrationToken() != null)
                {
                    snsPush = new SNSPush();
                    Container container = new Container();
                    container.profileData = profile.getData();
                    container.preferences = preferences;
                    snsPush.execute(container);
                }

                if (profile.getStatus()) {

                    if(profile.getData().getArlist() != null && profile.getData().getArlist().size() != 0) {

                        final String imageUrl = profile.getData().getArlist().get(i).getImage();
                        String my_new_str = imageUrl.replaceAll("large", "shadow");
                        String finalImageUrl = my_new_str.replaceAll("jpg", "png");
                        final String imageName = profile.getData().getArlist().get(i).getName();

                        destinationImageName = getExternalFilesDir(null) + File.separator + imageName + ".png";

                        destinationFile = new File(destinationImageName);
                        Ion.with(Dashboard.this)
                                .load(finalImageUrl)
                                .write(destinationFile)
                                .setCallback(new FutureCallback<File>() {
                                    @Override
                                    public void onCompleted(Exception e, File file) {
                                        progressBar.setVisibility(View.GONE);
                                        if (file != null)
                                            addImageNode(file.getName());
                                        else {
                                            NetworkError.show(Dashboard.this, true);
                                            if (destinationFile.exists()) {
                                                destinationFile.delete();
                                            } else {
                                                System.err.println(
                                                        "I cannot find '" + destinationFile + "' ('" + destinationFile.getAbsolutePath() + "')");
                                            }
                                        }

                                    }
                                });

                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        NoArtWork.show(Dashboard.this);
                    }


                }
                else {
                    progressBar.setVisibility(View.GONE);
                    preferences.clearData();
                    startActivity(new Intent(Dashboard.this, Login.class));
                    finish();
                }

            }else{
//                preferences.clearData();
//                startActivity(new Intent(Dashboard.this, Login.class));
//                finish();
                  progressBar.setVisibility(View.GONE);
                  NoArtWork.show(Dashboard.this);
            }
        }

        @Override
        public void onFailure(Call<Profile> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            NetworkError.show(Dashboard.this,true);
        }
    };
}
