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

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.TMDbDatabase;
import com.f2prateek.couchpotato.data.api.moviedb.model.TMDbMovieMinified;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.colorizer.ColorScheme;
import com.f2prateek.couchpotato.ui.misc.AlphaForegroundColorSpan;
import com.f2prateek.couchpotato.ui.widget.KenBurnsView;
import com.f2prateek.couchpotato.ui.widget.NotifyingScrollView;
import com.f2prateek.dart.InjectExtra;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MovieActivity extends BaseActivity
    implements NotifyingScrollView.OnScrollChangedListener {

  private static final String ARGS_MOVIE = "movie";

  @InjectExtra(ARGS_MOVIE) TMDbMovieMinified movie;

  @InjectView(android.R.id.home) ImageView actionBarIconView;
  @InjectView(R.id.movie_header) View movieHeader;
  @InjectView(R.id.movie_header_image) KenBurnsView movieHeaderBackground;
  @InjectView(R.id.movie_header_logo) ImageView movieHeaderLogo;
  @InjectView(R.id.movie_scroll_container) NotifyingScrollView scrollView;

  @InjectView(R.id.movie_primary) FrameLayout moviePrimary;
  @InjectView(R.id.movie_primary_accent) FrameLayout moviePrimaryAccent;
  @InjectView(R.id.movie_secondary) FrameLayout movieSecondary;
  @InjectView(R.id.movie_secondary_accent) FrameLayout movieSecondaryAccent;
  @InjectView(R.id.movie_tertiary_accent) FrameLayout movieTertiaryAccent;

  @Inject TMDbDatabase tmDbDatabase;
  @Inject Picasso picasso;

  private int actionBarTitleColor;
  private int actionBarHeight;
  private int headerHeight;
  private int minHeaderTranslation;
  private AccelerateDecelerateInterpolator smoothInterpolator;
  private RectF rectF1 = new RectF();
  private RectF rectF2 = new RectF();
  private AlphaForegroundColorSpan alphaForegroundColorSpan;
  private SpannableString spannableString;
  private TypedValue typedValue = new TypedValue();

  public static Intent createIntent(Context context, TMDbMovieMinified movie) {
    Intent intent = new Intent(context, MovieActivity.class);
    intent.putExtra(ARGS_MOVIE, movie);
    return intent;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    spannableString = new SpannableString(movie.title);
    picasso.load(movie.poster).fit().centerCrop().into(movieHeaderLogo, new Callback() {
      @Override public void onSuccess() {
        updateColorScheme();
      }

      @Override public void onError() {

      }
    });
    movieHeaderBackground.loadImages(picasso, movie.backdrop, movie.backdrop);

    init();
    setupFancyScroll();
  }

  private void updateColorScheme() {
    Observable.from(picasso)
        .map(new Func1<Picasso, Bitmap>() {
          @Override public Bitmap call(Picasso picasso) {
            try {
              return picasso.load(movie.poster).get();
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
          @Override public void onNext(ColorScheme colorScheme) {
            moviePrimary.setBackgroundColor(colorScheme.getPrimaryText());
            moviePrimaryAccent.setBackgroundColor(colorScheme.getPrimaryAccent());
            movieSecondary.setBackgroundColor(colorScheme.getSecondaryText());
            movieSecondaryAccent.setBackgroundColor(colorScheme.getSecondaryAccent());
            movieTertiaryAccent.setBackgroundColor(colorScheme.getTertiaryAccent());
          }
        });
  }

  @Override protected void inflateLayout(ViewGroup container) {
    getLayoutInflater().inflate(R.layout.activity_movie, container);
  }

  private void init() {
    smoothInterpolator = new AccelerateDecelerateInterpolator();
    headerHeight = getResources().getDimensionPixelSize(R.dimen.movie_header_height);
    minHeaderTranslation = -headerHeight + getActionBarHeight();
    actionBarTitleColor = getResources().getColor(android.R.color.white);
    alphaForegroundColorSpan = new AlphaForegroundColorSpan(actionBarTitleColor);
  }

  private void setupFancyScroll() {
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    // Use a drawable resource so we can have the righ
    actionBar.setIcon(R.drawable.ic_transparent);
    actionBar.setTitle(spannableString);
    setTitleAlpha(0);

    scrollView.setOnScrollChangedListener(this);
  }

  public int getActionBarHeight() {
    if (actionBarHeight != 0) return actionBarHeight;

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
   * This will animate view1 to somewhere between view1 and view2 depending on the interpolation
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

  @Override public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
    int scrollY = who.getScrollY();
    movieHeader.setTranslationY(Math.max(-scrollY, minHeaderTranslation));
    float ratio = clamp(movieHeader.getTranslationY() / minHeaderTranslation, 0.0f, 1.0f);
    interpolate(movieHeaderLogo, actionBarIconView, smoothInterpolator.getInterpolation(ratio));
    float alpha = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
    setTitleAlpha(alpha);
  }
}
