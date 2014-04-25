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

package com.f2prateek.couchpotato.data.api.couchpotato.model.movie;

import java.util.List;

/** Response for /CouchPotatoMovie.list */
public class MoviesResponse {

  private static final String FIELD_MOVIES = "movies";
  private static final String FIELD_TOTAL = "total";
  private static final String FIELD_SUCCESS = "success";
  private static final String FIELD_EMPTY = "empty";

  private List<CouchPotatoMovie> movies;
  private int total;
  private boolean success;
  private boolean empty;

  public List<CouchPotatoMovie> getMovies() {
    return movies;
  }

  public int getTotal() {
    return total;
  }

  public boolean isSuccess() {
    return success;
  }

  public boolean isEmpty() {
    return empty;
  }

  @Override public String toString() {
    return "MovieListResponse{"
        + "movies="
        + movies
        + ", total="
        + total
        + ", success="
        + success
        + ", empty="
        + empty
        + '}';
  }
}