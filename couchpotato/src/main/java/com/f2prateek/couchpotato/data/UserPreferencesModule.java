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
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoApiKey;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoHost;
import com.f2prateek.couchpotato.data.prefs.BooleanPreference;
import com.f2prateek.couchpotato.data.prefs.FirstRun;
import com.f2prateek.couchpotato.data.prefs.StringPreference;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(complete = false, library = true)
public final class UserPreferencesModule {
  @Provides @Singleton @CouchPotatoHost StringPreference provideHostPreference(
      SharedPreferences preferences) {
    return new StringPreference(preferences, "couch_potato_host");
  }

  @Provides @Singleton @CouchPotatoApiKey StringPreference provideApiKeyPreference(
      SharedPreferences preferences) {
    return new StringPreference(preferences, "couch_potato_api_key");
  }

  @Provides @Singleton @FirstRun BooleanPreference provideFirstRun(
      SharedPreferences sharedPreferences) {
    return new BooleanPreference(sharedPreferences, "first_run", false);
  }
}
