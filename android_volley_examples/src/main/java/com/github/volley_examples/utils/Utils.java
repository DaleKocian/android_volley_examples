package com.github.volley_examples.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dkocian on 1/15/2015.
 */
public class Utils {
    private static final int BUFFER_SIZE = 8192;

    public static String getFileNameFromUrl(String url) {
        if (url == null) {
            return null;
        }
        int index = indexOfLastSeparator(url);
        return url.substring(index + 1);
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(Constants.UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(Constants.WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static long copyInputStreamToOutputStream(InputStream is, OutputStream os) {
        byte[] buf = new byte[BUFFER_SIZE];
        long total = 0;
        int len = 0;
        try {
            while (-1 != (len = is.read(buf))) {
                os.write(buf, 0, len);
                total += len;
            }
        } catch (IOException ioe) {
            throw new RuntimeException("error reading stream", ioe);
        }
        return total;
    }
}
