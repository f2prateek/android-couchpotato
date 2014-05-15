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

package com.f2prateek.couchpotato.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.f2prateek.couchpotato.R;

/**
 * Maintains an aspect ratio based on either width or height. Disabled by default.
 * Also adds draws a foreground.
 */
public class ForegroundAspectRatioImageView extends ImageView {

  // NOTE: These must be kept in sync with the AspectRatioImageView attributes in attrs.xml.
  public static final int MEASUREMENT_WIDTH = 0;
  public static final int MEASUREMENT_HEIGHT = 1;

  private static final float DEFAULT_ASPECT_RATIO = 1f;
  private static final boolean DEFAULT_ASPECT_RATIO_ENABLED = false;
  private static final int DEFAULT_DOMINANT_MEASUREMENT = MEASUREMENT_WIDTH;

  private float aspectRatio;
  private boolean aspectRatioEnabled;
  private int dominantMeasurement;

  private Drawable foreground;

  public ForegroundAspectRatioImageView(Context context) {
    this(context, null);
  }

  public ForegroundAspectRatioImageView(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.ForegroundAspectRatioImageView);
    Drawable foreground =
        a.getDrawable(R.styleable.ForegroundAspectRatioImageView_android_foreground);
    if (foreground != null) {
      setForeground(foreground);
    }
    aspectRatio =
        a.getFloat(R.styleable.ForegroundAspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATIO);
    aspectRatioEnabled = a.getBoolean(R.styleable.ForegroundAspectRatioImageView_aspectRatioEnabled,
        DEFAULT_ASPECT_RATIO_ENABLED);
    dominantMeasurement = a.getInt(R.styleable.ForegroundAspectRatioImageView_dominantMeasurement,
        DEFAULT_DOMINANT_MEASUREMENT);
    a.recycle();
  }

  /**
   * Supply a drawable resource that is to be rendered on top of all of the child
   * views in the frame layout.
   *
   * @param drawableResId The drawable resource to be drawn on top of the children.
   */
  public void setForegroundResource(int drawableResId) {
    setForeground(getContext().getResources().getDrawable(drawableResId));
  }

  /**
   * Supply a Drawable that is to be rendered on top of all of the child
   * views in the frame layout.
   *
   * @param drawable The Drawable to be drawn on top of the children.
   */
  public void setForeground(Drawable drawable) {
    if (foreground == drawable) {
      return;
    }
    if (foreground != null) {
      foreground.setCallback(null);
      unscheduleDrawable(foreground);
    }

    foreground = drawable;

    if (drawable != null) {
      drawable.setCallback(this);
      if (drawable.isStateful()) {
        drawable.setState(getDrawableState());
      }
    }
    requestLayout();
    invalidate();
  }

  @Override protected boolean verifyDrawable(Drawable who) {
    return super.verifyDrawable(who) || (who == foreground);
  }

  @Override public void jumpDrawablesToCurrentState() {
    super.jumpDrawablesToCurrentState();
    if (foreground != null) foreground.jumpToCurrentState();
  }

  @Override protected void drawableStateChanged() {
    super.drawableStateChanged();
    if (foreground != null && foreground.isStateful()) {
      foreground.setState(getDrawableState());
    }
  }

  private void resetForegroundBounds(int width, int height) {
    foreground.setBounds(0, 0, width, height);
    invalidate();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (foreground != null) resetForegroundBounds(getMeasuredWidth(), getMeasuredHeight());

    if (aspectRatioEnabled) {
      int newWidth;
      int newHeight;
      switch (dominantMeasurement) {
        case MEASUREMENT_WIDTH:
          newWidth = getMeasuredWidth();
          newHeight = (int) (newWidth * aspectRatio);
          break;

        case MEASUREMENT_HEIGHT:
          newHeight = getMeasuredHeight();
          newWidth = (int) (newHeight * aspectRatio);
          break;

        default:
          throw new IllegalStateException("Unknown measurement with ID " + dominantMeasurement);
      }

      setMeasuredDimension(newWidth, newHeight);
    }
  }

  /** Get the aspect ratio for this image view. */
  public float getAspectRatio() {
    return aspectRatio;
  }

  /** Set the aspect ratio for this image view. This will update the view instantly. */
  public void setAspectRatio(float aspectRatio) {
    this.aspectRatio = aspectRatio;
    if (aspectRatioEnabled) {
      requestLayout();
    }
  }

  /** Get whether or not forcing the aspect ratio is enabled. */
  public boolean getAspectRatioEnabled() {
    return aspectRatioEnabled;
  }

  /** set whether or not forcing the aspect ratio is enabled. This will re-layout the view. */
  public void setAspectRatioEnabled(boolean aspectRatioEnabled) {
    this.aspectRatioEnabled = aspectRatioEnabled;
    requestLayout();
  }

  /** Get the dominant measurement for the aspect ratio. */
  public int getDominantMeasurement() {
    return dominantMeasurement;
  }

  /**
   * Set the dominant measurement for the aspect ratio.
   *
   * @see #MEASUREMENT_WIDTH
   * @see #MEASUREMENT_HEIGHT
   */
  public void setDominantMeasurement(int dominantMeasurement) {
    if (dominantMeasurement != MEASUREMENT_HEIGHT && dominantMeasurement != MEASUREMENT_WIDTH) {
      throw new IllegalArgumentException("Invalid measurement type.");
    }
    this.dominantMeasurement = dominantMeasurement;
    requestLayout();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (foreground != null) resetForegroundBounds(w, h);
  }

  @Override public void draw(Canvas canvas) {
    super.draw(canvas);

    if (foreground != null) foreground.draw(canvas);
  }
}
