/*
 * Copyright 2014 Prateek Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import butterknife.InjectView;
import com.astuetz.PagerSlidingTabStrip;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.TMDbDatabase;
import com.f2prateek.couchpotato.data.api.tmdb.model.Backdrop;
import com.f2prateek.couchpotato.data.api.tmdb.model.Images;
import com.f2prateek.couchpotato.data.api.tmdb.model.TMDbMovie;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.fragments.movie.MovieCastInfoFragment;
import com.f2prateek.couchpotato.ui.fragments.movie.MovieCrewInfoFragment;
import com.f2prateek.couchpotato.ui.fragments.movie.MovieReviewsFragment;
import com.f2prateek.couchpotato.ui.fragments.movie.MovieSimilarMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.movie.MovieVideosFragment;
import com.f2prateek.couchpotato.ui.misc.AlphaForegroundColorSpan;
import com.f2prateek.couchpotato.ui.misc.colorizer.ColorScheme;
import com.f2prateek.couchpotato.ui.misc.colorizer.FragmentTabAdapter;
import com.f2prateek.couchpotato.ui.widget.KenBurnsView;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.f2prateek.couchpotato.util.Strings;
import com.f2prateek.dart.InjectExtra;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MovieActivity extends BaseActivity implements AbsListView.OnScrollListener {
  public static final String ARGS_MOVIE = "minified_movie";

  @InjectExtra(ARGS_MOVIE) Movie minifiedMovie;

  // top level header that contains moviePoster, movieBackdrop and tabStrip
  @InjectView(R.id.movie_header) View movieHeader;
  // background that animates with backdrops
  @InjectView(R.id.movie_header_backdrop) KenBurnsView movieBackdrop;
  // poster for this movie
  @InjectView(R.id.movie_header_poster) ImageView moviePoster;
  @InjectView(R.id.movie_pager_strip) PagerSlidingTabStrip tabStrip;
  @InjectView(R.id.movie_pager) ViewPager pager;
  // The action bar icon view
  @InjectView(android.R.id.home) ImageView actionBarIconView;

  @Inject Picasso picasso;
  @Inject TMDbDatabase tmDbDatabase;

  FragmentTabAdapter tabAdapter;
  ShareActionProvider movieShareActionProvider;

  // color of the action bar title
  private int actionBarTitleColor;
  // height of the action bar title
  private int actionBarHeight;
  // height of the restaurantHeader read from dimensions, not the view
  private int headerHeight;
  // minimum translation for the sticky effect
  private int minHeaderTranslation;
  // interpolator for smooth animations
  private AccelerateDecelerateInterpolator smoothInterpolator;
  private RectF rectF1 = new RectF();
  private RectF rectF2 = new RectF();
  private AlphaForegroundColorSpan alphaForegroundColorSpan;
  private SpannableString spannableString;
  private TypedValue typedValue = new TypedValue();

  /** Create an intent to launch this activity. */
  public static Intent createIntent(Context context, Events.OnMovieClickedEvent event) {
    Intent intent = new Intent(context, MovieActivity.class);
    intent.putExtra(ARGS_MOVIE, event.movie);
    return intent;
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inflateLayout(R.layout.activity_movie);

    init();
    setupFancyScroll();

    tabAdapter = new FragmentTabAdapter(this, pager);
    pager.setAdapter(tabAdapter);
    tabAdapter.addTab(MovieCastInfoFragment.class,
        MovieCastInfoFragment.newInstanceArgs(minifiedMovie), R.string.cast);
    tabAdapter.addTab(MovieCrewInfoFragment.class,
        MovieCrewInfoFragment.newInstanceArgs(minifiedMovie), R.string.crew);
    tabAdapter.addTab(MovieVideosFragment.class, MovieVideosFragment.newInstanceArgs(minifiedMovie),
        R.string.videos);
    tabAdapter.addTab(MovieSimilarMoviesFragment.class,
        MovieSimilarMoviesFragment.newInstanceArgs(minifiedMovie), R.string.similar_movies);
    tabAdapter.addTab(MovieReviewsFragment.class,
        MovieReviewsFragment.newInstanceArgs(minifiedMovie), R.string.reviews);

    // Can't be configured via xml so done here!
    tabStrip.setTextColor(getResources().getColor(R.color.white));
    tabStrip.setViewPager(pager);

    picasso.load(minifiedMovie.poster())
        .fit()
        .centerCrop()
        .error(R.drawable.ic_launcher)
        .into(moviePoster);
    movieBackdrop.load(picasso, minifiedMovie.backdrop());

    subscribe(tmDbDatabase.getMovieImages(minifiedMovie.id()), new EndlessObserver<Images>() {
          @Override public void onNext(Images images) {
            if (!CollectionUtils.isNullOrEmpty(images.getBackdrops())) {
              List<String> backdrops = new ArrayList<>();
              for (Backdrop backdrop : images.getBackdrops()) {
                backdrops.add(backdrop.getFilePath());
              }
              movieBackdrop.update(backdrops);
            }
          }
        }
    );
    subscribe(tmDbDatabase.getMovie(minifiedMovie.id()), new EndlessObserver<TMDbMovie>() {
          @Override public void onNext(TMDbMovie tmDbMovie) {
            setShareIntent(tmDbMovie);
          }
        }
    );

    updateColorScheme();
  }

  /**
   * Use the minifiedMovie's poster to find a color scheme and update our views accordingly.
   */
  private void updateColorScheme() {
    subscribe(Observable.from(minifiedMovie.poster()).map(new Func1<String, Bitmap>() {
          @Override public Bitmap call(String url) {
            try {
              return picasso.load(url).get();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        }).map(new Func1<Bitmap, ColorScheme>() {
          @Override public ColorScheme call(Bitmap bitmap) {
            return ColorScheme.fromBitmap(bitmap);
          }
        }).subscribeOn(Schedulers.io()), new EndlessObserver<ColorScheme>() {
          @Override public void onNext(final ColorScheme colorScheme) {
            tabStrip.setIndicatorColor(colorScheme.getPrimaryAccent());
            tabStrip.setDividerColor(colorScheme.getPrimaryAccent());
          }
        }
    );
  }

  /**
   * Initialize some global variables
   */
  private void init() {
    smoothInterpolator = new AccelerateDecelerateInterpolator();
    headerHeight = getResources().getDimensionPixelSize(R.dimen.movie_header_height);
    // we have tabs, so we need twice the space of the actionbar
    minHeaderTranslation = -headerHeight + (2 * getActionBarHeight());
    actionBarTitleColor = getResources().getColor(android.R.color.white);
    alphaForegroundColorSpan = new AlphaForegroundColorSpan(actionBarTitleColor);
  }

  /**
   * Setup the action bar for effects.
   */
  private void setupFancyScroll() {
    spannableString = new SpannableString(minifiedMovie.title());
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setIcon(R.drawable.ic_transparent);
    movieHeader.bringToFront(); // explicit, list scrolls behind the header
    moviePoster.bringToFront();
  }

  /**
   * Get the height of the action bar.
   */
  public int getActionBarHeight() {
    if (actionBarHeight != 0) {
      return actionBarHeight;
    }
    getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
    actionBarHeight =
        TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
    return actionBarHeight;
  }

  public int getScrollY(AbsListView listView) {
    View c = listView.getChildAt(0);
    if (c == null) {
      return 0;
    }

    int firstVisiblePosition = listView.getFirstVisiblePosition();
    int top = c.getTop();

    int headerHeight = 0;
    if (firstVisiblePosition >= 1) {
      headerHeight = listView.getChildAt(0).getHeight();
    }

    return -top + firstVisiblePosition * c.getHeight() + headerHeight;
  }

  /**
   * Set the alpha value for the action bar title.
   */
  private void setTitleAlpha(float alpha) {
    alphaForegroundColorSpan.setAlpha(alpha);
    spannableString.setSpan(alphaForegroundColorSpan, 0, spannableString.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    getActionBar().setTitle(spannableString);
  }

  /**
   * Standard math clamp method https://en.wikipedia.org/wiki/Clamping_(graphics)
   *
   * @param value value to clamp
   * @param max maximum to return
   * @param min minimum to return
   * @return min if value < min, max if value > max, else value
   */
  public static float clamp(float value, float max, float min) {
    return Math.max(Math.min(value, min), max);
  }

  /**
   * Interpolate between two views.
   * This will animate view1 to somewhere between view1 and view2 dependening on the interpolation
   * value.
   * Used to translate the logo to the action bar icon.
   *
   * @param interpolation 'progress' of the interpolation
   */
  private void interpolate(View view1, View view2, float interpolation) {
    getOnScreenRect(rectF1, view1);
    getOnScreenRect(rectF2, view2);

    float scaleX = 1.0F + interpolation * (rectF2.width() / rectF1.width() - 1.0F);
    float scaleY = 1.0F + interpolation * (rectF2.height() / rectF1.height() - 1.0F);
    float translationX =
        0.5F * (interpolation * (rectF2.left + rectF2.right - rectF1.left - rectF1.right));
    float translationY =
        0.5F * (interpolation * (rectF2.top + rectF2.bottom - rectF1.top - rectF1.bottom));

    view1.setTranslationX(translationX);
    view1.setTranslationY(translationY - movieHeader.getTranslationY());
    view1.setScaleX(scaleX);
    view1.setScaleY(scaleY);
  }

  /**
   * Get the position of the view into the given RectF.
   */
  private RectF getOnScreenRect(RectF rect, View view) {
    rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    return rect;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_movie, menu);
    movieShareActionProvider =
        (ShareActionProvider) menu.findItem(R.id.menu_share).getActionProvider();
    return true;
  }

  private void setShareIntent(TMDbMovie movie) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    if (!Strings.isBlank(movie.getHomepage())) {
      intent.setData(Uri.parse(movie.getHomepage()));
    } else {
      intent.setData(Uri.parse("http://www.imdb.com/title/" + movie.getImdbId()));
    }
    if (movieShareActionProvider != null) {
      movieShareActionProvider.setShareIntent(intent);
    }
  }

  @Subscribe public void onMovieClicked(Events.OnMovieClickedEvent event) {
    startActivity(createIntent(this, event));
  }

  @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
    // ignore
  }

  @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    int scrollY = getScrollY(view);
    movieHeader.setTranslationY(Math.max(-scrollY, minHeaderTranslation));
    float ratio = clamp(movieHeader.getTranslationY() / minHeaderTranslation, 0.0f, 1.0f);
    interpolate(moviePoster, actionBarIconView, smoothInterpolator.getInterpolation(ratio));
    float alpha = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
    setTitleAlpha(alpha);
  }
}