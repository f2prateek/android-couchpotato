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

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Info {
  private static final String TMDB_ID = "tmdb_id";
  private static final String VIA_IMDB = "via_imdb";
  private static final String VIA_TMDB = "via_tmdb";
  private static final String ORIGINAL_TITLE = "original_title";

  public Rating rating;
  @SerializedName(TMDB_ID)
  public long tmdbId;
  @SerializedName(VIA_IMDB)
  public boolean viaImdb;
  public Images images;
  public ArrayList<String> titles;
  public String imdb;
  public int year;
  @SerializedName(VIA_TMDB)
  public boolean viaTmdb;
  public String plot;
  public ArrayList<String> genres;
  public String released;
  public String tagline;
  // public List release_date;
  @SerializedName(ORIGINAL_TITLE)
  public String originalTitle;
  public ArrayList<String> directors;
  public ArrayList<String> writers;
  public ArrayList<String> actors;
  public int runtime;
}
