package com.f2prateek.couchpotato.data.api.moviedb;

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
