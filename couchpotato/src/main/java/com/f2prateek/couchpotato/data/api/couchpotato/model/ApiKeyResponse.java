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

package com.f2prateek.couchpotato.data.api.couchpotato.model;

import com.google.gson.annotations.SerializedName;

public class ApiKeyResponse {
  private static final String FIELD_API_KEY = "api_key";
  private static final String FIELD_SUCCESS = "success";

  @SerializedName(FIELD_API_KEY)
  private String apiKey;
  @SerializedName(FIELD_SUCCESS)
  private boolean success;

  public String getApiKey() {
    return apiKey;
  }

  public boolean isSuccess() {
    return success;
  }

  @Override public String toString() {
    return "ApiKeyResponse{" + "apiKey='" + apiKey + '\'' + ", success=" + success + '}';
  }
}
