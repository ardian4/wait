package com.please.wait.ui.favorite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.please.core.Result;
import com.please.domain.model.Comic;
import com.please.wait.databinding.ActivityFavoriteComicBinding;
import com.please.wait.ui.common.ComicsDiff;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class BrowseFavoriteComicActivity extends AppCompatActivity {

    private ActivityFavoriteComicBinding mBinding;
    private final BrowseFavoriteRecycleViewAdapter mRecycleViewAdapter = new BrowseFavoriteRecycleViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFavoriteComicBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        mBinding.recycleView.setAdapter(mRecycleViewAdapter);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        BrowseFavoriteViewModel mViewModel = new ViewModelProvider(this)
                .get(BrowseFavoriteViewModel.class);
        mViewModel.getComicLiveData().observe(this, comicResult -> {
            Timber.i("comics live data changed to: %s", comicResult);
            if (comicResult == null) {
                return;
            }
            int status = comicResult.getStatus();
            switch (status) {
                case Result.RESULT_SUCCESS:
                    List<Comic> oldItems = mRecycleViewAdapter.getItems();
                    List<Comic> favoriteComics = comicResult.getData();
                    mRecycleViewAdapter.setItems(favoriteComics);
                    DiffUtil.Callback difference = ComicsDiff.diffOf(oldItems, favoriteComics);
                    DiffUtil.calculateDiff(difference).dispatchUpdatesTo(mRecycleViewAdapter);
                    break;
                case Result.RESULT_PROGRESS:
                    //todo show progress
                    Snackbar.make(this, mBinding.getRoot(), "P", Snackbar.LENGTH_SHORT).show();
                    break;
                case Result.RESULT_FAIL: {
                    Throwable exception = comicResult.getException();
                    String msg = exception != null && exception.getMessage() != null ? exception.getMessage() : "Error";
                    Snackbar.make(this, mBinding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
                }
                break;
                case Result.RESULT_CANCELLED:
                default:
                    //no op
                    break;

            }
        });
        mViewModel.setup();

    }

}