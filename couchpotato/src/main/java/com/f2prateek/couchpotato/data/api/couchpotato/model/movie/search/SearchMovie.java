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

package com.f2prateek.couchpotato.data.api.couchpotato.model.movie.search;

import com.f2prateek.couchpotato.data.api.couchpotato.model.movie.Images;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchMovie {
  private static final String TMDB_ID = "tmdb_id";
  private static final String ORIGINAL_TITLE = "original_title";

  public SearchRating rating;
  public List<String> genres;
  //public boolean in_library;
  @SerializedName(TMDB_ID)
  public long tmdbId;
  public String plot;
  public String tagline;
  @SerializedName(ORIGINAL_TITLE)
  public String originalTitle;
  public List<String> writers;
  //public boolean via_imdb;
  public Images images;
  public List<String> directors;
  public List<String> titles;
  public String imdb;
  public int year;
  //public boolean via_tmdb;
  //public boolean in_wanted;
  public List<String> actors;
  public int runtime;
}