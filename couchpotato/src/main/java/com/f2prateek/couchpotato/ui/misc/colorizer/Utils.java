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

package com.f2prateek.couchpotato.ui.misc.colorizer;

import android.graphics.Color;

class Utils {
  static int darken(final int color, float fraction) {
    return blendColors(Color.BLACK, color, fraction);
  }

  static int lighten(final int color, float fraction) {
    return blendColors(Color.WHITE, color, fraction);
  }

  /**
   * @return luma value according to to YIQ color space.
   */
  static final int calculateYiqLuma(int color) {
    return Math.round(
        (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000f);
  }

  /**
   * Blend {@code color1} and {@code color2} using the given ratio.
   *
   * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
   * 0.0 will return {@code color2}.
   */
  static int blendColors(int color1, int color2, float ratio) {
    final float inverseRatio = 1f - ratio;
    float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRatio);
    float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRatio);
    float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRatio);
    return Color.rgb((int) r, (int) g, (int) b);
  }

  static int changeBrightness(final int color, float fraction) {
    return calculateYiqLuma(color) >= 128 ? darken(color, fraction) : lighten(color, fraction);
  }

  static int calculateContrast(MedianCutQuantizer.ColorNode color1,
      MedianCutQuantizer.ColorNode color2) {
    return Math.abs(calculateYiqLuma(color1.getRgb()) - calculateYiqLuma(color2.getRgb()));
  }

  static float calculateColorfulness(MedianCutQuantizer.ColorNode node) {
    float[] hsv = node.getHsv();
    return hsv[1] * hsv[2];
  }

  static float weightedAverage(float... values) {
    if (values.length % 2 != 0) {
      throw new IllegalArgumentException("values length must be two.");
    }

    float sum = 0;
    float sumWeight = 0;

    for (int i = 0; i < values.length; i += 2) {
      float value = values[i];
      float weight = values[i + 1];

      sum += (value * weight);
      sumWeight += weight;
    }

    return sum / sumWeight;
  }
}
