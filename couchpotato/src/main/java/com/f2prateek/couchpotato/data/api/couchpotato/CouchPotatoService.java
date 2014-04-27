package com.f2prateek.couchpotato.data.api.couchpotato;

import com.f2prateek.couchpotato.data.api.couchpotato.model.ApiKeyResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.app.AppAvailableResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.app.AppVersionResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.file.FileTypesResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.logging.ClearLoggingResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.logging.GetLoggingResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.AddMovieResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.MovieRefreshResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.MovieResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.MoviesResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.search.MovieSearchResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.profile.ProfilesResponse;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface CouchPotatoService {
  // login
  @GET("/getkey") Observable<ApiKeyResponse> getApiKey(@Query("p") String password,
      @Query("u") String username);

  // logging
  @GET("/logging.clear") Observable<ClearLoggingResponse> clearLog();

  @GET("/logging.get") Observable<GetLoggingResponse> getLog(@Query("nr") String number);

  // app
  @GET("/app.available") Observable<AppAvailableResponse> appAvailable();

  @GET("/app.restart") Observable<Response> appRestart();

  @GET("/app.shutdown") Observable<Response> appShutdown();

  @GET("/app.version") Observable<AppVersionResponse> appVersion();

  // file
  @GET("/file.types") Observable<FileTypesResponse> list();

  // profile
  @GET("/profile.list") Observable<ProfilesResponse> getProfiles();

  // movie
  @GET("/movie.add") Observable<AddMovieResponse> addMovie(@Query("profile_id") String profile_id,
      @Query("identifier") String identifier, @Query("title") String title);

  @GET("/movie.list") Observable<MoviesResponse> getMovies();

  @GET("/movie.get") Observable<MovieResponse> getMovie(@Query("id") long id);

  @GET("/movie.refresh") Observable<MovieRefreshResponse> refreshMovie(@Query("id") long id);

  @GET("/movie.search") Observable<MovieSearchResponse> searchMovies(@Query("q") String query);
}
