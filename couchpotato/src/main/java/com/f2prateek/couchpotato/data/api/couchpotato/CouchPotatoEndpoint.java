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

package com.f2prateek.couchpotato.data.api.couchpotato;

import com.f2prateek.couchpotato.data.prefs.StringPreference;
import retrofit.Endpoint;

/**
 * A mutable endpoint that allows adding api key as param value.
 * Doubles as preference storage.
 */
public class CouchPotatoEndpoint implements Endpoint {
  private static final String NAME = "default";

  private final StringPreference hostPreference;
  private final StringPreference apiKeyPreference;
  private String url;

  public CouchPotatoEndpoint(StringPreference hostPreference, StringPreference apiKeyPreference) {
    this.hostPreference = hostPreference;
    this.apiKeyPreference = apiKeyPreference;
  }

  public void setHost(String host) {
    // todo: sanitize the input
    hostPreference.set(host);
    updateUrl();
  }

  public void setApiKey(String apiKey) {
    apiKeyPreference.set(apiKey);
    updateUrl();
  }

  public void clear() {
    hostPreference.delete();
    apiKeyPreference.delete();
    updateUrl();
  }

  /**
   * ApiKey not being set is same as user not being logged in.
   */
  public boolean isSet() {
    return apiKeyPreference.isSet();
  }

  private void updateUrl() {
    if (apiKeyPreference.get() == null) {
      url = hostPreference.get();
    } else {
      url = hostPreference.get() + "/api/" + apiKeyPreference.get();
    }
  }

  public String getHost() {
    return hostPreference.get();
  }

  @Override public String getUrl() {
    if (hostPreference.get() == null) {
      throw new IllegalStateException("host is not set yet.");
    } else if (url == null) {
      updateUrl();
    }
    return url;
  }

  @Override public String getName() {
    return NAME;
  }

  @Override public String toString() {
    return "CouchPotatoEndpoint{"
        + "host="
        + hostPreference.get()
        + ", apiKey="
        + apiKeyPreference.get()
        + ", url='"
        + url
        + '\''
        + '}';
  }
}
