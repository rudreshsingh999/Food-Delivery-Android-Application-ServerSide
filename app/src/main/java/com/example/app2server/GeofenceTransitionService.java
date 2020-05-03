package com.example.app2server;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.GeofencingEvent;

import static com.google.android.gms.common.GooglePlayServicesUtil.getErrorString;


class GeofenceTrasitionService extends IntentService {

    private static final String TAG = GeofenceTrasitionService.class.getSimpleName();


    public GeofenceTrasitionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMsg);
        }
    }
}

