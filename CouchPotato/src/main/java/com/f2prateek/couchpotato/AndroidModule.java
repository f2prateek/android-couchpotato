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

package com.f2prateek.couchpotato;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * A module for Android-specific dependencies which require a {@link android.content.Context} or
 * {@link android.app.Application} to create.
 */
@Module(library = true)
public class AndroidModule {
  private final CouchPotatoApplication application;

  public AndroidModule(CouchPotatoApplication application) {
    this.application = application;
  }

  @Provides @Singleton Context provideApplicationContext() {
    return application;
  }

  @Provides @Singleton SharedPreferences provideSharePreferences(final Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides @Singleton Resources provideResources(final Context context) {
    return context.getResources();
  }
}