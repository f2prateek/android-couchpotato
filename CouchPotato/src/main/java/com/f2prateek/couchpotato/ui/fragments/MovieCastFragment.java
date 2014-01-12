/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.f2prateek.couchpotato.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.moviedb.Casts;
import com.f2prateek.couchpotato.model.moviedb.MovieDbConfiguration;
import com.f2prateek.couchpotato.ui.util.BindingListAdapter;
import com.f2prateek.couchpotato.ui.widgets.AvatarTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Fragment to display the CouchPotatoMovie's cast, writers, directors, etc.
 * Rather than use a specific MovieBean, supply the info directly.
 */
public class MovieCastFragment extends BaseFragment {

  @InjectView(R.id.movie_people_list) ListView movieCast;
  @Inject Provider<MovieDbConfiguration> configurationProvider;
  private ArrayList<Casts.Cast> cast;

  /** Create a new instance of MovieCastFragment */
  public static MovieCastFragment newInstance(Gson gson, ArrayList<Casts.Cast> cast) {
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
    return inflater.inflate(R.layout.fragment_movie_people, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    movieCast.setAdapter(new CastAdapter(activityContext, cast));
  }

  class CastAdapter extends BindingListAdapter<Casts.Cast> {

    private final StyleSpan boldSpan = new StyleSpan(android.graphics.Typeface.BOLD);

    public CastAdapter(Context context, List<Casts.Cast> list) {
      super(context, list);
    }

    @Override public void bindView(Casts.Cast actor, View view) {
      final AvatarTextView textView = (AvatarTextView) view;
      textView.setText(getText(activityContext.getString(R.string.movie_cast_name_format), actor));
      String imageUrl = actor.getImage(configurationProvider.get());
      Picasso.with(activityContext).load(imageUrl).into(textView);
    }

    @Override public View newView(LayoutInflater inflater, int type, ViewGroup parent) {
      return LayoutInflater.from(activityContext).inflate(R.layout.item_movie_people, null);
    }

    private Spannable getText(String format, Casts.Cast actor) {
      String text = String.format(format, actor.name, actor.character);
      SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
      spannableStringBuilder.setSpan(boldSpan, text.indexOf(actor.name), actor.name.length(),
          Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      return spannableStringBuilder;
    }
  }
}