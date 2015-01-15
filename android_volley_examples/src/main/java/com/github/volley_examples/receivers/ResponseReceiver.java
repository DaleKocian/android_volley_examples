package com.github.volley_examples.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.github.volley_examples.utils.Constants;
import com.github.volley_examples.utils.Extras;
import com.github.volley_examples.utils.Urls;
import com.github.volley_examples.utils.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by dkocian on 1/15/2015.
 */
public class ResponseReceiver extends BroadcastReceiver {

    public static final String TAG = ResponseReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String fileName = intent.getStringExtra(Extras.FILE_NAME);
        if (fileName != null) {
            try {
                makeRequest(Urls.DOMAIN_URL, fileName);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (CertificateException e) {
                Log.e(TAG, e.getMessage());
            } catch (KeyStoreException e) {
                Log.e(TAG, e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, e.getMessage());
            } catch (KeyManagementException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void makeRequest(String requestUrl, String certificateName)
            throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // Load CAs from an InputStream (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance(Constants.X_509);
        Certificate ca;
        InputStream caInput = null;
        try {
            File SDCardRoot = Environment.getExternalStorageDirectory();
            File file = new File(SDCardRoot, certificateName);
            Log.i(TAG, file.getAbsolutePath());
            caInput = new BufferedInputStream(new FileInputStream(file));
            ca = cf.generateCertificate(caInput);
            Log.i(TAG, Constants.ALIAS + "=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            if (caInput != null) {
                caInput.close();
            }
        }
        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry(Constants.ALIAS, ca);
        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance(SSLSocketFactory.TLS);
        context.init(null, tmf.getTrustManagers(), null);
        // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(requestUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setSslSocketFactory(context.getSocketFactory());
        OkUrlFactory okUrlFactory = new OkUrlFactory(okHttpClient);
        HttpURLConnection urlConnection = okUrlFactory.open(url);
        InputStream inputStream = urlConnection.getInputStream();
        Utils.copyInputStreamToOutputStream(inputStream, System.out);
    }
}
