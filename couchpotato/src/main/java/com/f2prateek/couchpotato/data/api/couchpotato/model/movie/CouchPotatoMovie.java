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

package com.f2prateek.couchpotato.data.api.couchpotato.model.movie;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class CouchPotatoMovie {
  private static final String LIBRARY_ID = "library_id";
  private static final String STATUS_ID = "status_id";
  private static final String PROFILE_ID = "profile_id";
  private static final String LAST_EDIT = "last_edit";

  public MovieProfile profile;
  @SerializedName(LIBRARY_ID)
  public long libraryId;
  public ArrayList<Release> releases;
  @SerializedName(STATUS_ID)
  public long statusId;
  @SerializedName(PROFILE_ID)
  public long profileId;
  public Library library;
  @SerializedName(LAST_EDIT)
  public long lastEdit;
  public long id;
  public ArrayList<File> files;

  public String getImdbPage() {
    return "http://www.imdb.com/title/" + library.info.imdb;
  }
}
