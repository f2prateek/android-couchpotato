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
