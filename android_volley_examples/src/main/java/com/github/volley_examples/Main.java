package com.github.volley_examples;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.volley_examples.adapters.MyAdapter;
import com.github.volley_examples.itemdecoration.DividerItemDecoration;
import com.github.volley_examples.itemdecoration.HorizontalLineDecorator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dkocian on 1/15/2015.
 */
public class Main extends Activity {

    private static final String TAG = Main.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvRoot);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, getStringArray());
        mRecyclerView.addItemDecoration(new HorizontalLineDecorator(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private String[] getStringArray() {
        return new String[]{UnknownCertificateAuthorityActivity.class.getName(),
                OkHttpStackSslTest.class.getName()};
    }

    @Override
    public void onTrimMemory(int level) {
        Map<Integer, String> constantMap = getConstantMap();
        switch (level) {
            case TRIM_MEMORY_RUNNING_MODERATE:
                Log.e(TAG, getConstantName(constantMap, TRIM_MEMORY_RUNNING_MODERATE));
                break;
            case TRIM_MEMORY_RUNNING_LOW:
                Log.e(TAG, getConstantName(constantMap, TRIM_MEMORY_RUNNING_LOW));
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL:
                Log.e(TAG, getConstantName(constantMap, TRIM_MEMORY_RUNNING_CRITICAL));
                break;
            case TRIM_MEMORY_UI_HIDDEN:
                Log.e(TAG, getConstantName(constantMap, TRIM_MEMORY_UI_HIDDEN));
                break;
            case TRIM_MEMORY_BACKGROUND:
                Log.e(TAG, getConstantName(constantMap, TRIM_MEMORY_BACKGROUND));
                break;
            case TRIM_MEMORY_MODERATE:
                Log.e(TAG, getConstantName(constantMap, TRIM_MEMORY_MODERATE));
                break;
            case TRIM_MEMORY_COMPLETE:
                Log.e(TAG, getConstantName(constantMap, TRIM_MEMORY_COMPLETE));
                break;
            default:
                Log.e(TAG, "Default Case Hit!");
                break;
        }
    }

    private static Map<Integer, String> getConstantMap() {
        Map<Integer, String> constantMap = new HashMap<>();
        for (Field field : ComponentCallbacks2.class.getDeclaredFields()) {
            if (((field.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) != 0) && int.class == field.getType()) {
                // only record final static int fields
                try {
                    constantMap.put(field.getInt(null), field.getName());
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return constantMap;
    }

    public static String getConstantName(Map<Integer, String> constantNames, int constVal) {
        return constantNames.get(constVal);
    }
}
