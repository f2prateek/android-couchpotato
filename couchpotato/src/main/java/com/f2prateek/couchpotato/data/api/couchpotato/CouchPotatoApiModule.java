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

package com.f2prateek.couchpotato.data.api.couchpotato;

import android.content.SharedPreferences;
import com.f2prateek.couchpotato.data.prefs.StringPreference;
import com.f2prateek.ln.Ln;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(complete = false, library = true)
public class CouchPotatoApiModule {

  @Provides @Singleton @CouchPotatoHost StringPreference provideHostPreference(
      SharedPreferences preferences) {
    return new StringPreference(preferences, "couch_potato_host");
  }

  @Provides @Singleton @CouchPotatoApiKey StringPreference provideApiKeyPreference(
      SharedPreferences preferences) {
    return new StringPreference(preferences, "couch_potato_api_key");
  }

  @Provides @Singleton CouchPotatoEndpoint provideEndpoint(
      @CouchPotatoHost StringPreference hostPreference,
      @CouchPotatoApiKey StringPreference apiKeyPreference) {
    return new CouchPotatoEndpoint(hostPreference, apiKeyPreference);
  }

  @Provides @Singleton @CouchPotato Client provideClient(OkHttpClient client) {
    return new OkClient(client);
  }

  @Provides @Singleton @CouchPotato RestAdapter provideRestAdapter(CouchPotatoEndpoint endpoint,
      @CouchPotato Client client, Gson gson) {
    return new RestAdapter.Builder() //
        .setClient(client) //
        .setEndpoint(endpoint) //
        .setConverter(new GsonConverter(gson)) //
        .setLog(new RestAdapter.Log() {
          @Override public void log(String message) {
            Ln.d(message);
          }
        }) //
        .build();
  }

  @Provides @Singleton CouchPotatoService provideCouchPotatoService(
      @CouchPotato RestAdapter restAdapter) {
    return restAdapter.create(CouchPotatoService.class);
  }

  @Provides @Singleton CouchPotatoDatabase provideCouchPotatoDatabase(CouchPotatoService service) {
    return new CouchPotatoDatabase(service);
  }
}
