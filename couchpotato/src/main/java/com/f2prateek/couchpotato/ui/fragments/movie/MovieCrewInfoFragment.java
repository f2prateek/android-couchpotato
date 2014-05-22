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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import butterknife.ButterKnife;
import com.f2prateek.couchpotato.ForActivity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.TMDbDatabase;
import com.f2prateek.couchpotato.data.api.tmdb.model.Crew;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCreditsResponse;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.fragments.BaseGridFragment;
import com.f2prateek.couchpotato.ui.misc.BindableListAdapter;
import com.f2prateek.couchpotato.ui.views.MovieCrewItem;
import com.f2prateek.couchpotato.ui.widget.HeaderGridView;
import com.f2prateek.couchpotato.util.CollectionUtils;
import com.f2prateek.dart.InjectExtra;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

import static com.f2prateek.couchpotato.ui.activities.MovieActivity.ARGS_MOVIE;

public class MovieCrewInfoFragment extends BaseGridFragment {
  @InjectExtra(ARGS_MOVIE) Movie minifiedMovie;

  @Inject TMDbDatabase tmDbDatabase;
  @Inject Picasso picasso;
  @Inject @ForActivity Context activityContext;

  AbsListView.OnScrollListener scrollListener;

  public static Bundle newInstanceArgs(Movie movie) {
    Bundle args = new Bundle();
    args.putParcelable(ARGS_MOVIE, movie);
    return args;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    scrollListener = (AbsListView.OnScrollListener) activity;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    getCollectionView().setOnScrollListener(scrollListener);
    fetch();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_header_grid, container, false);
    HeaderGridView gridView = ButterKnife.findById(root, R.id.collection_view);
    View placeholder = inflater.inflate(R.layout.movie_header_placeholder, gridView, false);
    gridView.addHeaderView(placeholder);
    return root;
  }

  private void fetch() {
    subscribe(tmDbDatabase.getMovieCredits(minifiedMovie.id()),
        new EndlessObserver<MovieCreditsResponse>() {
          @Override public void onNext(MovieCreditsResponse credits) {
            if (!CollectionUtils.isNullOrEmpty(credits.getCrews())) {
              MovieCrewAdapter adapter = new MovieCrewAdapter(activityContext, picasso);
              adapter.replaceWith(credits.getCrews());
              setAdapter(adapter);
            }
          }
        }
    );
  }

  static class MovieCrewAdapter extends BindableListAdapter<Crew> {
    private final Picasso picasso;

    public MovieCrewAdapter(Context context, Picasso picasso) {
      super(context);
      this.picasso = picasso;
    }

    @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
      return inflater.inflate(R.layout.movie_crew_item, container, false);
    }

    @Override public void bindView(Crew item, int position, View view) {
      ((MovieCrewItem) view).bindTo(item, picasso);
    }
  }
}
