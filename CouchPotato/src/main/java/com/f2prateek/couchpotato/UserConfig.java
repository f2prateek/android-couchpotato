/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.f2prateek.couchpotato;

import android.content.SharedPreferences;

public class UserConfig {

  private static final String KEY_HOST_SCHEME = "UserConfig.KEY_HOST_SCHEME";
  private static final String KEY_HOST_URL = "UserConfig.KEY_HOST_URL";
  private static final String KEY_API_KEY = "UserConfig.KEY_API_KEY";
  private static final String KEY_HOST_PORT = "UserConfig.KEY_HOST_PORT";

  private String hostScheme;
  private String hostUrl;
  private String apiKey;
  private int port;

  // Convencience to read from shared Preferences
  public UserConfig(SharedPreferences sharedPreferences) {
    this.hostScheme = sharedPreferences.getString(KEY_HOST_SCHEME, "http");
    this.hostUrl = sharedPreferences.getString(KEY_HOST_URL, null);
    this.apiKey = sharedPreferences.getString(KEY_API_KEY, null);
    this.port = sharedPreferences.getInt(KEY_HOST_PORT, 5050);
  }

  public void save(SharedPreferences sharedPreferences) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(KEY_HOST_SCHEME, hostScheme);
    editor.putString(KEY_HOST_URL, hostUrl);
    editor.putString(KEY_API_KEY, apiKey);
    editor.putInt(KEY_HOST_PORT, port);
    editor.commit();
  }

  public String getHostScheme() {
    return hostScheme;
  }

  public void setHostScheme(String hostScheme) {
    this.hostScheme = hostScheme;
  }

  public String getHostUrl() {
    return hostUrl;
  }

  public void setHostUrl(String hostUrl) {
    this.hostUrl = hostUrl;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getServerUrl() {
    // TODO : perform validation?
    return hostScheme + "://" + hostUrl + ":" + port + "/api" + "/" + apiKey;
  }

  public String getUnauthenticatedServerUrl() {
    // TODO : perform validation?
    return hostScheme + "://" + hostUrl + ":" + port;
  }

  @Override public String toString() {
    return "UserConfig{" +
        "hostScheme='" + hostScheme + '\'' +
        ", hostUrl='" + hostUrl + '\'' +
        ", apiKey='" + apiKey + '\'' +
        ", port=" + port +
        '}';
  }
}