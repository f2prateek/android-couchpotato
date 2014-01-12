package com.f2prateek.couchpotato.services.couchpotato;

import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingClearResponse;
import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingGetResponse;
import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingLogResponse;
import com.f2prateek.couchpotato.model.couchpotato.logging.LoggingPartialResponse;
import retrofit.http.GET;
import retrofit.http.Query;

// logging
public interface CouchPotatoLoggingApi {
  @GET("/logging.clear") LoggingClearResponse clear();

  @GET("/logging.get") LoggingGetResponse get(@Query("id") long id);

  @GET("/logging.log") LoggingLogResponse log();

  @GET("/logging.partial") LoggingPartialResponse partial();
}
