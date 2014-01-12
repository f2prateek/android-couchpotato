package com.f2prateek.couchpotato.services.couchpotato;

import com.f2prateek.couchpotato.model.couchpotato.app.AppAvailableResponse;
import com.f2prateek.couchpotato.model.couchpotato.app.AppVersionResponse;
import retrofit.client.Response;
import retrofit.http.GET;

// app
public interface CouchPotatoAppApi {
  @GET("/app.available") AppAvailableResponse available();

  @GET("/app.restart") Response restart();

  @GET("/app.shutdown") Response shutdown();

  @GET("/app.version") AppVersionResponse version();
}
