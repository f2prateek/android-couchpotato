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
import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.couchpotato.movie.CouchPotatoMovie;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieRefreshResponse;
import com.f2prateek.couchpotato.model.moviedb.MovieDBMovie;
import com.f2prateek.couchpotato.model.moviedb.MovieDbConfiguration;
import com.f2prateek.couchpotato.services.CouchPotatoApi;
import com.f2prateek.couchpotato.services.MovieDBApi;
import com.f2prateek.couchpotato.ui.fragments.MovieCastFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieCrewFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieInfoFragment;
import com.f2prateek.couchpotato.ui.widgets.AspectRatioImageView;
import com.f2prateek.couchpotato.ui.widgets.NotifyingScrollView;
import com.f2prateek.couchpotato.ui.widgets.PagerSlidingTabStrip;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.f2prateek.couchpotato.util.Ln;
import com.f2prateek.couchpotato.util.RetrofitErrorHandler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import javax.inject.Provider;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * An activity to show a single {@link com.f2prateek.couchpotato.model.moviedb.MovieDBMovie}. This
 * is a CouchPotatoMovie available in the user's library.
 */
public class ViewMovieActivity extends BaseAuthenticatedActivity implements Callback<MovieDBMovie> {

  public static final String MOVIE_KEY = "ViewMovieActivity.MOVIE_KEY";

  private static final int CREW_FRAGMENT_NUM = 0;
  private static final int CAST_FRAGMENT_NUM = 1;
  private static final int INFO_FRAGMENT_NUM = 2;
  private static final int ARTWORK_FRAGMENT_NUM = 3;

  @InjectView(R.id.scroll_view) NotifyingScrollView notifyingScrollView;
  @InjectView(R.id.movie_backdrop) AspectRatioImageView movie_backdrop;
  @InjectView(R.id.movie_tagline) TextView movie_tagline;
  @InjectView(R.id.movie_info_pager) ViewPager movie_info_pager;
  @InjectView(R.id.tabs) PagerSlidingTabStrip tabStrip;

  private Drawable actionBarBackgroundDrawable;

  private MovieDBMovie movieDBMovie;
  private CouchPotatoMovie couchPotatoMovie;

  @Inject Provider<MovieDbConfiguration> configurationProvider;
  @Inject MovieDBApi movieDBApi;
  @Inject CouchPotatoApi couchPotatoApi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setDisplayShowHomeEnabled(false);

    couchPotatoMovie =
        gson.fromJson(getIntent().getExtras().getString(MOVIE_KEY), CouchPotatoMovie.class);
    movieDBApi.get_movie(couchPotatoMovie.library.info.tmdb_id, this);

    setContentView(R.layout.activity_movie);
    setUpFancyScroll(getResources().getColor(R.color.transparent_action_bar_color));
    tabStrip.setIndicatorColor(getResources().getColor(R.color.transparent_action_bar_color));
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
        refreshMovie();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void openImdbPage() {
    openUrl(movieDBMovie.getImdbPage());
  }

  public void playTrailer() {
    if (!CollectionUtils.isEmpty(movieDBMovie.getYoutubeTrailers())) {
      openUrl("https://www.youtube.com/watch?v=" + movieDBMovie.getYoutubeTrailers().get(0));
    }
  }

  public void openUrl(String url) {
    if (url == null) {
      return;
    }
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  public static class MovieInfoAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final MovieDBMovie movieDBMovie;
    private final Gson gson;

    public MovieInfoAdapter(FragmentManager fm, Gson gson, Context context,
        MovieDBMovie movieDBMovie) {
      super(fm);
      this.gson = gson;
      this.context = context;
      this.movieDBMovie = movieDBMovie;
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case CREW_FRAGMENT_NUM:
          return MovieCrewFragment.newInstance(gson, movieDBMovie.casts.crew);
        case CAST_FRAGMENT_NUM:
          return MovieCastFragment.newInstance(gson, movieDBMovie.casts.cast);
        case INFO_FRAGMENT_NUM:
          return MovieInfoFragment.newInstance(gson, movieDBMovie);
        case ARTWORK_FRAGMENT_NUM:
          return MovieCastFragment.newInstance(gson, movieDBMovie.casts.cast);
        default:
          return null;
      }
    }

    @Override public int getCount() {
      return 4;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case CREW_FRAGMENT_NUM:
          return context.getResources().getString(R.string.crew);
        case CAST_FRAGMENT_NUM:
          return context.getResources().getString(R.string.cast);
        case INFO_FRAGMENT_NUM:
          return context.getResources().getString(R.string.info);
        case ARTWORK_FRAGMENT_NUM:
          return context.getResources().getString(R.string.artwork);
        default:
          return super.getPageTitle(position);
      }
    }
  }

  /**
   * Refresh the CouchPotatoMovie from the server.
   */
  private void refreshMovie() {
    couchPotatoApi.movie_refresh(couchPotatoMovie.id, new Callback<MovieRefreshResponse>() {

      @Override
      public void success(MovieRefreshResponse movieRefreshResponse, Response response) {
        Ln.d("Refreshed CouchPotatoMovie: " + String.valueOf(movieRefreshResponse.success));
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not get refresh with id %d" + couchPotatoMovie.id);
        RetrofitErrorHandler.showError(ViewMovieActivity.this, retrofitError);
      }
    });
  }

  @Override public void success(MovieDBMovie movieDBMovie, Response response) {
    this.movieDBMovie = movieDBMovie;
    refreshView(movieDBMovie);
  }

  @Override public void failure(RetrofitError error) {
    RetrofitErrorHandler.showError(this, error);
  }

  public void refreshView(MovieDBMovie movie) {
    getActionBar().setTitle(movie.title);
    float aspectRatio = 1 / movie.images.backdrops.get(0).aspect_ratio;  // width is measured first
    movie_backdrop.setAspectRatio(aspectRatio);
    Picasso.with(this)
        .load(movie.getBackdropUrl(configurationProvider.get()))
        .fit()
        .centerInside()
        .into(movie_backdrop);
    if (TextUtils.isEmpty(movie.tagline)) {
      movie_tagline.setVisibility(View.GONE);
    } else {
      movie_tagline.setText(movie.tagline);
    }
    // TODO : figure out height issue, currently VP height is fixed, make it dynamic
    movie_info_pager.setAdapter(new MovieInfoAdapter(getFragmentManager(), gson, this, movie));
    movie_info_pager.setCurrentItem(2);
    tabStrip.setViewPager(movie_info_pager);
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
