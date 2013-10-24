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

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * A special variation of RelativeLayout that can be used as a checkable object.
 * This allows it to be used as the top-level view of a list view item, which
 * also supports checking.  Otherwise, it works identically to a RelativeLayout.
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
  private boolean mChecked;
  private final Drawable checkedDrawable;

  public CheckableRelativeLayout(Context context) {
    super(context);
    checkedDrawable = context.getResources().getDrawable(R.color.holo_blue_light);
  }

  public CheckableRelativeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    checkedDrawable = context.getResources().getDrawable(R.color.holo_blue_light);
  }

  public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    checkedDrawable = context.getResources().getDrawable(R.color.holo_blue_light);
  }

  public void setChecked(boolean checked) {
    mChecked = checked;
    setBackgroundDrawable(checked ? checkedDrawable : null);
  }

  public boolean isChecked() {
    return mChecked;
  }

  public void toggle() {
    setChecked(!mChecked);
  }
}