package com.github.volley_examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.volley_examples.toolbox.OkHttpStack;

import java.io.InputStream;

/**
 * Created by dkocian on 1/2/2015.
 */
public class OkHttpStackSslTest extends Activity {
    public static final String TAG = OkHttpStackSslTest.class.getSimpleName();
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__ss_ssl_http_client);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        Button btnSimpleRequest = (Button) findViewById(R.id.btn_simple_request);
        btnSimpleRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream keyStore = getResources().openRawResource(R.raw.my);
                RequestQueue queue = Volley.newRequestQueue(OkHttpStackSslTest.this,
                        new OkHttpStack(keyStore));
                StringRequest myStringRequest = new StringRequest(Request.Method.GET,
                        "https://ave.bolyartech.com:44401/https_test.html", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
                queue.add(myStringRequest);
            }
        });
    }
}
