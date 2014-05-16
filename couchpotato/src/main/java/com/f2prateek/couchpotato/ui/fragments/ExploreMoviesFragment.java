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

package com.f2prateek.couchpotato.ui.fragments;

import android.os.Bundle;
import android.widget.AbsListView;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.TMDbDatabase;
import com.f2prateek.ln.Ln;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import rx.Observer;
import rx.Subscription;

/**
 * A base fragment for exploring movies from TMDB.
 */
public abstract class ExploreMoviesFragment extends MoviesGridFragment
    implements Observer<List<Movie>> {
  private static final int LOAD_THRESHOLD = 4;
  private final AtomicBoolean loading = new AtomicBoolean(false);
  private final AtomicInteger page = new AtomicInteger(1);

  @Inject TMDbDatabase database;

  private Subscription request;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    fetch();

    grid.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
        // ignore
      }

      @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        if (adapter.getCount() == 0) return; // No items

        int lastVisibleItem = visibleItemCount + firstVisibleItem;
        if (lastVisibleItem >= totalItemCount - LOAD_THRESHOLD) {
          fetch();
        }
      }
    });
  }

  private void fetch() {
    if (loading.get()) return;
    loading.set(true);
    request = subscribe(page.getAndIncrement());
  }

  @Override public void onPause() {
    super.onPause();
    if (request != null) request.unsubscribe();
  }

  protected abstract Subscription subscribe(int page);

  @Override public void onNext(List<Movie> movies) {
    adapter.add(movies);
    root.setDisplayedChildView(grid);
    loading.set(false);
  }

  @Override public void onCompleted() {
    // ignore
  }

  @Override public void onError(Throwable e) {
    Ln.d(e);
  }
}
