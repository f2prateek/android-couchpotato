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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MovieDbConfiguration {
  public ImagesConfiguration images;

  public static class ImagesConfiguration {
    public String base_url;
    public ArrayList<String> poster_sizes;
    public ArrayList<String> backdrop_sizes;
    public ArrayList<String> profile_sizes;
    public ArrayList<String> logo_sizes;
  }

  // Lists cannot be saved directly, convert to HashSet
  static Set<String> listToSet(ArrayList<String> list) {
    return new HashSet<String>(list);
  }

  // Convert from set to list
  static ArrayList<String> setToList(Set<String> set) {
    return new ArrayList<String>(set);
  }
}

