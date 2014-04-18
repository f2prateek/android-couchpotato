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
