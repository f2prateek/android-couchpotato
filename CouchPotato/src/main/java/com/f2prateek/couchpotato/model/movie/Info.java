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

public class Info {
  public Rating rating;
  public long tmdb_id;
  public boolean via_imdb;
  public Images images;
  public List<String> titles;
  public String imdb;
  public int year;
  public boolean via_tmdb;
  public String plot;
  public List<String> genres;
  public String released;
  public String tagline;
  // public List release_date;
  public String original_title;
  public List<String> directors;
  public List<String> writers;
  public List<String> actors;
  public int runtime;

  @Override public String toString() {
    return "Info{" +
        "tmdb_id=" + tmdb_id +
        ", via_imdb=" + via_imdb +
        ", images=" + images +
        ", titles=" + titles +
        ", imdb='" + imdb + '\'' +
        ", year=" + year +
        ", via_tmdb=" + via_tmdb +
        ", plot='" + plot + '\'' +
        ", genres=" + genres +
        ", released='" + released + '\'' +
        ", tagline='" + tagline + '\'' +
        ", original_title='" + original_title + '\'' +
        ", directors=" + directors +
        ", writers=" + writers +
        ", actors=" + actors +
        ", runtime=" + runtime +
        '}';
  }
}
