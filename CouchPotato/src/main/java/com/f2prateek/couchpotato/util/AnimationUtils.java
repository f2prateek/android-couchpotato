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

package com.f2prateek.couchpotato.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class AnimationUtils {

  /**
   * Crossfade between two views.
   *
   * @param crossfadeIn View that will fade into visibility
   * @param crossfadeOut View that will fade out of visibility
   */
  public static void crossfadeViews(final View crossfadeIn, final View crossfadeOut, int duration) {
    fadeIn(crossfadeIn, duration);
    fadeOut(crossfadeOut, 0f, duration);
  }

  /** Fade in the given view. */
  public static void fadeIn(final View view, int duration) {
    view.setAlpha(0f);
    view.setVisibility(View.VISIBLE);

    // Animate the content view to 100% opacity, and clear any animation
    // listener set on the view.
    view.animate().withLayer().alpha(1f).setDuration(duration).setListener(null);
  }

  /** Fade out the given view. */
  public static void fadeOut(final View view, float opacity, int duration) {
    // Animate the loading view to the given opacity.

    AnimatorListenerAdapter animatorListenerAdapter = null;
    if (opacity == 0f) {
      // After the animation ends, set its visibility to GONE as an optimization step
      // (it won't participate in layout passes, etc.)
      animatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          view.setVisibility(View.GONE);
        }
      };
    }
    view.animate()
        .withLayer()
        .alpha(opacity)
        .setDuration(duration)
        .setListener(animatorListenerAdapter);
  }
}
