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

public class Images {
  public int id;
  public ArrayList<Backdrop> backdrops;
  public ArrayList<Poster> posters;

  public static class Backdrop {
    public String file_path;
    public int width;
    public int height;
    public float aspect_ratio;
    public double vote_average;
    public int vote_count;
  }

  public static class Poster {
    public String file_path;
    public int width;
    public int height;
    public float aspect_ratio;
    public double vote_average;
    public int vote_count;
  }
}
