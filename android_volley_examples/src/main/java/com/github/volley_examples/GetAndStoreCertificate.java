package com.github.volley_examples;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.github.volley_examples.utils.Constants;
import com.github.volley_examples.utils.Extras;
import com.github.volley_examples.utils.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dkocian on 1/14/2015.
 */
public class GetAndStoreCertificate extends IntentService {
    // Defines a custom Intent action
    private static final String TAG = GetAndStoreCertificate.class.getSimpleName();
    private static final int BUFFER_SIZE = 1024;

    public GetAndStoreCertificate() {
        this(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GetAndStoreCertificate(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra(Extras.URL);
        getAndStoreCertificate(url);
    }

    public void getAndStoreCertificate(String url) {
        OkUrlFactory okUrlFactory = new OkUrlFactory(new OkHttpClient());
        String fileName = Utils.getFileNameFromUrl(url);
        FileOutputStream fileOutput = null;
        try {
            // Set the download URL, a url that points to a file on the internet this is the file to be downloaded
            URL downloadUrl = new URL(url);
            // Create the new connection
            HttpURLConnection urlConnection = okUrlFactory.open(downloadUrl);
            // Set up some things on the connection
            urlConnection.setRequestMethod(HttpGet.METHOD_NAME);
            urlConnection.setDoOutput(true);
            // Connect!
            urlConnection.connect();
            // Set the path where we want to save the file in this case, going to save it on the root directory of the sd card.
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // Create a new file, specifying the path, and the filename which we want to save the file as.
            File file = new File(SDCardRoot, fileName);
            // This will be used to write the downloaded data into the file we created
            fileOutput = new FileOutputStream(file);
            // This will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();
            // This is the total size of the file
            int totalSize = urlConnection.getContentLength();
            // Variable to store total downloaded bytes
            int downloadedSize = 0;
            // Create a buffer...
            byte[] buffer = new byte[BUFFER_SIZE];
            int bufferLength = 0; //used to store a temporary size of the buffer
            // Now, read through the input buffer and write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                // Add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                // Add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                // This is where you would do something to report the progress, like this maybe
//                updateProgress(downloadedSize, totalSize);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            fileName = null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            fileName = null;
        }
        if (fileOutput != null) {
            // Close the output stream when done
            try {
                fileOutput.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        broadCastIntent(fileName);
    }

    private void updateProgress(int downloadSide, int totalSize) {
        Intent localIntent = new Intent();
        localIntent.setAction(Constants.BROADCAST_ACTION);
        // Puts the file name into the Intent
        localIntent.putExtra(Extras.DOWNLOAD_SIZE, downloadSide);
        localIntent.putExtra(Extras.TOTAL_SIZE, totalSize);
        // Broadcasts the Intent to receivers in this app.
        sendBroadcast(localIntent);
    }

    private void broadCastIntent(String fileName) {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        // Puts the file name into the Intent
        localIntent.putExtra(Extras.FILE_NAME, fileName);
        // Broadcasts the Intent to receivers in this app.
        sendBroadcast(localIntent);
    }
}
