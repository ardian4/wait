package com.please.wait.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.please.core.Result;
import com.please.domain.model.Comic;
import com.please.wait.R;
import com.please.wait.databinding.ActivityHomeBinding;
import com.please.wait.ui.favorite.BrowseFavoriteComicActivity;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class BrowseComicsActivity extends AppCompatActivity {
    private ActivityHomeBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        BrowseComicsViewModel mViewModel = new ViewModelProvider(this).get(BrowseComicsViewModel.class);
        mViewModel.getComicLiveData().observe(this, comicResult -> {
            Timber.i("comics live data changed to: %s", comicResult);
            if (comicResult == null) {
                return;
            }
            int status = comicResult.getStatus();
            switch (status) {
                case Result.RESULT_SUCCESS:
                    Comic comic = comicResult.getData();
                    Glide.with(this)
                            .load(comicResult.getData().getImage())
                            .into(mBinding.imageView);
                    enableComicActions(true);

                    mBinding.textViewDescription.setText(comic.getDescription());
                    mBinding.textViewTitle.setText(comicResult.getData().getTitle());
                    mBinding.buttonLike.setImageResource(comic.isFavorite()
                            ? R.drawable.ic_favorite_48px : R.drawable.ic_favorite_48px_2);
                    break;
                case Result.RESULT_PROGRESS:
                    //todo show progress
                    break;
                case Result.RESULT_FAIL: {
                    Throwable exception = comicResult.getException();
                    String msg = exception != null && exception.getMessage() != null ? exception.getMessage() : "Error";
                    Snackbar.make(this, mBinding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
                    enableComicActions(true);
                }
                break;
                case Result.RESULT_CANCELLED:
                default:
                    enableComicActions(true);
                    //no op
                    break;

            }
        });
        mViewModel.setup();

        mBinding.buttonLike.setOnClickListener(view -> mViewModel.likeAction());

        mBinding.buttonNextComic.setOnClickListener(view -> {
            enableComicActions(false);
            mViewModel.nextComic();
        });

        mBinding.buttonPreviousComic.setOnClickListener(view -> {
            enableComicActions(false);
            mViewModel.previousComic();
        });

        mBinding.buttonShare.setOnClickListener(v -> {
            Result<Comic> value = mViewModel.getComicLiveData().getValue();
            if (value == null) {
                return;
            }

            Comic data = value.getData();
            if (data == null) {
                return;
            }

            //todo write better text
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, data.getImage());
            sendIntent.setType("image/jpg");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_browse_comics, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.favorites){
            Timber.i("start activity for favorites");
            startActivity(new Intent(this, BrowseFavoriteComicActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enableComicActions(boolean enable) {
        Timber.i("change enable status for comic actions: %s",enable);
        mBinding.buttonNextComic.setEnabled(enable);
        mBinding.buttonPreviousComic.setEnabled(enable);
        mBinding.buttonLike.setEnabled(enable);
    }
}