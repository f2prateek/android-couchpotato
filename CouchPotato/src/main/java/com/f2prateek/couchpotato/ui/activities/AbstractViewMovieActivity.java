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

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.CouchPotatoApi;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.ui.base.BaseAuthenticatedActivity;
import com.f2prateek.couchpotato.ui.widgets.NotifyingScrollView;
import com.google.common.base.Joiner;
import java.util.List;
import javax.inject.Inject;

/**
 * Encapsulate common functionality for {@link ViewMovieActivity}
 * and {@link com.f2prateek.couchpotato.ui.activities.ViewSearchMovieActivity}.
 */
public abstract class AbstractViewMovieActivity extends BaseAuthenticatedActivity {

  public static final String MOVIE_KEY = "com.f2prateek.couchpotato.MOVIE_KEY";
  private final Joiner commaJoiner = Joiner.on(", ");

  @Inject CouchPotatoApi couchPotatoApi;

  @InjectView(R.id.scroll_view) NotifyingScrollView notifyingScrollView;
  @InjectView(R.id.movie_backdrop) ImageView movie_backdrop;
  @InjectView(R.id.movie_tagline) TextView movie_tagline;
  @InjectView(R.id.movie_plot) TextView movie_plot;
  @InjectView(R.id.movie_genres) TextView movie_genres;
  @InjectView(R.id.movie_cast) TextView movie_cast;
  @InjectView(R.id.movie_directors) TextView movie_directors;
  @InjectView(R.id.movie_writers) TextView movie_writers;

  private Drawable actionBarBackgroundDrawable;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setDisplayShowHomeEnabled(false);
    setContentView(R.layout.activity_movie);
    setUpFancyScroll(getResources().getColor(R.color.ab_color));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.abstract_movie_activity, menu);
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
    }
    return super.onOptionsItemSelected(item);
  }

  public abstract void openImdbPage();

  public abstract void playTrailer();

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

  protected void writeToTextView(TextView textView, List<String> text) {
    if (text != null && text.size() > 0) {
      textView.setText(commaJoiner.join(text));
    } else {
      textView.setText(R.string.info_not_available);
    }
  }

  protected Drawable.Callback drawableCallback = new Drawable.Callback() {
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

  public void openUrl(String url) {
    Intent imdb = new Intent(Intent.ACTION_VIEW);
    imdb.setData(Uri.parse(url));
    startActivity(imdb);
  }
}
