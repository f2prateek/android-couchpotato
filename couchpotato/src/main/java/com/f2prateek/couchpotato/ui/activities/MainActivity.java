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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.InjectView;
import com.astuetz.PagerSlidingTabStrip;
import com.f2prateek.couchpotato.Events;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoEndpoint;
import com.f2prateek.couchpotato.data.prefs.BooleanPreference;
import com.f2prateek.couchpotato.data.prefs.FirstRun;
import com.f2prateek.couchpotato.ui.colorizer.FragmentTabAdapter;
import com.f2prateek.couchpotato.ui.fragments.DiscoverMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.NowPlayingMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.PopularMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.TopRatedMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.UpcomingMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.WantedMoviesFragment;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;

public class MainActivity extends BaseActivity {
  @InjectView(R.id.pager) ViewPager pager;
  @InjectView(R.id.tabs) PagerSlidingTabStrip tabStrip;

  @Inject CouchPotatoEndpoint couchPotatoEndpoint;
  @Inject @FirstRun BooleanPreference firstRun;

  FragmentTabAdapter tabAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getActionBar().setCustomView(R.layout.view_server_settings_action_bar);

    inflateLayout(R.layout.activity_main);

    tabAdapter = new FragmentTabAdapter(this, pager);
    pager.setAdapter(tabAdapter);

    tabAdapter.addTab(WantedMoviesFragment.class, null, R.string.library);
    tabAdapter.addTab(PopularMoviesFragment.class, null, R.string.popular);
    tabAdapter.addTab(TopRatedMoviesFragment.class, null, R.string.top_rated);
    tabAdapter.addTab(NowPlayingMoviesFragment.class, null, R.string.now_playing);
    tabAdapter.addTab(UpcomingMoviesFragment.class, null, R.string.upcoming);
    tabAdapter.addTab(DiscoverMoviesFragment.class, null, R.string.discover);

    // Can't be configured via xml so done here!
    tabStrip.setTextColor(getResources().getColor(R.color.white));
    tabStrip.setViewPager(pager);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_server_settings:
        Intent intent = new Intent(this, CouchPotatoServerSettingsActivity.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
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
