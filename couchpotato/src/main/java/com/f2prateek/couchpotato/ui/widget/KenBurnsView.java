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

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.f2prateek.couchpotato.R;
import com.squareup.picasso.Picasso;
import java.util.Random;

import static butterknife.ButterKnife.findById;

public class KenBurnsView extends FrameLayout {
  private final Handler handler;
  private ImageView[] imageViews;
  private int activeImageIndex = -1;

  private final Random random = new Random();
  private int swapMs = 10000;
  private int fadeInOutMs = 400;

  private float maxScaleFactor = 1.5F;
  private float minScaleFactor = 1.2F;

  private Runnable mSwapImageRunnable = new Runnable() {
    @Override
    public void run() {
      swapImage();
      handler.postDelayed(mSwapImageRunnable, swapMs - fadeInOutMs * 2);
    }
  };

  public KenBurnsView(Context context) {
    this(context, null);
  }

  public KenBurnsView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    handler = new Handler();
  }

  private void swapImage() {
    if (activeImageIndex == -1) {
      activeImageIndex = 1;
      animate(imageViews[activeImageIndex]);
      return;
    }

    int inactiveIndex = activeImageIndex;
    activeImageIndex = (1 + activeImageIndex) % imageViews.length;

    final ImageView activeImageView = imageViews[activeImageIndex];
    activeImageView.setAlpha(0.0f);
    ImageView inactiveImageView = imageViews[inactiveIndex];

    animate(activeImageView);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.setDuration(fadeInOutMs);
    animatorSet.playTogether(ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
        ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f));
    animatorSet.start();
  }

  private void start(View view, long duration, float fromScale, float toScale,
      float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
    view.setScaleX(fromScale);
    view.setScaleY(fromScale);
    view.setTranslationX(fromTranslationX);
    view.setTranslationY(fromTranslationY);
    ViewPropertyAnimator propertyAnimator = view.animate()
        .translationX(toTranslationX)
        .translationY(toTranslationY)
        .scaleX(toScale)
        .scaleY(toScale)
        .setDuration(duration);
    propertyAnimator.start();
  }

  private float pickScale() {
    return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor
        - this.minScaleFactor);
  }

  private float pickTranslation(int value, float ratio) {
    return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
  }

  public void animate(View view) {
    float fromScale = pickScale();
    float toScale = pickScale();
    float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
    float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
    float toTranslationX = pickTranslation(view.getWidth(), toScale);
    float toTranslationY = pickTranslation(view.getHeight(), toScale);
    start(view, this.swapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX,
        toTranslationY);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    startKenBurnsAnimation();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    handler.removeCallbacks(mSwapImageRunnable);
  }

  private void startKenBurnsAnimation() {
    handler.post(mSwapImageRunnable);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    inflate(getContext(), R.layout.kenburns_view, this);
    imageViews = new ImageView[2];
    imageViews[0] = findById(this, R.id.image0);
    imageViews[1] = findById(this, R.id.image1);
  }

  public void loadImages(Picasso picasso, String image1, String image2) {
    picasso.load(image1).fit().centerCrop().into(imageViews[0]);
    picasso.load(image2).fit().centerCrop().into(imageViews[1]);
  }
}
