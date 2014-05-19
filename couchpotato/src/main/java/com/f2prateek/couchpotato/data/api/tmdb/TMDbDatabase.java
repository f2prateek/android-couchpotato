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

package com.f2prateek.couchpotato.data.api.tmdb;

import com.f2prateek.couchpotato.data.api.Movie;
import com.f2prateek.couchpotato.data.api.tmdb.model.Configuration;
import com.f2prateek.couchpotato.data.api.tmdb.model.Images;
import com.f2prateek.couchpotato.data.api.tmdb.model.MinifiedMovie;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCollectionResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCreditsResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieVideosResponse;
import com.f2prateek.couchpotato.data.api.tmdb.model.TMDbMovie;
import com.f2prateek.couchpotato.data.api.tmdb.model.Video;
import java.util.List;
import rx.Observable;
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
      configurationObservable = tmDbService.configuration().subscribeOn(Schedulers.io()).cache();
    }
    return configurationObservable;
  }

  private Observable<List<Movie>> transformMovieResponse(final Configuration configuration,
      Observable<MovieCollectionResponse> observable) {
    return observable.map(new Func1<MovieCollectionResponse, List<MinifiedMovie>>() {
      @Override public List<MinifiedMovie> call(MovieCollectionResponse response) {
        return response.getResults();
      }
    }).flatMap(new Func1<List<MinifiedMovie>, Observable<MinifiedMovie>>() {
      @Override public Observable<MinifiedMovie> call(List<MinifiedMovie> movies) {
        return Observable.from(movies);
      }
    }).filter(new Func1<MinifiedMovie, Boolean>() {
      @Override public Boolean call(MinifiedMovie movie) {
        // TODO: control in preferences
        return !movie.isAdult();
      }
    }).map(new Func1<MinifiedMovie, MinifiedMovie>() {
      @Override public MinifiedMovie call(MinifiedMovie movie) {
        movie.setConfiguration(configuration);
        return movie;
      }
    }).map(new Func1<MinifiedMovie, Movie>() {
      @Override public Movie call(MinifiedMovie movie) {
        return Movie.create(movie);
      }
    }).toList().subscribeOn(Schedulers.io());
  }

  public Observable<List<Movie>> getPopularMovies(final int page) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<List<Movie>>>() {
      @Override public Observable<List<Movie>> call(Configuration configuration) {
        return transformMovieResponse(configuration, tmDbService.popular(page));
      }
    });
  }

  public Observable<List<Movie>> getTopRatedMovies(final int page) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<List<Movie>>>() {
      @Override public Observable<List<Movie>> call(Configuration configuration) {
        return transformMovieResponse(configuration, tmDbService.topRated(page));
      }
    });
  }

  public Observable<List<Movie>> getUpcomingMovies(final int page) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<List<Movie>>>() {
      @Override public Observable<List<Movie>> call(Configuration configuration) {
        return transformMovieResponse(configuration, tmDbService.upcoming(page));
      }
    });
  }

  public Observable<List<Movie>> getNowPlayingMovies(final int page) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<List<Movie>>>() {
      @Override public Observable<List<Movie>> call(Configuration configuration) {
        return transformMovieResponse(configuration, tmDbService.nowPlaying(page));
      }
    });
  }

  public Observable<TMDbMovie> getMovie(final long id) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<TMDbMovie>>() {
      @Override public Observable<TMDbMovie> call(Configuration configuration) {
        return movie(id, configuration);
      }
    });
  }

  private Observable<TMDbMovie> movie(final long id, final Configuration configuration) {
    return tmDbService.movie(id).map(new Func1<TMDbMovie, TMDbMovie>() {
      @Override public TMDbMovie call(TMDbMovie movie) {
        movie.setConfiguration(configuration);
        return movie;
      }
    }).subscribeOn(Schedulers.io());
  }

  public Observable<List<Movie>> discoverMovies(final int page) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<List<Movie>>>() {
      @Override public Observable<List<Movie>> call(Configuration configuration) {
        return transformMovieResponse(configuration, tmDbService.discover(page));
      }
    });
  }

  public Observable<Images> getMovieImages(final long id) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<Images>>() {
      @Override public Observable<Images> call(Configuration configuration) {
        return images(id, configuration);
      }
    }).subscribeOn(Schedulers.io());
  }

  private Observable<Images> images(final long id, final Configuration configuration) {
    return tmDbService.movieImages(id).map(new Func1<Images, Images>() {
      @Override public Images call(Images images) {
        images.setConfiguration(configuration);
        return images;
      }
    });
  }

  public Observable<MovieCreditsResponse> getMovieCredits(final long id) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<MovieCreditsResponse>>() {
      @Override public Observable<MovieCreditsResponse> call(Configuration configuration) {
        return credits(id, configuration);
      }
    }).subscribeOn(Schedulers.io());
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

  public Observable<List<Video>> getVideos(final long id) {
    return videos(id).subscribeOn(Schedulers.io());
  }

  private Observable<List<Video>> videos(final long id) {
    return tmDbService.movieVideos(id) //
        .map(new Func1<MovieVideosResponse, List<Video>>() {
          @Override public List<Video> call(MovieVideosResponse movieVideosResponse) {
            return movieVideosResponse.getResults();
          }
        });
  }

  public Observable<List<Movie>> getSimilarMovies(final long id) {
    return getConfiguration().flatMap(new Func1<Configuration, Observable<List<Movie>>>() {
      @Override public Observable<List<Movie>> call(Configuration configuration) {
        return transformMovieResponse(configuration, tmDbService.movieSimilar(id));
      }
    });
  }
}
