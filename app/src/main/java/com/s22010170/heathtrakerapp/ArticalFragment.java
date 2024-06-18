package com.s22010170.heathtrakerapp;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.s22010170.heathtrakerapp.adapters.EDUArticalRecyclerViewAdapter;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

import java.util.ArrayList;
import java.util.List;

public class ArticalFragment extends Fragment {
    LinearProgressIndicator progressBar;
    RecyclerView articalRecyclerView;
    EDUArticalRecyclerViewAdapter articleRecyclerViewAdapter;
    SearchView searchView;
    List<Article> articles = new ArrayList<Article>();
    ShowMessage showMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artical, container, false);
        // get the views
        progressBar = view.findViewById(R.id.progressBar_artical);
        articalRecyclerView = view.findViewById(R.id.recyclerView_artical);
        searchView = view.findViewById(R.id.search_view_artical);
        // create an instance of the ShowMessage class
        showMessage = new ShowMessage();

        // set the search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNewsArticles(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        getNewsArticles("");
        setupRecyclerView();
//        getNewsArticles();
        return view;
    }

    private void setupRecyclerView(){
        articalRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        articleRecyclerViewAdapter = new EDUArticalRecyclerViewAdapter(articles);
        articalRecyclerView.setAdapter(articleRecyclerViewAdapter);
    }

    private void changeProgressBarVisibility(boolean visibility){
        if (visibility){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    private void getNewsArticles(String query){
        // set the progress bar to visible
        changeProgressBarVisibility(true);
        // get the news articles
        NewsApiClient newsApiClient = new NewsApiClient(getResources().getString(R.string.news_api_key));
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category("health")
                        .country("us")
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        // set the progress bar to invisible
                        changeProgressBarVisibility(false);
                        // print the title of the articles
                        response.getArticles().forEach(article -> {
                            System.out.println(article.getTitle());
                        });

                        // update the UI
                        requireActivity().runOnUiThread(() -> {
                            changeProgressBarVisibility(false);
                            articles = response.getArticles();
                            articleRecyclerViewAdapter = new EDUArticalRecyclerViewAdapter(articles);
                            articalRecyclerView.setAdapter(articleRecyclerViewAdapter);
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // set the progress bar to invisible
                        changeProgressBarVisibility(false);
                        // print the error message
                        if (throwable.getMessage() != null)
                        {
                            System.out.println(throwable.getMessage());
                            showMessage.show("Error", throwable.getMessage(), requireContext());
                        }
                        System.out.println("An error occurred while loading the articles.");
                        showMessage.show("Error", "An error occurred while loading the articles.", requireContext());
                    }
                }
        );
    }
}