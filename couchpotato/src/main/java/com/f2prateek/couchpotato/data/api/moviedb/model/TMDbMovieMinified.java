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

package com.f2prateek.couchpotato.data.api.moviedb.model;

import com.google.gson.annotations.SerializedName;

public class TMDbMovieMinified {
  public boolean adult;

  @SerializedName("backdrop_path")
  public String backdrop;

  public long id;
  @SerializedName("original_title")
  public String originalTitle;

  //public Date release_date;
  @SerializedName("poster_path")
  public String poster;

  public float popularity;
  public String title;

  @SerializedName("vote_average")
  public float voteAverage;

  @SerializedName("vote_count")
  public long voteCount;
}
