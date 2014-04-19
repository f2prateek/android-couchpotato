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

package com.f2prateek.couchpotato.data.api.tmdb;

import retrofit.RequestInterceptor;

/**
 * RequestInterceptor to add our api key as a parameter.
 */
class TMDbRequestInterceptor implements RequestInterceptor {
  private static final String API_KEY_PARAM = "api_key";

  private final String apiKey;

  public TMDbRequestInterceptor(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override public void intercept(RequestFacade request) {
    request.addQueryParam(API_KEY_PARAM, apiKey);
  }
}
