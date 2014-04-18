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

package com.f2prateek.couchpotato.data;

import com.f2prateek.couchpotato.data.api.moviedb.TMDbService;
import com.f2prateek.couchpotato.data.api.moviedb.model.DiscoverMoviesResponse;
import com.f2prateek.couchpotato.data.api.moviedb.model.TMDbConfiguration;
import com.f2prateek.couchpotato.data.api.moviedb.model.TMDbMovieMinified;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TMDbDatabase {
  private final TMDbService tmDbService;

  public TMDbDatabase(TMDbService tmDbService) {
    this.tmDbService = tmDbService;
  }

  public Subscription getPopularMovies(final int page,
      final Observer<List<TMDbMovieMinified>> observer) {
    return tmDbService.configuration()
        .flatMap(new Func1<TMDbConfiguration, Observable<List<TMDbMovieMinified>>>() {
          @Override public Observable<List<TMDbMovieMinified>> call(
              TMDbConfiguration tmDbConfiguration) {
            return popularMovies(page, tmDbConfiguration);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  private Observable<List<TMDbMovieMinified>> popularMovies(final int page,
      final TMDbConfiguration tmDbConfiguration) {
    return tmDbService.popular(page) //
        .map(new Func1<DiscoverMoviesResponse, List<TMDbMovieMinified>>() {
          @Override public List<TMDbMovieMinified> call(DiscoverMoviesResponse response) {
            return response.results;
          }
        }) //
        .flatMap(new Func1<List<TMDbMovieMinified>, Observable<TMDbMovieMinified>>() {
          @Override public Observable<TMDbMovieMinified> call(List<TMDbMovieMinified> movies) {
            return Observable.from(movies);
          }
        }) //
        .filter(new Func1<TMDbMovieMinified, Boolean>() {
          @Override public Boolean call(TMDbMovieMinified tmDbMovie) {
            // TODO: control in preferences
            return !tmDbMovie.adult;
          }
        }) //
        .map(new Func1<TMDbMovieMinified, TMDbMovieMinified>() {
          @Override public TMDbMovieMinified call(TMDbMovieMinified tmDbMovieMinified) {
            tmDbMovieMinified.poster =
                tmDbConfiguration.images.getPosterUrl(tmDbMovieMinified.poster);
            return tmDbMovieMinified;
          }
        }) //
        .map(new Func1<TMDbMovieMinified, TMDbMovieMinified>() {
          @Override public TMDbMovieMinified call(TMDbMovieMinified tmDbMovieMinified) {
            tmDbMovieMinified.backdrop =
                tmDbConfiguration.images.getBackdropUrl(tmDbMovieMinified.backdrop);
            return tmDbMovieMinified;
          }
        }) //
        .toList();
  }
}
