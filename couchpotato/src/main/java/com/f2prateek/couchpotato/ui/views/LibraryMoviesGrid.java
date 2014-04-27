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

package com.f2prateek.couchpotato.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoDatabase;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.widget.BetterViewAnimator;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;

public class LibraryMoviesGrid extends BetterViewAnimator {
  @InjectView(R.id.grid) AbsListView grid;

  @Inject CouchPotatoDatabase database;

  private final MovieGridAdapter adapter;
  private Subscription request;

  public LibraryMoviesGrid(Context context, AttributeSet attrs) {
    super(context, attrs);
    CouchPotatoApplication.get(context).inject(this);
    adapter = new MovieGridAdapter(context);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
    grid.setAdapter(adapter);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    fetch();
  }

  private void fetch() {
    request = database.getMovies(new EndlessObserver<List<Movie>>() {
      @Override public void onNext(List<Movie> movies) {
        adapter.add(movies);
        setDisplayedChildView(grid);
      }
    });
  }

  @Override protected void onDetachedFromWindow() {
    request.unsubscribe();
    super.onDetachedFromWindow();
  }
}
