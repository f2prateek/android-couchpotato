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

package com.f2prateek.couchpotato.model.movie;

import java.util.List;

public class Movie {
  public MovieProfile profile;
  public long library_id;
  public List<Release> releases;
  public long status_id;
  public long profile_id;
  public Library library;
  public long last_edit;
  public long id;
  public List<File> files;

  @Override public String toString() {
    return "Movie{" +
        "profile=" + profile +
        ", library_id=" + library_id +
        ", releases=" + releases +
        ", status_id=" + status_id +
        ", profile_id=" + profile_id +
        ", library=" + library +
        ", last_edit=" + last_edit +
        ", id=" + id +
        ", files=" + files +
        '}';
  }

  public String getPosterUrl() {
    String[] posters = library.info.images.poster;
    if (posters != null && posters.length > 0) {
      return posters[0];
    } else {
      return null;
    }
  }

  public String getBackdropUrl() {
    String[] backdrops = library.info.images.backdrop;
    if (backdrops != null && backdrops.length > 0) {
      return backdrops[0];
    } else {
      return null;
    }
  }

  public String getImdbPage() {
    return "http://www.imdb.com/title/" + library.info.imdb;
  }
}
