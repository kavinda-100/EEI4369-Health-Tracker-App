package com.s22010170.heathtrakerapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.s22010170.heathtrakerapp.utils.MyWebViewClient;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

public class FullNewsArticelFragment extends Fragment {
    WebView webView;
    ShowMessage showMessage;
    LinearProgressIndicator progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_news_articel, container, false);

        // get the views
        webView = view.findViewById(R.id.webView_of_news_articel);
        progressBar = view.findViewById(R.id.progressBar_full_artical);

        // create an instance of the ShowMessage class
        showMessage = new ShowMessage();

        // show the progress bar
        showProgressBar(true);

        // get the arguments
        Bundle bundle = getArguments();
        if(bundle == null){
            showMessage.show("Error", "No data found", getContext());
            return view;
        }
        // get the url
        String url = bundle.getString("url");
        if(url == null){
            showMessage.show("Error", "No data found", getContext());
            return view;
        }
        // web settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        // load the url
        webView.loadUrl(url);
        // hide the progress bar
        showProgressBar(false);

        return view;
    }

    private void showProgressBar(boolean show){
        if(show){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

}