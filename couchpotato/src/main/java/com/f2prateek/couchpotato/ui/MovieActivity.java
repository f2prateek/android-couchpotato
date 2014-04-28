/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
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

package com.f2prateek.couchpotato.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Property;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoDatabase;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoEndpoint;
import com.f2prateek.couchpotato.data.api.couchpotato.model.profile.Profile;
import com.f2prateek.couchpotato.data.api.tmdb.TMDbDatabase;
import com.f2prateek.couchpotato.data.api.tmdb.model.Backdrop;
import com.f2prateek.couchpotato.data.api.tmdb.model.Cast;
import com.f2prateek.couchpotato.data.api.tmdb.model.Crew;
import com.f2prateek.couchpotato.data.api.tmdb.model.Images;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCreditsResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.TMDbMovie;
import com.f2prateek.couchpotato.data.api.tmdb.model.Video;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.colorizer.ColorScheme;
import com.f2prateek.couchpotato.ui.misc.AlphaForegroundColorSpan;
import com.f2prateek.couchpotato.ui.views.MovieCrewItem;
import com.f2prateek.couchpotato.ui.views.MovieGridItem;
import com.f2prateek.couchpotato.ui.views.MovieVideoItem;
import com.f2prateek.couchpotato.ui.widget.KenBurnsView;
import com.f2prateek.couchpotato.ui.widget.NotifyingScrollView;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.f2prateek.couchpotato.util.Strings;
import com.f2prateek.dart.InjectExtra;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MovieActivity extends BaseActivity
    implements NotifyingScrollView.OnScrollChangedListener {
  private static final String ARGS_MOVIE = "minified_movie";
  private static final String ARGS_ORIENTATION = "orientation";
  private static final String ARGS_THUMBNAIL_LEFT = "thumbnail_left";
  private static final String ARGS_THUMBNAIL_TOP = "thumbnail_top";
  private static final String ARGS_THUMBNAIL_WIDTH = "thumbnail_width";
  private static final String ARGS_THUMBNAIL_HEIGHT = "thumbnail_height";

  private static final int ANIMATION_DURATION = 500; // 500ms
  private static final int HALF_ANIMATION_DURATION = ANIMATION_DURATION / 2;

  private final TimeInterpolator decelerateInterpolator = new DecelerateInterpolator();
  private final TimeInterpolator accelerateInterpolator = new AccelerateInterpolator();
  private final AccelerateDecelerateInterpolator smoothInterpolator =
      new AccelerateDecelerateInterpolator();

  @InjectExtra(ARGS_MOVIE) Movie minifiedMovie;
  @InjectExtra(ARGS_THUMBNAIL_LEFT) int thumbnailLeft;
  @InjectExtra(ARGS_THUMBNAIL_TOP) int thumbnailTop;
  @InjectExtra(ARGS_THUMBNAIL_WIDTH) int thumbnailWidth;
  @InjectExtra(ARGS_THUMBNAIL_HEIGHT) int thumbnailHeight;
  @InjectExtra(ARGS_ORIENTATION) int originalOrientation;

  @InjectView(android.R.id.home) ImageView actionBarIconView;
  @InjectView(R.id.movie_header) FrameLayout movieHeader;
  @InjectView(R.id.movie_header_gradient) View movieHeaderGradient;
  @InjectView(R.id.movie_header_backdrop) KenBurnsView movieBackdrop;
  @InjectView(R.id.movie_header_poster) ImageView moviePoster;

  @InjectView(R.id.similar_movies_header) View similarMoviesHeader;
  @InjectView(R.id.similar_movies_container) LinearLayout similarMoviesContainer;
  @InjectView(R.id.movie_cast_header) View movieCastHeader;
  @InjectView(R.id.movie_cast_container) LinearLayout movieCastContainer;
  @InjectView(R.id.movie_crew_header) View movieCrewHeader;
  @InjectView(R.id.movie_crew_container) LinearLayout movieCrewContainer;
  @InjectView(R.id.movie_videos_header) View movieVideosHeader;
  @InjectView(R.id.movie_videos_container) LinearLayout movieVideosContainer;
  @InjectView(R.id.movie_scroll_container) NotifyingScrollView scrollView;
  @InjectView(R.id.movie_title) TextView movieTitle;
  @InjectView(R.id.movie_tagline) TextView movieTagline;
  @InjectView(R.id.movie_plot) TextView moviePlot;
  @InjectView(R.id.movie_secondary) FrameLayout movieSecondary;
  @InjectView(R.id.movie_secondary_accent) FrameLayout movieSecondaryAccent;
  @InjectView(R.id.movie_tertiary_accent) FrameLayout movieTertiaryAccent;
  @InjectView(R.id.couchpotato_controller) View couchPotatoController;
  @InjectView(R.id.add) ImageView addMovieButton;
  @InjectView(R.id.share) ImageView shareMovieButton;

  @InjectViews({
      R.id.similar_movies_header, R.id.movie_cast_header, R.id.movie_crew_header,
      R.id.movie_videos_header, R.id.movie_title, R.id.movie_title, R.id.movie_plot,
      R.id.movie_tagline
  }) List<TextView> colorSchemedText;

  @Inject TMDbDatabase tmDbDatabase;
  @Inject Picasso picasso;
  @Inject CouchPotatoEndpoint couchPotatoEndpoint;
  @Inject CouchPotatoDatabase couchPotatoDatabase;

  private int actionBarTitleColor;
  private int actionBarHeight;
  private int minHeaderTranslation;
  private RectF tempRect1 = new RectF();
  private RectF tempRect2 = new RectF();
  private AlphaForegroundColorSpan alphaForegroundColorSpan;
  private SpannableString spannableString;
  private int posterLeftDelta; // distance from left of poster to left of thumbnail
  private int posterTopDelta; // distance from top of poster to top of thumbnail
  private float posterWidthScale; // ratio of poster width to thumbnail width
  private float posterHeightScale; // ratio of poster height to thumbnail width
  private int actionBarGradientColor = Color.BLACK;
  private TMDbMovie movie;

  /** Create an intent to launch this activity. */
  public static Intent createIntent(Context context, Movie movie, int left, int top, int width,
      int height, int orientation) {
    Intent intent = new Intent(context, MovieActivity.class);
    intent.putExtra(ARGS_MOVIE, movie);
    intent.putExtra(ARGS_THUMBNAIL_LEFT, left);
    intent.putExtra(ARGS_THUMBNAIL_TOP, top);
    intent.putExtra(ARGS_THUMBNAIL_WIDTH, width);
    intent.putExtra(ARGS_THUMBNAIL_HEIGHT, height);
    intent.putExtra(ARGS_ORIENTATION, orientation);
    return intent;
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Load the initial data we need for animations
    initialBindData();

    // Set the pivots regardless of whether we run the animation or not
    moviePoster.setPivotX(0);
    moviePoster.setPivotY(0);

    // Only run the animation if we're coming from the parent activity, not if
    // we're recreated automatically by the window manager (e.g., device rotation)
    if (savedInstanceState == null) {
      ViewTreeObserver observer = moviePoster.getViewTreeObserver();
      observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
          moviePoster.getViewTreeObserver().removeOnPreDrawListener(this);
          // Figure out where the thumbnail and full size versions are, relative
          // to the screen and each other, and scale factors to make the large version the same size
          // as the thumbnail
          int[] posterLocation = new int[2];
          moviePoster.getLocationOnScreen(posterLocation);
          posterLeftDelta = thumbnailLeft - posterLocation[0];
          posterTopDelta = thumbnailTop - posterLocation[1];
          posterWidthScale = (float) thumbnailWidth / moviePoster.getWidth();
          posterHeightScale = (float) thumbnailHeight / moviePoster.getHeight();
          runEnterAnimation(new Runnable() {
            @Override public void run() {
              // Defer binding until after animation is done
              init();
              bindMovie(true);
            }
          });
          return true;
        }
      });
    } else {
      // Just scroll up, otherwise the poster view will be out of sync with the scrollView
      // Setting scrollY directly does not work
      ObjectAnimator.ofInt(scrollView, "scrollY", 0).setDuration(HALF_ANIMATION_DURATION).start();
      init();
      bindMovie(false);
    }
  }

  /** Set up views and effects. */
  private void init() {
    int headerHeight = getResources().getDimensionPixelSize(R.dimen.movie_header_height);
    minHeaderTranslation = -headerHeight + getActionBarHeight();
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setIcon(R.drawable.ic_transparent);
    scrollView.setOnScrollChangedListener(this);
    setActionBarTitleColor(getResources().getColor(R.color.white));
  }

  private void setActionBarTitleColor(int color) {
    actionBarTitleColor = color;
    alphaForegroundColorSpan = new AlphaForegroundColorSpan(actionBarTitleColor);
  }

  /**
   * Bind the data needed to run animations. Load the minimum of data needed so we can quickly
   * run the animation. See {@link #bindMovie(boolean)}.
   */
  private void initialBindData() {
    picasso.load(minifiedMovie.poster()).fit().centerCrop().into(moviePoster);
    movieTitle.setText(minifiedMovie.title());
  }

  /** Bind data to the views. Some data might already be bound in {@link #initialBindData()}. */
  private void bindMovie(boolean animate) {
    spannableString = new SpannableString(minifiedMovie.title());
    movieBackdrop.load(picasso, minifiedMovie.backdrop());
    updateColorScheme(animate);

    tmDbDatabase.getMovie(minifiedMovie.id(), new EndlessObserver<TMDbMovie>() {
      @Override public void onNext(TMDbMovie response) {
        movie = response;
        if (couchPotatoEndpoint.isSet()) {
          couchPotatoController.setVisibility(View.VISIBLE);
        }
        if (Strings.isBlank(movie.getTagline())) {
          movieTagline.setVisibility(View.GONE);
        } else {
          movieTagline.setText(movie.getTagline());
        }
        if (Strings.isBlank(movie.getOverview())) {
          moviePlot.setVisibility(View.GONE);
        } else {
          moviePlot.setText(movie.getOverview());
        }
      }
    });
    tmDbDatabase.getMovieImages(minifiedMovie.id(), new EndlessObserver<Images>() {
      @Override public void onNext(Images images) {
        if (!CollectionUtils.isNullOrEmpty(images.getBackdrops())) {
          List<String> backdrops = new ArrayList<>();
          for (Backdrop backdrop : images.getBackdrops()) {
            backdrops.add(backdrop.getFilePath());
          }
          movieBackdrop.update(backdrops);
        }
      }
    });
    tmDbDatabase.getSimilarMovies(minifiedMovie.id(), new EndlessObserver<List<Movie>>() {
          @Override public void onNext(List<Movie> movies) {
            if (!CollectionUtils.isNullOrEmpty(movies)) {
              similarMoviesContainer.setVisibility(View.VISIBLE);
              similarMoviesHeader.setVisibility(View.VISIBLE);
              FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                  getResources().getDimensionPixelOffset(R.dimen.poster_item_width),
                  ViewGroup.LayoutParams.MATCH_PARENT);
              for (Movie movie : movies) {
                MovieGridItem child =
                    (MovieGridItem) getLayoutInflater().inflate(R.layout.grid_movie_item,
                        similarMoviesContainer, false);
                child.setLayoutParams(params);
                similarMoviesContainer.addView(child);
                child.bindTo(movie);
              }
            }
          }
        }
    );
    tmDbDatabase.getVideos(minifiedMovie.id(), new EndlessObserver<List<Video>>() {
          @Override public void onNext(List<Video> videos) {
            if (!CollectionUtils.isNullOrEmpty(videos)) {
              movieVideosContainer.setVisibility(View.VISIBLE);
              movieVideosHeader.setVisibility(View.VISIBLE);
              FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                  getResources().getDimensionPixelOffset(R.dimen.trailer_item_width),
                  ViewGroup.LayoutParams.MATCH_PARENT);
              for (Video video : videos) {
                MovieVideoItem child =
                    (MovieVideoItem) getLayoutInflater().inflate(R.layout.movie_video_item,
                        movieVideosContainer, false);
                child.setLayoutParams(params);
                movieVideosContainer.addView(child);
                child.bindTo(video);
              }
            }
          }
        }
    );
    tmDbDatabase.getMovieCredits(minifiedMovie.id(), new EndlessObserver<MovieCreditsResponse>() {
          @Override public void onNext(MovieCreditsResponse credits) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.poster_item_width),
                ViewGroup.LayoutParams.MATCH_PARENT);
            if (!CollectionUtils.isNullOrEmpty(credits.getCasts())) {
              movieCastContainer.setVisibility(View.VISIBLE);
              movieCastHeader.setVisibility(View.VISIBLE);
              for (Cast cast : credits.getCasts()) {
                MovieCrewItem child =
                    (MovieCrewItem) getLayoutInflater().inflate(R.layout.movie_crew_item,
                        movieCastContainer, false);
                child.setLayoutParams(params);
                movieCastContainer.addView(child);
                child.bindTo(cast);
              }
            }

            if (!CollectionUtils.isNullOrEmpty(credits.getCrews())) {
              movieCrewContainer.setVisibility(View.VISIBLE);
              movieCrewHeader.setVisibility(View.VISIBLE);
              for (Crew crew : credits.getCrews()) {
                MovieCrewItem child =
                    (MovieCrewItem) getLayoutInflater().inflate(R.layout.movie_crew_item,
                        movieCrewContainer, false);
                child.setLayoutParams(params);
                movieCrewContainer.addView(child);
                child.bindTo(crew);
              }
            }
          }
        }
    );
  }

  @OnClick(R.id.add) public void add(final View button) {
    couchPotatoDatabase.getProfiles(new EndlessObserver<List<Profile>>() {
      @Override public void onNext(List<Profile> profiles) {
        PopupMenu popupMenu = new PopupMenu(MovieActivity.this, button);
        for (Profile profile : profiles) {
          popupMenu.getMenu().add(0, profile.getId(), 0, profile.getLabel());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override public boolean onMenuItemClick(MenuItem item) {
            couchPotatoDatabase.addMovie(item.getItemId(), movie.getImdbId(),
                new EndlessObserver<Boolean>() {
                  @Override public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                      Crouton.makeText(MovieActivity.this,
                          getString(R.string.movie_added, minifiedMovie.title()), Style.ALERT)
                          .show();
                    }
                  }
                }
            );
            return true;
          }
        });
      }
    });
  }

  /**
   * Use the minifiedMovie's poster to find a color scheme and update our views accordingly.
   * Don't animate if we're being re-created
   */
  private void updateColorScheme(final boolean animate) {
    Observable.from(minifiedMovie.poster())
        .map(new Func1<String, Bitmap>() {
          @Override public Bitmap call(String url) {
            try {
              return picasso.load(url).get();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        })
        .map(new Func1<Bitmap, ColorScheme>() {
          @Override public ColorScheme call(Bitmap bitmap) {
            return ColorScheme.fromBitmap(bitmap);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new EndlessObserver<ColorScheme>() {
          @Override public void onNext(final ColorScheme colorScheme) {
            final long duration = animate ? ANIMATION_DURATION : 0;
            final int transparent = getResources().getColor(android.R.color.transparent);

            setActionBarTitleColor(colorScheme.getPrimaryText());
            actionBarGradientColor = colorScheme.getPrimaryAccent();
            // Rather than setting the background completely on the window, set it only on the views
            // as needed. This has two advantages - no overdraw, and for horizontal scroll views
            // that don't fill up the width can have a distinct background to indicate a segment
            ButterKnife.apply(colorSchemedText, new ButterKnife.Action<TextView>() {
              @Override public void apply(TextView view, int index) {
                animateTextColor(view, colorScheme.getPrimaryText(), duration);
                animateBackgroundColor(view, transparent, colorScheme.getPrimaryAccent(), duration);
              }
            });

            // todo: only for testing colors, delete these three for later
            animateBackgroundColor(movieSecondary, transparent, colorScheme.getSecondaryText(),
                duration);
            animateBackgroundColor(movieSecondaryAccent, transparent,
                colorScheme.getSecondaryAccent(), duration);
            animateBackgroundColor(movieTertiaryAccent, transparent,
                colorScheme.getTertiaryAccent(), duration);

            animateBackgroundColor(couchPotatoController, transparent,
                colorScheme.getSecondaryAccent(), duration);
          }
        });
  }

  @Override protected void inflateLayout(ViewGroup container) {
    getLayoutInflater().inflate(R.layout.activity_movie, container);
  }

  /**
   * The enter animation scales the picture in from its previous thumbnail
   * size/location, colorizing it in parallel. In parallel, the background of the
   * activity is fading in. When the pictue is in place, the text description
   * drops down.
   */
  public void runEnterAnimation(final Runnable endAction) {
    // Set starting values for properties we're going to animate. These
    // values scale and position the full size version down to the thumbnail
    // size/location, from which we'll animate it back up
    moviePoster.setPivotX(0);
    moviePoster.setPivotY(0);
    moviePoster.setScaleX(posterWidthScale);
    moviePoster.setScaleY(posterHeightScale);
    moviePoster.setTranslationX(posterLeftDelta);
    moviePoster.setTranslationY(posterTopDelta);

    // We'll fade the content in later
    scrollView.setAlpha(0);
    movieBackdrop.setAlpha(0);

    // Animate scale and translation to go from thumbnail to full size
    moviePoster.animate().setDuration(ANIMATION_DURATION).
        scaleX(1).scaleY(1).
        translationX(0).translationY(0).
        setInterpolator(decelerateInterpolator).
        withEndAction(new Runnable() {
          public void run() {
            // Animate the content in after the image animation is done
            scrollView.animate().setDuration(HALF_ANIMATION_DURATION).alpha(1).
                setInterpolator(decelerateInterpolator).withEndAction(endAction);
            movieBackdrop.animate().setDuration(HALF_ANIMATION_DURATION).alpha(1).
                setInterpolator(decelerateInterpolator);
          }
        });
  }

  /**
   * Overriding this method allows us to run our exit animation first, then exiting
   * the activity when it is complete.
   */
  @Override
  public void onBackPressed() {
    runExitAnimation(new Runnable() {
      public void run() {
        finish();
        // override transitions to skip the standard window animations
        overridePendingTransition(0, 0);
      }
    });
  }

  /**
   * The exit animation is basically a reverse of the enter animation, except that if
   * the orientation has changed we simply scale the picture back into the center of
   * the screen.
   *
   * @param endAction This action gets run after the animation completes (this is
   * when we actually switch activities)
   */
  public void runExitAnimation(final Runnable endAction) {
    // No need to set initial values for the reverse animation; the image is at the
    // starting size/location that we want to start from. Just animate to the
    // thumbnail size/location that we retrieved earlier
    // Caveat: Configuration change invalidates thumbnail positions; just animate
    // the scale around the center. Also, fade it out since it won't match up with
    // whatever is actually in the center
    final boolean fadeOut;
    if (getResources().getConfiguration().orientation != originalOrientation) {
      moviePoster.setPivotX(moviePoster.getWidth() / 2);
      moviePoster.setPivotY(moviePoster.getHeight() / 2);
      posterLeftDelta = 0;
      posterTopDelta = 0;
      fadeOut = true;
    } else {
      fadeOut = false;
    }

    // First, slide/fade content out of the way
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.setDuration(HALF_ANIMATION_DURATION);
    animatorSet.setInterpolator(accelerateInterpolator);
    animatorSet.playTogether(ObjectAnimator.ofFloat(movieBackdrop, "alpha", 1.0f, 0.0f),
        ObjectAnimator.ofFloat(scrollView, "alpha", 1.0f, 0.0f),
        ObjectAnimator.ofFloat(movieHeaderGradient, "alpha", 1.0f, 0.0f));

    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        // Animate image back to thumbnail size/location
        moviePoster.animate().setDuration(HALF_ANIMATION_DURATION).
            scaleX(posterWidthScale).scaleY(posterHeightScale).
            translationX(posterLeftDelta).translationY(posterTopDelta).
            withEndAction(endAction);
        if (fadeOut) {
          moviePoster.animate().alpha(0);
        }
      }
    });
    animatorSet.start();
  }

  /**
   * Property to let us animate the text color.
   */
  final Property<TextView, Integer> textColorProperty =
      new Property<TextView, Integer>(Integer.class, "textColor") {
        @Override
        public Integer get(TextView object) {
          return object.getCurrentTextColor();
        }

        @Override
        public void set(TextView object, Integer value) {
          object.setTextColor(value);
        }
      };

  private void animateTextColor(TextView textView, int endColor, long animationDuration) {
    final ObjectAnimator animator = ObjectAnimator.ofInt(textView, textColorProperty, endColor);
    animator.setDuration(animationDuration);
    animator.setEvaluator(new ArgbEvaluator());
    animator.setInterpolator(decelerateInterpolator);
    animator.start();
  }

  // make sure to set a color drawable on the view first
  final Property<View, Integer> backgroundColorProperty =
      new Property<View, Integer>(Integer.class, "backgroundColor") {
        @Override
        public Integer get(View object) {
          return ((ColorDrawable) object.getBackground()).getColor();
        }

        @Override
        public void set(View object, Integer value) {
          object.setBackgroundColor(value);
        }
      };

  private void animateBackgroundColor(View view, int startColor, int endColor,
      long animationDuration) {
    view.setBackgroundColor(startColor);
    final ObjectAnimator animator = ObjectAnimator.ofInt(view, backgroundColorProperty, endColor);
    animator.setDuration(animationDuration);
    animator.setEvaluator(new ArgbEvaluator());
    animator.setInterpolator(decelerateInterpolator);
    animator.start();
  }

  public int getActionBarHeight() {
    // Check if we have it already.
    if (actionBarHeight != 0) return actionBarHeight;

    TypedValue typedValue = new TypedValue();
    getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
    actionBarHeight =
        TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
    return actionBarHeight;
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
   * This will translate source to somewhere between source and destination depending on the
   * interpolation
   * value.
   * Used to translate the logo to the action bar icon.
   *
   * @param interpolation 'progress' of the interpolation
   */
  private void interpolate(View source, View destination, float interpolation) {
    getOnScreenRect(tempRect1, source);
    getOnScreenRect(tempRect2, destination);

    float scaleX = 1.0F + interpolation * (tempRect2.width() / tempRect1.width() - 1.0F);
    float scaleY = 1.0F + interpolation * (tempRect2.height() / tempRect1.height() - 1.0F);
    float translationX = interpolation * (tempRect2.left - tempRect1.left);
    float translationY = interpolation * (tempRect2.top - tempRect1.top);

    source.setTranslationX(translationX);
    source.setTranslationY(translationY);
    source.setScaleX(scaleX);
    source.setScaleY(scaleY);
  }

  /**
   * Get the position of the view into the given RectF.
   */
  private RectF getOnScreenRect(RectF rect, View view) {
    rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    return rect;
  }

  @Override public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
    int scrollY = who.getScrollY();
    movieHeader.setTranslationY(Math.max(-scrollY, minHeaderTranslation));
    float ratio = clamp(movieHeader.getTranslationY() / minHeaderTranslation, 0.0f, 1.0f);
    interpolate(moviePoster, actionBarIconView, smoothInterpolator.getInterpolation(ratio));
    float alpha = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
    setTitleAlpha(alpha);
    setActionBarGradient(alpha);
  }

  private void setActionBarGradient(float alpha) {
    int[] colors = new int[2];
    colors[0] = actionBarGradientColor;
    colors[1] = getResources().getColor(android.R.color.transparent);
    GradientDrawable gradientDrawable =
        new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);

    movieHeaderGradient.setAlpha(alpha);
    movieHeaderGradient.setBackground(gradientDrawable);
  }

  @Subscribe public void onMovieClicked(Events.OnMovieClickedEvent event) {
    int orientation = getResources().getConfiguration().orientation;

    startActivity(MovieActivity.createIntent(this, event.movie, event.left, event.top, event.width,
            event.height, orientation)
    );

    // Override transitions: we don't want the normal window animations
    overridePendingTransition(0, 0);
  }
}
