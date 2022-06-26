package com.please.wait.ui.favorite;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.please.domain.model.Comic;
import com.please.wait.databinding.RowFavoriteComicBinding;

import java.util.List;

class BrowseFavoriteRecycleViewAdapter extends RecyclerView.Adapter<BrowseFavoriteViewHolder> {

    private List<Comic> items;

    @NonNull
    @Override
    public BrowseFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFavoriteComicBinding binding = RowFavoriteComicBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BrowseFavoriteViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseFavoriteViewHolder holder, int position) {
        Comic comic = items.get(position);
        holder.mTitle.setText(comic.getTitle());
        holder.mDescription.setText(comic.getDescription());
        Glide.with(holder.mImageView).load(comic.getImage()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<Comic> items) {
        this.items = items;
    }

    public List<Comic> getItems(){
        return items;
    }
}
