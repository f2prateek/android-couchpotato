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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.tmdb.model.MinifiedMovie;
import com.f2prateek.ln.Ln;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

/**
 * Same as {@link MovieGridItem}, but measured by height, not
 * width.
 */
public class MovieScrollItem extends RelativeLayout {
  @InjectView(R.id.movie_poster) ImageView image;
  @InjectView(R.id.movie_title) TextView title;

  @Inject Bus bus;
  @Inject Picasso picasso;

  private MinifiedMovie movie;

  public MovieScrollItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    CouchPotatoApplication.get(context).inject(this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    Ln.d("Inflating MovieScrollItem");
    ButterKnife.inject(this);
  }

  public void bindTo(MinifiedMovie movie) {
    this.movie = movie;
    picasso.load(movie.getPosterPath()).fit().centerCrop().into(image);
    title.setText(movie.getTitle());
  }

  @OnClick(R.id.movie_poster) public void onImageClicked(View view) {
    // We can't simply start the animation from here since we need to be able to override the
    // normal activity animations
    int[] screenLocation = new int[2];
    view.getLocationOnScreen(screenLocation);

    int width = view.getWidth();
    int height = view.getHeight();

    bus.post(
        new Events.OnMovieClickedEvent(movie, height, width, screenLocation[0], screenLocation[1]));
  }
}
