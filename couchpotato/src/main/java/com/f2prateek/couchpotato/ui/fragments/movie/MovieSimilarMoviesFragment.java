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

package com.f2prateek.couchpotato.ui.fragments.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.TMDbDatabase;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.ScopedBus;
import com.f2prateek.couchpotato.ui.misc.BindableListAdapter;
import com.f2prateek.couchpotato.ui.views.MovieGridItem;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;

public class MovieSimilarMoviesFragment extends MovieInfoGridFragment {
  @Inject TMDbDatabase tmDbDatabase;
  @Inject Picasso picasso;
  @Inject ScopedBus scopedBus;

  @Inject @ForActivity Context activityContext;

  @Override protected void fetch(Movie minifiedMovie) {
    subscribe(tmDbDatabase.getSimilarMovies(minifiedMovie.id()),
        new EndlessObserver<List<Movie>>() {
          @Override public void onNext(List<Movie> movies) {
            if (!CollectionUtils.isNullOrEmpty(movies)) {
              SimilarMoviesAdapter adapter =
                  new SimilarMoviesAdapter(activityContext, picasso, scopedBus);
              adapter.replaceWith(movies);
              setAdapter(adapter);
            }
          }
        }
    );
  }

  static class SimilarMoviesAdapter extends BindableListAdapter<Movie>
      implements MovieGridItem.MovieClickListener {
    private final Picasso picasso;
    private final ScopedBus scopedBus;

    public SimilarMoviesAdapter(Context context, Picasso picasso, ScopedBus scopedBus) {
      super(context);
      this.picasso = picasso;
      this.scopedBus = scopedBus;
    }

    @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
      return inflater.inflate(R.layout.movie_poster_grid_item, container, false);
    }

    @Override public void bindView(Movie item, int position, View view) {
      ((MovieGridItem) view).bindTo(item, picasso, this);
    }

    @Override public void onMovieClicked(Movie movie) {
      scopedBus.post(Events.OnMovieClickedEvent.fromSource(movie));
    }
  }
}
