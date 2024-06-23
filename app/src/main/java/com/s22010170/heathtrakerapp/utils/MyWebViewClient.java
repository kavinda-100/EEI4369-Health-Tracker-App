package com.s22010170.heathtrakerapp.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MyWebViewClient extends WebViewClient {

    LinearProgressIndicator progressBar;

    // Constructor to initialize the ProgressBar
    public MyWebViewClient(LinearProgressIndicator progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // Show the ProgressBar when the page starts loading
        progressBar.setVisibility(View.VISIBLE);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // Hide the ProgressBar when the page finishes loading
        progressBar.setVisibility(View.GONE);
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }
}
