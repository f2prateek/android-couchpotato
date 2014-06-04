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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.TMDbDatabase;
import com.f2prateek.couchpotato.data.api.tmdb.model.TMDbMovie;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.misc.BindableAdapter;
import com.f2prateek.couchpotato.ui.views.MoveInfoItem;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class MovieOverviewInfoFragment extends MovieInfoGridFragment {
  @Inject TMDbDatabase tmDbDatabase;
  @Inject Picasso picasso;
  @Inject @ForActivity Context activityContext;

  @Override public void onViewCreated(View view, Bundle inState) {
    super.onViewCreated(view, inState);
    ((GridView) getCollectionView()).setNumColumns(1);
  }

  @Override protected void fetch(Movie minifiedMovie) {
    subscribe(tmDbDatabase.getMovie(minifiedMovie.id()), new EndlessObserver<TMDbMovie>() {
      @Override public void onNext(TMDbMovie tmDbMovie) {
        MovieInfoAdapter adapter = new MovieInfoAdapter(activityContext, picasso, tmDbMovie);
        setAdapter(adapter);
      }
    });
  }

  static class MovieInfoAdapter extends BindableAdapter {
    private final Picasso picasso;
    private final TMDbMovie movie;

    public MovieInfoAdapter(Context context, Picasso picasso, TMDbMovie movie) {
      super(context);
      this.picasso = picasso;
      this.movie = movie;
    }

    @Override public int getCount() {
      return 1;
    }

    @Override public Object getItem(int position) {
      return null;
    }

    @Override public long getItemId(int position) {
      return 0;
    }

    @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
      switch (position) {
        case 0:
          return inflater.inflate(R.layout.movie_info, container, false);
      }
      return null;
    }

    @Override public void bindView(Object item, int position, View view) {
      switch (position) {
        case 0:
          ((MoveInfoItem) view).bindTo(movie, picasso);
          break;
      }
    }
  }
}
