package com.github.volley_examples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by dkocian on 1/15/2015.
 */
public class Main extends ListActivity {

    private static final String TAG = Main.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[]{UnknownCertificateAuthorityActivity.class.getSimpleName(),
                OkHttpStackSslTest.class.getSimpleName()};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(getBaseContext(), UnknownCertificateAuthorityActivity.class);
                break;
            case 1:
                intent = new Intent(getBaseContext(), OkHttpStackSslTest.class);
                break;
            default:
                Log.e(TAG, "Default Case Hit");
                break;
        }
        startActivity(intent);
    }
}
