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

package com.f2prateek.couchpotato.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class AvatarTextView extends TextView implements Target {
  public AvatarTextView(Context context) {
    super(context);
  }

  public AvatarTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AvatarTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
    setCompoundDrawablesWithIntrinsicBounds(new RoundedAvatarDrawable(bitmap), null, null, null);
  }

  @Override public void onBitmapFailed(Drawable drawable) {

  }

  @Override public void onPrepareLoad(Drawable drawable) {

  }
}
