package com.f2prateek.couchpotato.data.api.couchpotato;

import com.f2prateek.couchpotato.data.api.couchpotato.model.ApiKeyResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.UpdaterInfo;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface CouchPotatoService {
  @GET("/updater.info") Observable<UpdaterInfo> updaterInfo();

  @GET("/getkey") Observable<ApiKeyResponse> apiKey(@Query("p") String password,
      @Query("u") String username);
}
