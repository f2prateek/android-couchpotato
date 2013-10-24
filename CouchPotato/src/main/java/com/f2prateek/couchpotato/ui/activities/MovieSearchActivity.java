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

import android.os.Bundle;
import com.f2prateek.couchpotato.ui.base.BaseAuthenticatedActivity;
import com.f2prateek.couchpotato.ui.fragments.MovieSearchGridFragment;

/** Activity to display {@link com.f2prateek.couchpotato.ui.fragments.MovieSearchGridFragment}. */
public class MovieSearchActivity extends BaseAuthenticatedActivity {
  public static final String SEARCH_QUERY = "com.f2prateek.couchpotato.SEARCH_QUERY";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    if (savedInstanceState != null) {
      return;
    }

    String query = getIntent().getExtras().getString(SEARCH_QUERY).trim();
    getActionBar().setTitle("\"" + query + "\"");

    MovieSearchGridFragment movieSearchGridFragment = MovieSearchGridFragment.newInstance(query);
    getFragmentManager().beginTransaction()
        .add(android.R.id.content, movieSearchGridFragment)
        .commit();
  }
}