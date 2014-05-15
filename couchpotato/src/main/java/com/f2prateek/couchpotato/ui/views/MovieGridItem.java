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

package com.f2prateek.couchpotato.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.ui.ScopedBus;
import com.squareup.picasso.Picasso;

public class MovieGridItem extends FrameLayout {
  @InjectView(R.id.movie_poster) ImageView image;
  @InjectView(R.id.movie_title) TextView title;

  private Movie movie;
  private ScopedBus bus;

  public MovieGridItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(Movie movie, Picasso picasso, ScopedBus bus) {
    this.movie = movie;
    this.bus = bus;
    picasso.load(movie.poster()).fit().centerCrop().into(image);
    title.setText(movie.title());
  }

  @OnClick(R.id.movie_poster) public void onMovieClicked() {
    int[] screenLocation = new int[2];
    getLocationOnScreen(screenLocation);

    int width = getWidth();
    int height = getHeight();

    bus.post(
        new Events.OnMovieClickedEvent(movie, height, width, screenLocation[0], screenLocation[1]));
  }
}

