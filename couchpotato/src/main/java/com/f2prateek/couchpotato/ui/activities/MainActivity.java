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

package com.f2prateek.couchpotato.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoEndpoint;
import com.f2prateek.couchpotato.data.prefs.BooleanPreference;
import com.f2prateek.couchpotato.data.prefs.FirstRun;
import com.f2prateek.couchpotato.ui.ButterKnives;
import com.f2prateek.couchpotato.ui.fragments.NowPlayingMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.PopularMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.TopRatedMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.UpcomingMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.WantedMoviesFragment;
import com.squareup.otto.Subscribe;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends BaseActivity {
  @InjectViews(R.id.couchpotato_library) List<View> authenticatedActions;
  @InjectView(R.id.navigation_drawer_layout) DrawerLayout drawerLayout;

  @Inject CouchPotatoEndpoint couchPotatoEndpoint;
  @Inject @FirstRun BooleanPreference firstRun;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inflateLayout(R.layout.activity_main);

    if (!couchPotatoEndpoint.isSet()) {
      ButterKnives.hide(authenticatedActions);
    }

    showPopularMovies();

    if (!firstRun.get()) {
      drawerLayout.postDelayed(new Runnable() {
        @Override public void run() {
          drawerLayout.openDrawer(Gravity.START);
        }
      }, 1000);
      firstRun.set(true);
    }
  }

  @OnClick(R.id.tmdb_popular) public void showPopularMovies() {
    getFragmentManager().beginTransaction()
        .replace(R.id.content, new PopularMoviesFragment())
        .commit();
  }

  @OnClick(R.id.tmdb_top_rated) public void showTopRatedMovies() {
    getFragmentManager().beginTransaction()
        .replace(R.id.content, new TopRatedMoviesFragment())
        .commit();
  }

  @OnClick(R.id.tmdb_now_playing) public void showNowPlayingMovies() {
    getFragmentManager().beginTransaction()
        .replace(R.id.content, new NowPlayingMoviesFragment())
        .commit();
  }

  @OnClick(R.id.tmdb_upcoming) public void showUpcomingMovies() {
    getFragmentManager().beginTransaction()
        .replace(R.id.content, new UpcomingMoviesFragment())
        .commit();
  }

  @OnClick(R.id.couchpotato_library) public void showLibrary() {
    getFragmentManager().beginTransaction()
        .replace(R.id.content, new WantedMoviesFragment())
        .commit();
  }

  @OnClick(R.id.couchpotato_server) public void editCouchPotatoServerSetting() {
    Intent intent = new Intent(this, CouchPotatoServerSettingsActivity.class);
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
