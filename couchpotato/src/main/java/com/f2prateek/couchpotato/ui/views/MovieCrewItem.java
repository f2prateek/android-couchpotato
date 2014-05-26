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

package com.f2prateek.couchpotato.ui.views;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.tmdb.model.Cast;
import com.f2prateek.couchpotato.data.api.tmdb.model.Crew;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;

public class MovieCrewItem extends FrameLayout {
  @InjectView(R.id.crew_profile) ImageView profile;
  @InjectView(R.id.crew_name) TextView name;

  private TypefaceSpan condensedTextSpan;

  public MovieCrewItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);

    condensedTextSpan = new TypefaceSpan("sans-serif-condensed");
  }

  public void bindTo(Cast cast, Picasso picasso) {
    bindTo(cast.getProfilePath(), cast.getName(), cast.getCharacter(), picasso);
  }

  public void bindTo(Crew crew, Picasso picasso) {
    bindTo(crew.getProfilePath(), crew.getName(), crew.getJob(), picasso);
  }

  private void bindTo(String imageUrl, String crew, String role, Picasso picasso) {
    picasso.load(imageUrl).fit().centerCrop().error(R.drawable.ic_launcher).into(profile);

    String displayText = Phrase.from(this, R.string.crew_name_display_format)
        .put("name", crew)
        .put("role", role)
        .format()
        .toString();
    SpannableString spannableString = new SpannableString(displayText);
    spannableString.setSpan(condensedTextSpan, displayText.indexOf(role), displayText.length(),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    name.setText(spannableString);
  }
}
