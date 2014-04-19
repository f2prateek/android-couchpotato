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
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KenBurnsView extends FrameLayout {

  private static final int SWAP_DURATION = 10000;
  private static final int FADE_DURATION = 500;
  private static final float MAX_SCALE_FACTOR = 1.5F;
  private static final float MIN_SCALE_FACTOR = 1.2F;

  private final Random random = new Random();
  private final Handler handler = new Handler();

  @InjectView(R.id.image0) ImageView activeImageView;
  @InjectView(R.id.image1) ImageView inactiveImageView;

  private List<String> images;
  private int currentImageIndex = 0;

  private Picasso picasso;

  private Runnable swapImageRunnable = new Runnable() {
    @Override
    public void run() {
      swapImage();
      handler.postDelayed(this, SWAP_DURATION - FADE_DURATION * 2);
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
  }

  private void swapImage() {
    // Swap our active and inactive image views for transition
    final ImageView swapper = inactiveImageView;
    inactiveImageView = activeImageView;
    activeImageView = swapper;

    // Load the current image
    activeImageView.bringToFront();
    loadImage(activeImageView, images.get(currentImageIndex));
    currentImageIndex = (1 + currentImageIndex) % images.size();

    // Animate the active ImageView
    animate(activeImageView);

    // Fade in the active ImageView and fade out the inactive ImageView
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.setDuration(FADE_DURATION);
    animatorSet.playTogether(ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
        ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f));
    animatorSet.start();
  }

  /**
   * Triggers the KenBurns Animation.
   */
  public void animate(View view) {
    float fromScale = pickScale();
    float toScale = pickScale();
    float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
    float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
    float toTranslationX = pickTranslation(view.getWidth(), toScale);
    float toTranslationY = pickTranslation(view.getHeight(), toScale);
    start(view, SWAP_DURATION, fromScale, toScale, fromTranslationX, fromTranslationY,
        toTranslationX, toTranslationY);
  }

  private float pickScale() {
    return MIN_SCALE_FACTOR + random.nextFloat() * (MAX_SCALE_FACTOR - MIN_SCALE_FACTOR);
  }

  private float pickTranslation(int value, float ratio) {
    return value * (ratio - 1.0f) * (random.nextFloat() - 0.5f);
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

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    handler.removeCallbacks(swapImageRunnable);
  }

  private void startKenBurnsAnimation() {
    handler.post(swapImageRunnable);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    inflate(getContext(), R.layout.kenburns_view, this);
    ButterKnife.inject(this);
  }

  /** Initial load of images, we only have one image that we can display. */
  public void load(Picasso picasso, String image) {
    this.picasso = picasso;
    this.images = Arrays.asList(image);
    loadImage(activeImageView, image);
    loadImage(inactiveImageView, image);
    startKenBurnsAnimation();
  }

  public void update(List<String> images) {
    this.images = images;
    if (images.size() > 1) {
      currentImageIndex = 1;
    }
  }

  private void loadImage(ImageView imageView, String image) {
    picasso.load(image).fit().noFade().centerCrop().into(imageView);
  }
}
