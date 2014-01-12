package com.f2prateek.couchpotato.services.couchpotato;

import com.f2prateek.couchpotato.model.couchpotato.movie.MovieAddResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieGetResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieListResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.MovieRefreshResponse;
import com.f2prateek.couchpotato.model.couchpotato.movie.search.MovieSearchResponse;
import retrofit.http.GET;
import retrofit.http.Query;

// movie
public interface CouchPotatoMovieApi {

  @GET("/movie.add") MovieAddResponse add(@Query("profile_id") String profile_id,
      @Query("identifier") String identifier, @Query("title") String title);

  @GET("/movie.list") MovieListResponse list();

  @GET("/movie.get") MovieGetResponse get(@Query("id") long id);

  @GET("/movie.refresh") MovieRefreshResponse refresh(@Query("id") long id);

  @GET("/movie.search") MovieSearchResponse search(@Query("q") String query);
}
