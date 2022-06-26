package com.please.wait.ui.common;

import androidx.recyclerview.widget.DiffUtil;

import com.please.domain.model.Comic;

import java.util.List;

public class ComicsDiff extends DiffUtil.Callback {

    private final List<Comic> mOldItems;
    private final List<Comic> mNewItems;

    public ComicsDiff(List<Comic> oldItems, List<Comic> newItems) {
        mOldItems = oldItems;
        mNewItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return mOldItems != null ? mOldItems.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewItems != null ? mNewItems.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Comic oldComic = mOldItems.get(oldItemPosition);
        Comic newComic = mNewItems.get(newItemPosition);

        return oldComic.getId() == newComic.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Comic oldComic = mOldItems.get(oldItemPosition);
        Comic newComic = mNewItems.get(newItemPosition);

        return oldComic.equals(newComic);
    }

    public static DiffUtil.Callback diffOf(List<Comic> oldItems, List<Comic> newItems) {
        return new ComicsDiff(oldItems, newItems);
    }
}
