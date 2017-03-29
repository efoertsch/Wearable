package com.fisincorporated.wearable.network;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fisincorporated.wearable.R;

//
public class NetworkSetupActivity extends Activity implements NetworkManager.NetworkStateCallBack {

    private static final String LOG_TAG = NetworkSetupActivity.class.getSimpleName();

    // Intent action for sending the user directly to the add Wi-Fi network activity.
    private static final String ACTION_ADD_NETWORK_SETTINGS =
            "com.google.android.clockwork.settings.connectivity.wifi.ADD_NETWORK_SETTINGS";

    private ImageView connectivityIcon;
    private TextView connectivityText;

    private View button;
    private ImageView buttonIcon;
    private TextView buttonText;
    private TextView infoText;
    private View progressBar;

    // Tags added to the button in the UI to detect what operation the user has requested.
    // These are required since the app reuses the button for different states of the app/UI.
    // See onButtonClick() for how these tags are used.
    static final String TAG_REQUEST_NETWORK = "REQUEST_NETWORK";
    static final String TAG_RELEASE_NETWORK = "RELEASE_NETWORK";
    static final String TAG_ADD_WIFI = "ADD_WIFI";

    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        connectivityIcon = (ImageView) findViewById(R.id.connectivity_icon);
        connectivityText = (TextView) findViewById(R.id.connectivity_text);

        progressBar = findViewById(R.id.progress_bar);

        button = findViewById(R.id.button);
        button.setTag(TAG_REQUEST_NETWORK);
        buttonIcon = (ImageView) findViewById(R.id.button_icon);
        buttonText = (TextView) findViewById(R.id.button_label);

        infoText = (TextView) findViewById(R.id.info_text);

        networkManager = NetworkManager.get();
        networkManager.addNetworkStatusCallback(this);

    }

    @Override
    public void onStop() {
        // for now keep network
        //releaseHighBandwidthNetwork();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        networkManager.addNetworkStatusCallback(this);
        currentNetworkState(networkManager.getCurrentNetworkState());
    }

    @Override
    public void onPause(){
        super.onPause();
        networkManager.removeNetworkStatusCallback(this);
    }

    private void addWifiNetwork() {
        // requires android.permission.CHANGE_WIFI_STATE
        startActivity(new Intent(ACTION_ADD_NETWORK_SETTINGS));
    }

    /**
     * Click handler for the button in the UI. The view tag is used to determine the specific
     * function of the button.
     *
     * @param view The view that was clicked
     */
    public void onButtonClick(View view) {
        switch (view.getTag().toString()) {
            case TAG_REQUEST_NETWORK:
                networkManager.requestHighBandwidthNetwork();
                break;
            case TAG_RELEASE_NETWORK:
                networkManager.releaseHighBandwidthNetwork();
                break;
            case TAG_ADD_WIFI:
                addWifiNetwork();
                break;
        }
    }

    // Sets the text and icons the connectivity indicator, button, and info text in the app UI,
    // which are all reused for the various states of the app and network connectivity. Also,
    // will show/hide a progress bar, which is dependent on the state of the network connectivity
    // request.
    public void currentNetworkState(final int uiState) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (uiState) {
                    case NetworkManager.UI_STATE_REQUEST_NETWORK:
                        if (networkManager.isNetworkHighBandwidth()) {
                            connectivityIcon.setImageResource(R.drawable.ic_cloud_happy);
                            connectivityText.setText(R.string.network_fast);
                        } else {
                            connectivityIcon.setImageResource(R.drawable.ic_cloud_sad);
                            connectivityText.setText(R.string.network_slow);
                        }

                        button.setTag(TAG_REQUEST_NETWORK);
                        buttonIcon.setImageResource(R.drawable.ic_fast_network);
                        buttonText.setText(R.string.button_request_network);
                        infoText.setText(R.string.info_request_network);
                        break;

                    case NetworkManager.UI_STATE_REQUESTING_NETWORK:
                        connectivityIcon.setImageResource(R.drawable.ic_cloud_disconnected);
                        connectivityText.setText(R.string.network_connecting);

                        progressBar.setVisibility(View.VISIBLE);
                        infoText.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        break;

                    case NetworkManager.UI_STATE_NETWORK_CONNECTED:
                        if (networkManager.isNetworkHighBandwidth()) {
                            connectivityIcon.setImageResource(R.drawable.ic_cloud_happy);
                            connectivityText.setText(R.string.network_fast);
                        } else {
                            connectivityIcon.setImageResource(R.drawable.ic_cloud_sad);
                            connectivityText.setText(R.string.network_slow);
                        }

                        progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);

                        button.setTag(TAG_RELEASE_NETWORK);
                        buttonIcon.setImageResource(R.drawable.ic_no_network);
                        buttonText.setText(R.string.button_release_network);
                        infoText.setText(R.string.info_release_network);
                        break;

                    case NetworkManager.UI_STATE_CONNECTION_TIMEOUT:
                        connectivityIcon.setImageResource(R.drawable.ic_cloud_disconnected);
                        connectivityText.setText(R.string.network_disconnected);

                        progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);

                        button.setTag(TAG_ADD_WIFI);
                        buttonIcon.setImageResource(R.drawable.ic_wifi_network);
                        buttonText.setText(R.string.button_add_wifi);
                        infoText.setText(R.string.info_add_wifi);
                        break;
                }
            }
        });
    }
}