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

package com.f2prateek.couchpotato.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.moviedb.Casts;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Fragment to display the CouchPotatoMovie's cast, writers, directors, etc.
 * Rather than use a specific MovieBean, supply the info directly.
 */
public class MovieCastFragment extends BaseFragment {

  @InjectView(R.id.movie_cast) LinearLayout movie_cast;
  private ArrayList<Casts.Cast> cast;

  /** Create a new instance of MovieCastFragment */
  public static MovieCastFragment newInstance(ArrayList<Casts.Cast> cast) {
    MovieCastFragment f = new MovieCastFragment();
    Bundle args = new Bundle();
    args.putString("cast", gson.toJson(cast));
    f.setArguments(args);
    return f;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Type collectionType = new TypeToken<ArrayList<Casts.Cast>>() {
    }.getType();
    cast = gson.fromJson(getArguments().getString("cast"), collectionType);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movie_cast, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Context context = getActivity();
    for (Casts.Cast actor : cast) {
      movie_cast.addView(getViewForActor(actor.name, context));
    }
  }

  private View getViewForActor(String name, Context context) {
    // TODO : actor images, etc.
    TextView textView = new TextView(context);
    textView.setText(name);
    return textView;
  }
}