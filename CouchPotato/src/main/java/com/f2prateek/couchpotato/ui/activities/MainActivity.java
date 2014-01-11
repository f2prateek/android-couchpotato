/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.ui.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.services.CouchPotatoApi;
import com.f2prateek.couchpotato.ui.fragments.DetailedMovieGridFragment;
import com.f2prateek.couchpotato.ui.fragments.SimpleMovieGridFragment;
import com.f2prateek.couchpotato.ui.widgets.SwipeableActionBarTabsAdapter;
import javax.inject.Inject;

/** The top level activity that is shown first to the user. */
public class MainActivity extends BaseAuthenticatedActivity {

  @InjectView(R.id.pager) ViewPager viewPager;

  private SwipeableActionBarTabsAdapter tabsAdapter;
  private SearchView searchView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupTabs(savedInstanceState != null ? savedInstanceState.getInt("tab", 0) : 0);
  }

  /** Setup the tabs two display our fragments. */
  private void setupTabs(int tab) {
    ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    tabsAdapter = new SwipeableActionBarTabsAdapter(this, viewPager);
    tabsAdapter.addTab(actionBar.newTab().setText(R.string.movie_list_fragment),
        SimpleMovieGridFragment.class, null);
    tabsAdapter.addTab(actionBar.newTab().setText(R.string.movie_list_fragment),
        DetailedMovieGridFragment.class, null);

    actionBar.setSelectedNavigationItem(tab);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_activity, menu);
    MenuItem searchItem = menu.findItem(R.id.movie_search);
    searchView = (SearchView) searchItem.getActionView();
    setupSearchView(searchItem);
    return true;
  }

  private void setupSearchView(MenuItem searchItem) {
    searchItem.setShowAsActionFlags(
        MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        //Intent intent = new Intent(MainActivity.this, MovieSearchActivity.class);
        //intent.putExtra(MovieSearchActivity.SEARCH_QUERY, query);
        //startActivity(intent);
        return true;
      }

      @Override public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_shutdown:
        shutdown();
        return true;
      case R.id.action_restart:
        restart();
        return true;
      case R.id.action_settings:
        openSettings();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /** Open the settings activity. */
  private void openSettings() {
    // TODO : actually open the settings activity.o
    Intent intent = new Intent(MainActivity.this, ServerSetupActivity.class);
    startActivity(intent);
  }

  /** Shutdown the CouchPotato server. */
  private void shutdown() {
    /*
    couchPotatoApi.app_shutdown(new Callback<Response>() {
      @Override public void success(Response appShutdownResponse, Response response) {
        Ln.d("Shutting Down");
        Crouton.makeText(MainActivity.this, R.string.shutting_down, Style.ALERT).show();
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not shutdown server.");
        RetrofitErrorHandler.showError(MainActivity.this, retrofitError);
      }
    });
    */
  }

  /** Restart the CouchPotato server. */
  private void restart() {
    /*
    couchPotatoApi.app_restart(new Callback<Response>() {
      @Override public void success(Response appRestartResponse, Response response) {
        Ln.d("Restarting");
        Crouton.makeText(MainActivity.this, R.string.restarting, Style.ALERT).show();
      }

      @Override public void failure(RetrofitError retrofitError) {
        Ln.e("Could not restart server.");
        RetrofitErrorHandler.showError(MainActivity.this, retrofitError);
      }
    });
    */
  }
}
