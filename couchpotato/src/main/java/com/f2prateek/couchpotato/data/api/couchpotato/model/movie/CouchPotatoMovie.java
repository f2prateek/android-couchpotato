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

import java.util.ArrayList;

public class CouchPotatoMovie {
  public MovieProfile profile;
  public long library_id;
  public ArrayList<Release> releases;
  public long status_id;
  public long profile_id;
  public Library library;
  public long last_edit;
  public long id;
  public ArrayList<File> files;

  @Override public String toString() {
    return "MovieDBMovie{" +
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

  public String getImdbPage() {
    return "http://www.imdb.com/title/" + library.info.imdb;
  }
}
