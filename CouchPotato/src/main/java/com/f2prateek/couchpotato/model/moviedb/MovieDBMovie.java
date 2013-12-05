/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.model.moviedb;

import com.f2prateek.couchpotato.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Date;

public class MovieDBMovie {
  public long id;

  public boolean adult;
  public String backdrop_path;
  public long budget;
  public ArrayList<Genre> genres;
  public String homepage;
  public String imdb_id;
  public String original_title;
  public String overview;
  public float popularity;
  public String poster_path;
  public Date release_date;
  public long revenue;
  public int runtime;
  public String status;
  public String tagline;
  public String title;
  public double vote_average;
  public int vote_count;
  public Casts casts;
  public Images images;
  public SimilarMovies similarMovies;
  public Trailers trailers;

  public String getPosterUrl(MovieDbConfiguration movieDbConfiguration) {
    if (CollectionUtils.isEmpty(movieDbConfiguration.images.poster_sizes)
        || CollectionUtils.isEmpty(images.posters)) {
      return null;
    }
    return movieDbConfiguration.images.base_url
        + movieDbConfiguration.images.poster_sizes.get(3)
        + images.posters.get(0).file_path;
  }

  public String getSmallPosterUrl(MovieDbConfiguration movieDbConfiguration) {
    if (CollectionUtils.isEmpty(movieDbConfiguration.images.poster_sizes)
        || CollectionUtils.isEmpty(images.posters)) {
      return null;
    }
    return movieDbConfiguration.images.base_url
        + movieDbConfiguration.images.poster_sizes.get(1)
        + images.posters.get(0).file_path;
  }

  public String getBackdropUrl(MovieDbConfiguration movieDbConfiguration) {
    if (CollectionUtils.isEmpty(movieDbConfiguration.images.backdrop_sizes)
        || CollectionUtils.isEmpty(images.backdrops)) {
      return null;
    }
    return movieDbConfiguration.images.base_url
        + movieDbConfiguration.images.backdrop_sizes.get(1)
        + "/"
        + images.backdrops
        .get(0).file_path;
  }

  public ArrayList<Trailers.Trailer> getYoutubeTrailers() {
    return trailers.youtube;
  }

  public String getImdbPage() {
    return "http://www.imdb.com/title/" + imdb_id;
  }
}