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

package com.f2prateek.couchpotato.ui.fragments;

import android.os.Bundle;
import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoDatabase;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;

public class WantedMoviesFragment extends MoviesGridFragment {

  @Inject CouchPotatoDatabase database;

  private Subscription request;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    request = database.getMovies(new EndlessObserver<List<Movie>>() {
      @Override public void onNext(List<Movie> movies) {
        adapter.add(movies);
        root.setDisplayedChildView(grid);
      }
    });
  }

  @Override public void onPause() {
    super.onPause();
    request.unsubscribe();
  }
}
