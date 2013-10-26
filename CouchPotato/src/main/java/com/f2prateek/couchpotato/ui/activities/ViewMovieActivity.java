/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.CouchPotatoApi;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.couchpotato.movie.Movie;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieGetResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieRefreshResponse;
import com.f2prateek.couchpotato.ui.CropPosterTransformation;
import com.f2prateek.couchpotato.ui.base.BaseAuthenticatedActivity;
import com.f2prateek.couchpotato.ui.fragments.MovieCastFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieInfoFragment;
import com.f2prateek.couchpotato.ui.widgets.NotifyingScrollView;
import com.f2prateek.couchpotato.ui.widgets.PagerSlidingTabStrip;
import com.f2prateek.couchpotato.util.Ln;
import com.f2prateek.couchpotato.util.RetrofitErrorHandler;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/** An activity to show a single {@link Movie}. This is a movie available in the user's library. */
public class ViewMovieActivity extends BaseAuthenticatedActivity {

  public static final String MOVIE_KEY = "com.f2prateek.couchpotato.MOVIE_KEY";
  private final Joiner commaJoiner = Joiner.on(", ");

  @Inject CouchPotatoApi couchPotatoApi;

  @InjectView(R.id.scroll_view) NotifyingScrollView notifyingScrollView;
  @InjectView(R.id.movie_backdrop) ImageView movie_backdrop;
  @InjectView(R.id.movie_tagline) TextView movie_tagline;
  @InjectView(R.id.movie_info_pager) ViewPager movie_info_pager;
  @InjectView(R.id.tabs) PagerSlidingTabStrip tabStrip;

  private Drawable actionBarBackgroundDrawable;
  private Movie movie;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setDisplayShowHomeEnabled(false);
    setContentView(R.layout.activity_movie);

    movie = new Gson().fromJson(getIntent().getExtras().getString(MOVIE_KEY), Movie.class);
    setUpFancyScroll(getResources().getColor(R.color.ab_color));
    refreshView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.view_movie_activity, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_imdb:
        openImdbPage();
        return true;
      case R.id.action_play_trailer:
        playTrailer();
        return true;
      case R.id.action_refresh:
        refresh();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void openImdbPage() {
    openUrl(movie.getImdbPage());
  }

  public void playTrailer() {
    //TODO:stub
  }

  public void openUrl(String url) {
    Intent imdb = new Intent(Intent.ACTION_VIEW);
    imdb.setData(Uri.parse(url));
    startActivity(imdb);
  }

  public static class MovieInfoAdapter extends FragmentPagerAdapter {

    private final Movie movie;

    public MovieInfoAdapter(FragmentManager fm, Movie movie) {
      super(fm);
      this.movie = movie;
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return MovieCastFragment.newInstance(movie);
        case 1:
          return MovieInfoFragment.newInstance(movie);
        case 2:
          return MovieCastFragment.newInstance(movie);
        default:
          return null;
      }
    }

    @Override public int getCount() {
      return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        // TODO : use resources
        case 0:
          return "Cast";
        case 1:
          return "Info";
        case 2:
          return "Files";
        default:
          return super.getPageTitle(position);
      }
    }
  }

  /**
   * Refresh the movie from the server. We still have to fetch it again from the server.
   */
  private void refresh() {
    couchPotatoApi.movie_refresh(movie.id, new Callback<MovieRefreshResponse>() {
      @Override public void success(MovieRefreshResponse movieRefreshResponse, Response response) {
        Ln.d("Refreshed movie: " + String.valueOf(movieRefreshResponse.success));
        if (movieRefreshResponse.success) update();
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not get refresh with id %d" + movie.id);
        RetrofitErrorHandler.showError(ViewMovieActivity.this, retrofitError);
      }
    });
  }

  /**
   * Update this movie from the server.
   */
  private void update() {
    couchPotatoApi.movie_get(movie.id, new Callback<MovieGetResponse>() {
      @Override public void success(MovieGetResponse movieGetResponse, Response response) {
        Ln.d("Got movie: " + String.valueOf(movieGetResponse.success));
        if (movieGetResponse.success) {
          movie = movieGetResponse.movie;
          refreshView();
        }
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not get movie with id %d" + movie.id);
        RetrofitErrorHandler.showError(ViewMovieActivity.this, retrofitError);
      }
    });
  }

  public void refreshView() {
    initializePoster();
    // TODO : figure out height issue
    movie_info_pager.setAdapter(new MovieInfoAdapter(getFragmentManager(), movie));
    movie_info_pager.setCurrentItem(1);
    tabStrip.setViewPager(movie_info_pager);
  }

  public void initializePoster() {
    getActionBar().setTitle(movie.library.titles.get(0).title);
    Picasso.with(this)
        .load(movie.getBackdropUrl())
        .transform(new CropPosterTransformation(movie_backdrop,
            getResources().getConfiguration().orientation))
        .into(movie_backdrop);
    if (TextUtils.isEmpty(movie.library.info.tagline)) {
      movie_tagline.setVisibility(View.GONE);
    } else {
      movie_tagline.setText(movie.library.info.tagline);
    }
    //movie_plot.setText(movie.library.info.plot);
    //writeToTextView(movie_genres, movie.library.info.genres);
    //writeToTextView(movie_cast, movie.library.info.actors);
    //writeToTextView(movie_directors, movie.library.info.directors);
    //writeToTextView(movie_writers, movie.library.info.writers);
  }

  private void writeToTextView(TextView textView, List<String> text) {
    if (text != null && text.size() > 0) {
      textView.setText(commaJoiner.join(text));
    } else {
      textView.setText(R.string.info_not_available);
    }
  }

  // ViewFX

  public void setUpFancyScroll(int color) {
    actionBarBackgroundDrawable = new ColorDrawable(color);
    actionBarBackgroundDrawable.setAlpha(0);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      actionBarBackgroundDrawable.setCallback(drawableCallback);
    }
    getActionBar().setBackgroundDrawable(actionBarBackgroundDrawable);

    notifyingScrollView.setOnScrollChangedListener(onScrollChangedListener);
  }

  private NotifyingScrollView.OnScrollChangedListener onScrollChangedListener =
      new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
          final int headerHeight =
              findViewById(R.id.movie_backdrop).getHeight() - getActionBar().getHeight();
          final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
          final int newAlpha = (int) (ratio * 255);
          actionBarBackgroundDrawable.setAlpha(newAlpha);
        }
      };

  private Drawable.Callback drawableCallback = new Drawable.Callback() {
    @Override
    public void invalidateDrawable(Drawable who) {
      getActionBar().setBackgroundDrawable(who);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
    }
  };
}
