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

import com.f2prateek.couchpotato.data.api.tmdb.TMDbService;
import com.f2prateek.couchpotato.data.api.tmdb.model.Configuration;
import com.f2prateek.couchpotato.data.api.tmdb.model.Images;
import com.f2prateek.couchpotato.data.api.tmdb.model.MinifiedMovie;
import com.f2prateek.couchpotato.data.api.tmdb.model.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCollectionResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCreditsResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieVideosResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.Video;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TMDbDatabase {
  private final TMDbService tmDbService;
  private Observable<Configuration> configurationObservable;

  public TMDbDatabase(TMDbService tmDbService) {
    this.tmDbService = tmDbService;
  }

  // Fetch the configuration and cache it future use.
  public Observable<Configuration> getConfiguration() {
    if (configurationObservable == null) {
      configurationObservable = tmDbService.configuration();
    }
    return configurationObservable.cache();
  }

  public Subscription getPopularMovies(final int page,
      final Observer<List<MinifiedMovie>> observer) {
    return getConfiguration() //
        .flatMap(new Func1<Configuration, Observable<List<MinifiedMovie>>>() {
                   @Override public Observable<List<MinifiedMovie>> call(
                       Configuration configuration) {
                     return popularMovies(page, configuration);
                   }
                 }
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  private Observable<List<MinifiedMovie>> popularMovies(final int page,
      final Configuration configuration) {
    return tmDbService.popular(page) //
        .map(new Func1<MovieCollectionResponse, List<MinifiedMovie>>() {
          @Override public List<MinifiedMovie> call(MovieCollectionResponse response) {
            return response.getResults();
          }
        }) //
        .flatMap(new Func1<List<MinifiedMovie>, Observable<MinifiedMovie>>() {
          @Override public Observable<MinifiedMovie> call(List<MinifiedMovie> movies) {
            return Observable.from(movies);
          }
        }) //
        .filter(new Func1<MinifiedMovie, Boolean>() {
          @Override public Boolean call(MinifiedMovie movie) {
            // TODO: control in preferences
            return !movie.isAdult();
          }
        }) //
        .map(new Func1<MinifiedMovie, MinifiedMovie>() {
          @Override public MinifiedMovie call(MinifiedMovie movie) {
            movie.setConfiguration(configuration);
            return movie;
          }
        }) //
        .toList();
  }

  public Subscription getMovie(final long id, final Observer<Movie> observer) {
    return getConfiguration() //
        .flatMap(new Func1<Configuration, Observable<Movie>>() {
          @Override public Observable<Movie> call(Configuration configuration) {
            return movie(id, configuration);
          }
        }) //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(observer);
  }

  private Observable<Movie> movie(final long id, final Configuration configuration) {
    return tmDbService.movie(id) //
        .map(new Func1<Movie, Movie>() {
          @Override public Movie call(Movie movie) {
            movie.setConfiguration(configuration);
            return movie;
          }
        });
  }

  public Subscription getMovieImages(final long id, final Observer<Images> observer) {
    return getConfiguration() //
        .flatMap(new Func1<Configuration, Observable<Images>>() {
          @Override public Observable<Images> call(Configuration configuration) {
            return images(id, configuration);
          }
        }) //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(observer);
  }

  private Observable<Images> images(final long id, final Configuration configuration) {
    return tmDbService.movieImages(id).map(new Func1<Images, Images>() {
      @Override public Images call(Images images) {
        images.setConfiguration(configuration);
        return images;
      }
    });
  }

  public Subscription getMovieCredits(final long id,
      final Observer<MovieCreditsResponse> observer) {
    return getConfiguration() //
        .flatMap(new Func1<Configuration, Observable<MovieCreditsResponse>>() {
          @Override public Observable<MovieCreditsResponse> call(Configuration configuration) {
            return credits(id, configuration);
          }
        }) //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(observer);
  }

  private Observable<MovieCreditsResponse> credits(final long id,
      final Configuration configuration) {
    return tmDbService.movieCredits(id)
        .map(new Func1<MovieCreditsResponse, MovieCreditsResponse>() {
          @Override public MovieCreditsResponse call(MovieCreditsResponse movieCreditsResponse) {
            movieCreditsResponse.setConfiguration(configuration);
            return movieCreditsResponse;
          }
        });
  }

  public Subscription getVideos(final long id, final Observer<List<Video>> observer) {
    return getConfiguration() //
        .flatMap(new Func1<Configuration, Observable<List<Video>>>() {
          @Override public Observable<List<Video>> call(Configuration configuration) {
            return videos(id, configuration);
          }
        }) //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(observer);
  }

  private Observable<List<Video>> videos(final long id, final Configuration configuration) {
    return tmDbService.movieVideos(id).map(new Func1<MovieVideosResponse, List<Video>>() {
      @Override public List<Video> call(MovieVideosResponse movieVideosResponse) {
        return movieVideosResponse.getResults();
      }
    });
  }

  public Subscription getSimilarMovies(final long id,
      final Observer<List<MinifiedMovie>> observer) {
    return getConfiguration() //
        .flatMap(new Func1<Configuration, Observable<List<MinifiedMovie>>>() {
          @Override public Observable<List<MinifiedMovie>> call(Configuration configuration) {
            return similarMovies(id, configuration);
          }
        }) //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(observer);
  }

  private Observable<List<MinifiedMovie>> similarMovies(final long id,
      final Configuration configuration) {
    return tmDbService.movieSimilar(id) //
        .map(new Func1<MovieCollectionResponse, List<MinifiedMovie>>() {
          @Override public List<MinifiedMovie> call(MovieCollectionResponse response) {
            return response.getResults();
          }
        }) //
        .flatMap(new Func1<List<MinifiedMovie>, Observable<MinifiedMovie>>() {
          @Override public Observable<MinifiedMovie> call(List<MinifiedMovie> movies) {
            return Observable.from(movies);
          }
        }) //
        .filter(new Func1<MinifiedMovie, Boolean>() {
          @Override public Boolean call(MinifiedMovie movie) {
            // TODO: control in preferences
            return !movie.isAdult();
          }
        }) //
        .map(new Func1<MinifiedMovie, MinifiedMovie>() {
          @Override public MinifiedMovie call(MinifiedMovie movie) {
            movie.setConfiguration(configuration);
            return movie;
          }
        }) //
        .toList();
  }
}
