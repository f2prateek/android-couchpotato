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

package com.f2prateek.couchpotato.data;

import android.content.SharedPreferences;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoHost;
import com.f2prateek.couchpotato.data.prefs.BooleanPreference;
import com.f2prateek.couchpotato.data.prefs.IntPreference;
import com.f2prateek.couchpotato.data.prefs.StringPreference;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RestAdapter;

@Module(
    complete = false,
    library = true,
    overrides = true)
public final class DebugDataModule {
  private static final int DEFAULT_ANIMATION_SPEED = 1; // 1x (normal) speed.
  private static final boolean DEFAULT_PICASSO_DEBUGGING = false; // Debug indicators displayed
  private static final boolean DEFAULT_PIXEL_GRID_ENABLED = false; // No pixel grid overlay.
  private static final boolean DEFAULT_PIXEL_RATIO_ENABLED = false; // No pixel ratio overlay.
  private static final boolean DEFAULT_SCALPEL_ENABLED = false; // No crazy 3D view tree.
  private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false; // Draw views by default.
  private static final boolean DEFAULT_SEEN_DEBUG_DRAWER = false; // Show debug drawer first time.
  private static final int DEFAULT_NETWORK_LOGGING_LEVEL = RestAdapter.LogLevel.NONE.ordinal();

  @Provides @Singleton @CouchPotatoHost StringPreference provideHostPreference(
      SharedPreferences preferences) {
    // Keep in sync with UserPreferencesModule; this for my local server for development
    return new StringPreference(preferences, "couch_potato_host", "http://10.14.176.118:5050");
  }

  @Provides @Singleton @AnimationSpeed IntPreference provideAnimationSpeed(
      SharedPreferences preferences) {
    return new IntPreference(preferences, "debug_animation_speed", DEFAULT_ANIMATION_SPEED);
  }

  @Provides @Singleton @PicassoIndicators BooleanPreference providePicassoIndicators(
      SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_picasso_indicators",
        DEFAULT_PICASSO_DEBUGGING);
  }

  @Provides @Singleton @PicassoLogging BooleanPreference providePicassoLogging(
      SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_picasso_logging", DEFAULT_PICASSO_DEBUGGING);
  }

  @Provides @Singleton @PixelGridEnabled BooleanPreference providePixelGridEnabled(
      SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_pixel_grid_enabled",
        DEFAULT_PIXEL_GRID_ENABLED);
  }

  @Provides @Singleton @PixelRatioEnabled BooleanPreference providePixelRatioEnabled(
      SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_pixel_ratio_enabled",
        DEFAULT_PIXEL_RATIO_ENABLED);
  }

  @Provides @Singleton @SeenDebugDrawer BooleanPreference provideSeenDebugDrawer(
      SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_seen_debug_drawer", DEFAULT_SEEN_DEBUG_DRAWER);
  }

  @Provides @Singleton @ScalpelEnabled BooleanPreference provideScalpelEnabled(
      SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
  }

  @Provides @Singleton @ScalpelWireframeEnabled BooleanPreference provideScalpelWireframeEnabled(
      SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_scalpel_wireframe_drawer",
        DEFAULT_SCALPEL_WIREFRAME_ENABLED);
  }

  @Provides @Singleton @NetworkLoggingLevel IntPreference provideNetworkLogginLevel(
      SharedPreferences preferences) {
    return new IntPreference(preferences, "network_logging_level", DEFAULT_NETWORK_LOGGING_LEVEL);
  }
}
