package com.s22010170.heathtrakerapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.s22010170.heathtrakerapp.utils.MyWebViewClient;


public class VideosFragment extends Fragment {

    WebView webView;
    SearchView searchView;
    LinearProgressIndicator progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        // get the views
        webView = view.findViewById(R.id.webView_videos);
        searchView = view.findViewById(R.id.search_view_videos);
        progressBar = view.findViewById(R.id.progressBar_videos);

        // web settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient(progressBar));
        // Load a default YouTube URL for initial display
        String defaultUrl = "https://www.youtube.com/results?search_query=health+news+videos";
        webView.loadUrl(defaultUrl);

        // Set up the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // When the user submits the search, update the URL with the search query
                loadYouTubeSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // You can also update the URL as the user types, if desired
                return false;
            }
        });

        return view;
    }
    // Method to construct the YouTube search URL and load it in the WebView
    private void loadYouTubeSearch(String query) {
        // Construct the search URL
        String searchUrl = "https://www.youtube.com/results?search_query=" + query.replace(" ", "+");
        // Load the constructed URL
        webView.loadUrl(searchUrl);
    }
}