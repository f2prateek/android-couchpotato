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
    hostPreference.set(host);
    updateUrl();
  }

  public void setApiKey(String apiKey) {
    apiKeyPreference.set(apiKey);
    updateUrl();
  }

  private void updateUrl() {
    if (apiKeyPreference.get() == null) {
      url = hostPreference.get();
    } else {
      url = hostPreference.get() + apiKeyPreference.get();
    }
  }

  public String getHost() {
    return hostPreference.get();
  }

  @Override public String getUrl() {
    if (url == null) {
      throw new IllegalStateException("url is not set yet.");
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
