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

import android.auto.value.AutoValue;
import android.graphics.Bitmap;
import android.os.Parcelable;

@AutoValue
public abstract class ColorScheme implements Parcelable {
  public abstract int getPrimaryAccent();

  public abstract int getSecondaryAccent();

  public abstract int getTertiaryAccent();

  public abstract int getPrimaryText();

  public abstract int getSecondaryText();

  public static ColorScheme fromBitmap(Bitmap bitmap) {
    return new DominantColorCalculator(bitmap).getColorScheme();
  }

  public static ColorScheme create(int primaryAccent, int secondaryAccent, int tertiaryAccent,
      int primaryText, int secondaryText) {
    return new AutoValue_ColorScheme(primaryAccent, secondaryAccent, tertiaryAccent, primaryText,
        secondaryText);
  }
}
