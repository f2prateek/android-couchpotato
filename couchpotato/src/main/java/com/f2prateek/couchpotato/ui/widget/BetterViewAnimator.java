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

package com.f2prateek.couchpotato.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewAnimator;

/**
 * A {@link android.widget.ViewAnimator} which looks up children by id instead of position.
 */
public class BetterViewAnimator extends ViewAnimator {
  public BetterViewAnimator(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Get the view id of the displayed view.
   */
  public int getDisplayedChildId() {
    return getChildAt(getDisplayedChild()).getId();
  }

  /**
   * Sets which child view will be displayed.
   *
   * @param id the resource id of the child view to display
   */
  public void setDisplayedChildId(int id) {
    if (getDisplayedChildId() == id) {
      return;
    }
    for (int i = 0, count = getChildCount(); i < count; i++) {
      if (getChildAt(i).getId() == id) {
        setDisplayedChild(i);
        return;
      }
    }
    throw new IllegalArgumentException("No view with ID " + id);
  }

  /**
   * Sets which child view will be displayed.
   *
   * @param view the view to display
   */
  public void setDisplayedChild(View view) {
    setDisplayedChildId(view.getId());
  }
}
