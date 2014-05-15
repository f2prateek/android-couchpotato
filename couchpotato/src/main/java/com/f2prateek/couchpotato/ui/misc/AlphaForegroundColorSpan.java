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

package com.f2prateek.couchpotato.ui.misc;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

public class AlphaForegroundColorSpan extends ForegroundColorSpan {

  private float alpha;

  public AlphaForegroundColorSpan(int color) {
    super(color);
  }

  public AlphaForegroundColorSpan(Parcel src) {
    super(src);
    alpha = src.readFloat();
  }

  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeFloat(alpha);
  }

  @Override
  public void updateDrawState(TextPaint ds) {
    ds.setColor(getAlphaColor());
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public float getAlpha() {
    return alpha;
  }

  private int getAlphaColor() {
    int foregroundColor = getForegroundColor();
    return Color.argb((int) (alpha * 255), Color.red(foregroundColor), Color.green(foregroundColor),
        Color.blue(foregroundColor));
  }
}
