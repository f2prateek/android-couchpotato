package com.f2prateek.couchpotato.services.couchpotato;

import com.f2prateek.couchpotato.model.couchpotato.manage.ManageProgressResponse;
import com.f2prateek.couchpotato.model.couchpotato.manage.ManageUpdateResponse;
import retrofit.http.GET;

// manage
public interface CouchPotatoManageApi {
  @GET("/manage.progress") ManageProgressResponse progress();

  @GET("/manage.update") ManageUpdateResponse update();
}
