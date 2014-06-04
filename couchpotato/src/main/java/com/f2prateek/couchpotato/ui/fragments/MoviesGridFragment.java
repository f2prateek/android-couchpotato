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
import com.f2prateek.couchpotato.ui.views.MovieGridAdapter;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

/**
 * A base fragment for displaying a grid of movies.
 */
public abstract class MoviesGridFragment extends BaseGridFragment {
  protected MovieGridAdapter adapter;
  @Inject Picasso picasso;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    adapter = new MovieGridAdapter(activityContext, picasso, bus);
    setAdapter(adapter);
  }
}
