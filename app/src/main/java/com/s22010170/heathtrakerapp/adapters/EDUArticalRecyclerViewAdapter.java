package com.s22010170.heathtrakerapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.s22010170.heathtrakerapp.AddMedicationFragment;
import com.s22010170.heathtrakerapp.FullNewsArticelFragment;
import com.s22010170.heathtrakerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EDUArticalRecyclerViewAdapter extends RecyclerView.Adapter<EDUArticalRecyclerViewAdapter.ViewHolder>{
    List<Article> articles;
    Context context;

    public EDUArticalRecyclerViewAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public EDUArticalRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artical_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EDUArticalRecyclerViewAdapter.ViewHolder holder, int position) {
        // get the single article
        Article article = articles.get(position);
        // set the title, source and image
        holder.articleTitle.setText(article.getTitle());
        holder.articleSource.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.image_not_supported)
                .placeholder(R.drawable.image_not_supported)
                .into(holder.articleImage);

        // set the on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullNewsArticelFragment fullNewsArticelFragment = new FullNewsArticelFragment();
                // set the bundle
                Bundle bundle = new Bundle();
                bundle.putString("url", article.getUrl());
                fullNewsArticelFragment.setArguments(bundle);
                // navigate to the full news article fragment
                if (context instanceof FragmentActivity) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_container, fullNewsArticelFragment)
                            .setReorderingAllowed(true)
                            .addToBackStack("fromNewsArticalFragment")
                            .commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle, articleSource;
        ImageView articleImage;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.artical_title);
            articleSource = itemView.findViewById(R.id.artical_source);
            articleImage = itemView.findViewById(R.id.artical_image);
            cardView = itemView.findViewById(R.id.artical_row);
        }
    }
}
