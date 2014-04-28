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

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import butterknife.OnClick;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.ui.fragments.PopularMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.WantedMoviesFragment;
import com.squareup.otto.Subscribe;

public class MainActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Start in Popular
    showPopularMovies();
  }

  @Override protected void inflateLayout(ViewGroup container) {
    getLayoutInflater().inflate(R.layout.activity_main, container);
  }

  @OnClick(R.id.tmdb_popular) public void showPopularMovies() {
    getFragmentManager().beginTransaction()
        .replace(R.id.content, new PopularMoviesFragment())
        .commit();
  }

  @OnClick(R.id.couchpotato_library) public void showLibrary() {
    getFragmentManager().beginTransaction()
        .replace(R.id.content, new WantedMoviesFragment())
        .commit();
  }

  @OnClick(R.id.couchpotato_login) public void onLoginClicked() {
    Intent intent = new Intent(this, CouchPotatoLoginActivity.class);
    startActivity(intent);
  }

  @Subscribe public void onMovieClicked(Events.OnMovieClickedEvent event) {
    int orientation = getResources().getConfiguration().orientation;

    startActivity(MovieActivity.createIntent(this, event.movie, event.left, event.top, event.width,
            event.height, orientation)
    );

    // Override transitions: we don't want the normal window animations
    overridePendingTransition(0, 0);
  }
}
