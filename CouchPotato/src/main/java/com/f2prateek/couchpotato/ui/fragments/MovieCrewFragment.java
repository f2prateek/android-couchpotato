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

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.model.moviedb.Casts;
import com.f2prateek.couchpotato.model.moviedb.Configuration;
import com.f2prateek.couchpotato.ui.widgets.RoundedAvatarDrawable;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Fragment to display the CouchPotatoMovie's cast, writers, directors, etc.
 * Rather than use a specific MovieBean, supply the info directly.
 */
public class MovieCrewFragment extends BaseFragment {

  @InjectView(R.id.movie_cast_and_crew) LinearLayout movie_cast;
  @Inject Provider<Configuration> configurationProvider;
  private ArrayList<Casts.Crew> crew;
  private static final StyleSpan boldSpan = new StyleSpan(android.graphics.Typeface.BOLD);

  /** Create a new instance of MovieCastFragment */
  public static MovieCrewFragment newInstance(ArrayList<Casts.Crew> crew) {
    MovieCrewFragment f = new MovieCrewFragment();
    Bundle args = new Bundle();
    args.putString("crew", gson.toJson(crew));
    f.setArguments(args);
    return f;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Type collectionType = new TypeToken<ArrayList<Casts.Crew>>() {
    }.getType();
    crew = gson.fromJson(getArguments().getString("crew"), collectionType);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_movie_cast_and_crew, container, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    for (Casts.Crew person : crew) {
      movie_cast.addView(getViewForCrew(person));
    }
  }

  private View getViewForCrew(Casts.Crew person) {
    final TextView textView = (TextView) LayoutInflater.from(activityContext)
        .inflate(R.layout.item_movie_cast_and_crew, null);
    textView.setText(getText(activityContext.getString(R.string.movie_crew_name_format), person));
    String imageUrl = person.getImage(configurationProvider.get());
    Picasso.with(activityContext).load(imageUrl).into(new Target() {
      @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
        textView.setCompoundDrawablesWithIntrinsicBounds(new RoundedAvatarDrawable(bitmap), null,
            null, null);
      }

      @Override public void onBitmapFailed(Drawable drawable) {

      }

      @Override public void onPrepareLoad(Drawable drawable) {

      }
    });
    return textView;
  }

  private static Spannable getText(String format, Casts.Crew person) {
    String text = String.format(format, person.name, person.department);
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
    spannableStringBuilder.setSpan(boldSpan, text.indexOf(person.name), person.name.length(),
        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    return spannableStringBuilder;
  }
}