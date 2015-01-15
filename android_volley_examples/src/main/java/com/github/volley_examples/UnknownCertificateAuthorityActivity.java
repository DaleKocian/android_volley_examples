package com.github.volley_examples;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;

import com.github.volley_examples.receivers.ResponseReceiver;
import com.github.volley_examples.utils.Constants;
import com.github.volley_examples.utils.Extras;
import com.github.volley_examples.utils.Urls;

/**
 * Created by dkocian on 1/14/2015.
 */
public class UnknownCertificateAuthorityActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unknown_cert_auth_ui);
    }

    public void executeHttps(View view) {
        Intent getAndStoreCertificateService = new Intent(this, GetAndStoreCertificate.class);
        getAndStoreCertificateService.putExtra(Extras.URL, Urls.CERT_URL);
        startService(getAndStoreCertificateService);
        ResponseReceiver responseReceiver = new ResponseReceiver();
        IntentFilter mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        this.registerReceiver(responseReceiver, mStatusIntentFilter, null, getHandler());
    }

    private Handler getHandler() {
        HandlerThread handlerThread = new HandlerThread("ht");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        return new Handler(looper);
    }
}
