package com.fisincorporated.wearable.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

// Code cribbed from Android
// https://github.com/googlesamples/android-WearHighBandwidthNetworking

public class NetworkManager {

    private static final String LOG_TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager networkManager;

    // These constants are used by setUiState() to determine what information to display in the UI,
    // as this app reuses UI components for the various states of the app, which is dependent on
    // the state of the network.
    public static final int UI_STATE_REQUEST_NETWORK = 1;
    public static final int UI_STATE_REQUESTING_NETWORK = 2;
    public static final int UI_STATE_NETWORK_CONNECTED = 3;
    public static final int UI_STATE_CONNECTION_TIMEOUT = 4;

    // Message to notify the network request timout handler that too much time has passed.
    private static final int MESSAGE_CONNECTIVITY_TIMEOUT = 1;

    // How long the app should wait trying to connect to a sufficient high-bandwidth network before
    // asking the user to add a new Wi-Fi network.
    private static final long NETWORK_CONNECTIVITY_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(10);

    // The minimum network bandwidth required by the app for high-bandwidth operations.
    private static final int MIN_NETWORK_BANDWIDTH_KBPS = 10000;

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    // Handler for dealing with network connection timeouts.
    private Handler handler;

    private CopyOnWriteArraySet<NetworkStateCallBack> networkStateCallbacks = new CopyOnWriteArraySet<>();

    public interface NetworkStateCallBack {
        void currentNetworkState(int status);
    }

    private NetworkManager() {
    }

    public void init(Context context) {
        if (networkManager == null) {
            networkManager = new NetworkManager();
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }

    public static NetworkManager get() {
        return networkManager;
    }

    public void addNetworkStatusCallback(NetworkStateCallBack networkStateCallBack) {
        networkStateCallbacks.add(networkStateCallBack);
    }

    public void removeNetworkStatusCallback(NetworkStateCallBack networkStateCallBack) {
        networkStateCallbacks.remove(networkStateCallBack);
    }

    public int getCurrentNetworkState() {
        if (isNetworkHighBandwidth()) {
            return UI_STATE_NETWORK_CONNECTED;
        } else {
             return UI_STATE_REQUEST_NETWORK;
        }
    }

    // Determine if there is a high-bandwidth network exists. Checks both the active
    // and bound networks. Returns false if no network is available (low or high-bandwidth).
    public boolean isNetworkHighBandwidth() {
        Network network = connectivityManager.getBoundNetworkForProcess();
        network = network == null ? connectivityManager.getActiveNetwork() : network;
        if (network == null) {
            return false;
        }

        // requires android.permission.ACCESS_NETWORK_STATE
        int bandwidth = connectivityManager
                .getNetworkCapabilities(network).getLinkDownstreamBandwidthKbps();

        if (bandwidth >= MIN_NETWORK_BANDWIDTH_KBPS) {
            return true;
        }
        return false;
    }

    public void requestHighBandwidthNetwork() {
        // Before requesting a high-bandwidth network, ensure prior requests are invalidated.
        unregisterNetworkCallback();

        Log.d(LOG_TAG, "Requesting high-bandwidth network");
        forwardNetworkState(UI_STATE_REQUESTING_NETWORK);

        // Requesting an unmetered network may prevent you from connecting to the cellular
        // network on the user's watch or phone; however, unless you explicitly ask for permission
        // to a access the user's cellular network, you should request an unmetered network.
        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(final Network network) {
                handler.removeMessages(MESSAGE_CONNECTIVITY_TIMEOUT);
                // requires android.permission.INTERNET
                if (!connectivityManager.bindProcessToNetwork(network)) {
                    Log.e(LOG_TAG, "ConnectivityManager.bindProcessToNetwork()"
                            + " requires android.permission.INTERNET");
                    forwardNetworkState(UI_STATE_REQUEST_NETWORK);
                } else {
                    Log.d(LOG_TAG, "Network available");
                    forwardNetworkState(UI_STATE_NETWORK_CONNECTED);
                }
            }


            @Override
            public void onCapabilitiesChanged(Network network,
                                              NetworkCapabilities networkCapabilities) {
                Log.d(LOG_TAG, "Network capabilities changed");
            }

            @Override
            public void onLost(Network network) {
                Log.d(LOG_TAG, "Network lost");
                forwardNetworkState(UI_STATE_REQUEST_NETWORK);
            }
        };

        // requires android.permission.CHANGE_NETWORK_STATE
        connectivityManager.requestNetwork(request, networkCallback);

        handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_CONNECTIVITY_TIMEOUT),
                NETWORK_CONNECTIVITY_TIMEOUT_MS);
    }

    private void forwardNetworkState(int networkState) {
        for (NetworkStateCallBack networkStateCallBack : networkStateCallbacks) {
            networkStateCallBack.currentNetworkState(networkState);
        }
    }

    public void releaseHighBandwidthNetwork() {
        forwardNetworkState(UI_STATE_REQUEST_NETWORK);
        connectivityManager.bindProcessToNetwork(null);
        unregisterNetworkCallback();
    }

    public void unregisterNetworkCallback() {
        if (networkCallback != null) {
            Log.d(LOG_TAG, "Unregistering network callback");
            connectivityManager.unregisterNetworkCallback(networkCallback);
            networkCallback = null;
        }
    }
}
