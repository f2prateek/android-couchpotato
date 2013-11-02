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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.bus.DataEvent;
import com.f2prateek.couchpotato.model.couchpotato.movie.CouchPotatoMovie;
import com.f2prateek.couchpotato.model.moviedb.MovieDBMovie;
import com.f2prateek.couchpotato.services.MovieDBService;
import com.f2prateek.couchpotato.ui.CropPosterTransformation;
import com.f2prateek.couchpotato.ui.fragments.MovieCastFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieInfoFragment;
import com.f2prateek.couchpotato.ui.widgets.NotifyingScrollView;
import com.f2prateek.couchpotato.ui.widgets.PagerSlidingTabStrip;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

/**
 * An activity to show a single {@link com.f2prateek.couchpotato.model.moviedb.MovieDBMovie}. This
 * is a CouchPotatoMovie available in the user's library.
 */
public class ViewMovieActivity extends BaseAuthenticatedActivity {

  public static final String MOVIE_KEY = "ViewMovieActivity.MOVIE_KEY";

  private static final int CREW_FRAGMENT_NUM = 0;
  private static final int CAST_FRAGMENT_NUM = 1;
  private static final int INFO_FRAGMENT_NUM = 2;
  private static final int ARTWORK_FRAGMENT_NUM = 3;

  @InjectView(R.id.scroll_view) NotifyingScrollView notifyingScrollView;
  @InjectView(R.id.movie_backdrop) ImageView movie_backdrop;
  @InjectView(R.id.movie_tagline) TextView movie_tagline;
  @InjectView(R.id.movie_info_pager) ViewPager movie_info_pager;
  @InjectView(R.id.tabs) PagerSlidingTabStrip tabStrip;

  private Drawable actionBarBackgroundDrawable;

  private CouchPotatoMovie couchPotatoMovie;
  private MovieDBMovie movieDBMovie;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setDisplayShowHomeEnabled(false);

    couchPotatoMovie =
        gson.fromJson(getIntent().getExtras().getString(MOVIE_KEY), CouchPotatoMovie.class);
    Intent intent = new Intent(this, MovieDBService.class);
    intent.putExtra(MovieDBService.EXTRA_MOVIE_ID, couchPotatoMovie.library.info.tmdb_id);
    startService(intent);

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
    openUrl(couchPotatoMovie.getImdbPage());
  }

  public void playTrailer() {
    openUrl("https://www.youtube.com/watch?v=" + movieDBMovie.trailers.youtube.get(0).name);
  }

  public void openUrl(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  public static class MovieInfoAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final MovieDBMovie movieDBMovie;
    private final CouchPotatoMovie couchPotatoMovie;

    public MovieInfoAdapter(FragmentManager fm, Context context, MovieDBMovie movieDBMovie,
        CouchPotatoMovie couchPotatoMovie) {
      super(fm);
      this.context = context;
      this.movieDBMovie = movieDBMovie;
      this.couchPotatoMovie = couchPotatoMovie;
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case CREW_FRAGMENT_NUM:
          return MovieCastFragment.newInstance(movieDBMovie.casts.cast);
        case CAST_FRAGMENT_NUM:
          return MovieCastFragment.newInstance(movieDBMovie.casts.cast);
        case INFO_FRAGMENT_NUM:
          return MovieInfoFragment.newInstance(couchPotatoMovie);
        case ARTWORK_FRAGMENT_NUM:
          return MovieCastFragment.newInstance(movieDBMovie.casts.cast);
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
    /*
    couchPotatoApi.movie_refresh(couchPotatoMovie.id, new Callback<MovieRefreshResponse>() {

      @Override public void success(MovieRefreshResponse movieRefreshResponse, Response response) {
        Ln.d("Refreshed CouchPotatoMovie: " + String.valueOf(movieRefreshResponse.success));
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not get refresh with id %d" + couchPotatoMovie.id);
        RetrofitErrorHandler.showError(ViewMovieActivity.this, retrofitError);
      }
    });
  */
  }

  @Subscribe
  public void onMovieReceived(DataEvent<MovieDBMovie> event) {
    movieDBMovie = event.data;
    refreshView();
  }

  public void refreshView() {
    getActionBar().setTitle(couchPotatoMovie.library.titles.get(0).title);
    Picasso.with(this)
        .load(couchPotatoMovie.getBackdropUrl())
        .transform(new CropPosterTransformation(movie_backdrop,
            getResources().getConfiguration().orientation))
        .into(movie_backdrop);
    if (TextUtils.isEmpty(couchPotatoMovie.library.info.tagline)) {
      movie_tagline.setVisibility(View.GONE);
    } else {
      movie_tagline.setText(couchPotatoMovie.library.info.tagline);
    }
    // TODO : figure out height issue, currently VP height is fixed, make it dynamic
    movie_info_pager.setAdapter(
        new MovieInfoAdapter(getFragmentManager(), this, movieDBMovie, couchPotatoMovie));
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
