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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
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
import com.f2prateek.couchpotato.ui.misc.colorizer.FragmentTabAdapter;
import com.f2prateek.couchpotato.ui.widget.KenBurnsView;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.f2prateek.couchpotato.util.Strings;
import com.f2prateek.dart.InjectExtra;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MovieActivity extends BaseActivity {
  public static final String ARGS_MOVIE = "minified_movie";

  @InjectExtra(ARGS_MOVIE) Movie minifiedMovie;

  @InjectView(R.id.movie_header_backdrop) KenBurnsView movieBackdrop;
  @InjectView(R.id.movie_header_poster) ImageView moviePoster;
  @InjectView(R.id.movie_pager_strip) PagerSlidingTabStrip tabStrip;
  @InjectView(R.id.movie_pager) ViewPager pager;

  @Inject Picasso picasso;
  @Inject TMDbDatabase tmDbDatabase;

  FragmentTabAdapter tabAdapter;
  ShareActionProvider movieShareActionProvider;

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

    tabAdapter = new FragmentTabAdapter(this, pager);
    pager.setAdapter(tabAdapter);
    tabAdapter.addTab(MovieCastInfoFragment.class,
        MovieCastInfoFragment.newInstanceArgs(minifiedMovie), R.string.cast);
    tabAdapter.addTab(MovieCrewInfoFragment.class,
        MovieCrewInfoFragment.newInstanceArgs(minifiedMovie), R.string.crew);
    tabStrip.setViewPager(pager);

    picasso.load(minifiedMovie.poster()).fit().centerCrop().into(moviePoster);
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
}