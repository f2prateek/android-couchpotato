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
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.TMDbDatabase;
import com.f2prateek.couchpotato.data.api.tmdb.model.Cast;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCreditsResponse;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.misc.BindableListAdapter;
import com.f2prateek.couchpotato.ui.views.MovieCrewItem;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class MovieCastInfoFragment extends MovieInfoGridFragment {
  @Inject TMDbDatabase tmDbDatabase;
  @Inject Picasso picasso;
  @Inject @ForActivity Context activityContext;

  @Override protected void fetch(Movie minifiedMovie) {
    subscribe(tmDbDatabase.getMovieCredits(minifiedMovie.id()),
        new EndlessObserver<MovieCreditsResponse>() {
          @Override public void onNext(MovieCreditsResponse credits) {
            if (!CollectionUtils.isNullOrEmpty(credits.getCasts())) {
              MovieCastAdapter adapter = new MovieCastAdapter(activityContext, picasso);
              adapter.replaceWith(credits.getCasts());
              setAdapter(adapter);
            }
          }
        }
    );
  }

  static class MovieCastAdapter extends BindableListAdapter<Cast> {
    private final Picasso picasso;

    public MovieCastAdapter(Context context, Picasso picasso) {
      super(context);
      this.picasso = picasso;
    }

    @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
      return inflater.inflate(R.layout.movie_crew_item, container, false);
    }

    @Override public void bindView(Cast item, int position, View view) {
      ((MovieCrewItem) view).bindTo(item, picasso);
    }
  }
}
