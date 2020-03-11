package com.hero.libhero.okhttp.download;

public interface ProgressListener {
    void onPreExecute(long contentLength);
    void update(long totalBytes, boolean done);
}
