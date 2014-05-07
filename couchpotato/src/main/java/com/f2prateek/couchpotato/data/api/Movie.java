package com.f2prateek.couchpotato.data.api;

import android.auto.value.AutoValue;
import android.os.Parcelable;
import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.CouchPotatoMovie;
import com.f2prateek.couchpotato.data.api.tmdb.model.MinifiedMovie;

/**
 * Tiny abstraction for mapping movies from different services.
 */
@AutoValue
public abstract class Movie implements Parcelable {
  /** TMDb id for the movie */
  public abstract long id();

  /** Movie title. */
  public abstract String title();

  /** Path to the poster for this movie. */
  public abstract String poster();

  /** Path to the backdrop for this movie. */
  public abstract String backdrop();

  public static Movie create(MinifiedMovie movie) {
    return new AutoValue_Movie(movie.getId(), movie.getTitle(), movie.getPosterPath(),
        movie.getBackdropPath());
  }

  public static Movie create(CouchPotatoMovie movie) {
    return new AutoValue_Movie(movie.library.info.tmdbId, movie.library.info.titles.get(0),
        movie.library.info.images.getPoster(), movie.library.info.images.getBackdrop());
  }
}
