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

package com.f2prateek.couchpotato.data.api.couchpotato;

import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.couchpotato.model.ApiKeyResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.AddMovieResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.CouchPotatoMovie;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.MoviesResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.profile.Profile;
import com.f2prateek.couchpotato.data.api.couchpotato.model.profile.ProfilesResponse;
import com.f2prateek.couchpotato.util.Strings;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.f2prateek.couchpotato.data.Util.md5;

public class CouchPotatoDatabase {
  private final CouchPotatoService couchPotatoService;
  private Observable<List<Profile>> profilesObservable;

  public CouchPotatoDatabase(CouchPotatoService couchPotatoService) {
    this.couchPotatoService = couchPotatoService;
  }

  public Observable<ApiKeyResponse> getApiKey(final String username, final String password) {
    return couchPotatoService.getApiKey(md5(password), md5(username))
        .timeout(10, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io());
  }

  public enum LibraryMovieStatus {
    WANTED("active"),
    SNATCHED("done");

    String string;

    LibraryMovieStatus(String string) {
      this.string = string;
    }

    @Override public String toString() {
      return string;
    }
  }

  /**
   * Fetch all movies with the given status(es).
   */
  public Observable<List<Movie>> getMovies(final List<LibraryMovieStatus> statuses) {
    String status = Strings.join(", ", statuses);
    return couchPotatoService.getMovies(status)
        .timeout(30, TimeUnit.SECONDS)
        .flatMap(new Func1<MoviesResponse, Observable<CouchPotatoMovie>>() {
          @Override public Observable<CouchPotatoMovie> call(MoviesResponse moviesResponse) {
            return Observable.from(moviesResponse.getMovies());
          }
        })
        .map(new Func1<CouchPotatoMovie, Movie>() {
          @Override public Movie call(CouchPotatoMovie couchPotatoMovie) {
            return Movie.create(couchPotatoMovie);
          }
        })
        .toList()
        .subscribeOn(Schedulers.io());
  }

  // Fetch the profiles and cache it future use.
  public Observable<List<Profile>> getProfiles() {
    if (profilesObservable == null) {
      profilesObservable =
          couchPotatoService.getProfiles().map(new Func1<ProfilesResponse, List<Profile>>() {
            @Override public List<Profile> call(ProfilesResponse response) {
              return response.getProfiles();
            }
          }).cache().subscribeOn(Schedulers.io());
    }
    return profilesObservable;
  }

  public Observable<Boolean> addMovie(int profileId, String imdbId) {
    return couchPotatoService.addMovie(profileId, imdbId)
        .map(new Func1<AddMovieResponse, Boolean>() {
          @Override public Boolean call(AddMovieResponse addMovieResponse) {
            return addMovieResponse.success;
          }
        })
        .subscribeOn(Schedulers.io());
  }
}