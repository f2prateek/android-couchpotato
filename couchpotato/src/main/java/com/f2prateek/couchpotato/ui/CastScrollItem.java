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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.tmdb.model.Cast;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

/**
 * Same as {@link com.f2prateek.couchpotato.ui.MovieGridItem}, but measured by height, not
 * width.
 */
public class CastScrollItem extends RelativeLayout {
  @InjectView(R.id.cast_image) ImageView image;
  @InjectView(R.id.cast_name) TextView name;

  @Inject Picasso picasso;

  public CastScrollItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    CouchPotatoApplication.get(context).inject(this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(Cast cast) {
    picasso.load(cast.getProfilePath()).fit().centerCrop().into(image);
    name.setText(cast.getName());
  }
}
