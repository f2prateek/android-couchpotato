package com.f2prateek.couchpotato.data.api.couchpotato;

import com.f2prateek.couchpotato.data.api.couchpotato.model.ApiKeyResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.DirectoryListResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.FileTypesResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.ProfileListResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.app.AppAvailableResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.app.AppVersionResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.logging.ClearLogginResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.logging.GetLoggingResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.logging.LoggingLogResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.logging.LoggingPartialResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.manage.ManageProgressResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.manage.ManageUpdateResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.AddMovieResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.MovieRefreshResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.MovieResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.MoviesResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.search.MovieSearchResponse;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface CouchPotatoService {

  // login
  @GET("/getkey") Observable<ApiKeyResponse> getApiKey(@Query("p") String password,
      @Query("u") String username);

  // directory
  @GET("/directory.list") Observable<DirectoryListResponse> listDirectory();

  // file
  @GET("/file.types") Observable<FileTypesResponse> getFileTypes();

  // profile
  @GET("/profile.list") Observable<ProfileListResponse> getProfiles();

  // manage
  @GET("/manage.progress") Observable<ManageProgressResponse> progress();

  @GET("/manage.update") Observable<ManageUpdateResponse> update();

  // logging
  @GET("/logging.clear") Observable<ClearLogginResponse> clear();

  @GET("/logging.get") Observable<GetLoggingResponse> getLogging(@Query("id") long id);

  @GET("/logging.log") Observable<LoggingLogResponse> log();

  @GET("/logging.partial") Observable<LoggingPartialResponse> partial();

  // app
  @GET("/app.available") Observable<AppAvailableResponse> available();

  @GET("/app.restart") Observable<Response> restart();

  @GET("/app.shutdown") Observable<Response> shutdown();

  @GET("/app.version") Observable<AppVersionResponse> version();

  // movie
  @GET("/movie.add") Observable<AddMovieResponse> addMovie(@Query("profile_id") String profile_id,
      @Query("identifier") String identifier, @Query("title") String title);

  @GET("/movie.list") Observable<MoviesResponse> getMovies();

  @GET("/movie.get") Observable<MovieResponse> getMovie(@Query("id") long id);

  @GET("/movie.refresh") Observable<MovieRefreshResponse> refreshMovie(@Query("id") long id);

  @GET("/movie.search") Observable<MovieSearchResponse> searchMovies(@Query("q") String query);
}
