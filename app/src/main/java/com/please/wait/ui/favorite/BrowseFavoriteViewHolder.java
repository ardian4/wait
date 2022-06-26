package com.please.wait.ui.favorite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.please.wait.databinding.RowFavoriteComicBinding;

class BrowseFavoriteViewHolder extends RecyclerView.ViewHolder {

    final ImageView mImageView;
    final TextView mTitle;
    final TextView mDescription;

    public BrowseFavoriteViewHolder(@NonNull View itemView, RowFavoriteComicBinding binding) {
        super(itemView);
        mImageView = binding.imageView;
        mTitle = binding.title;
        mDescription = binding.description;
    }
}
