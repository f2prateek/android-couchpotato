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

public class Casts {
  public int id;
  public ArrayList<Cast> cast;
  public ArrayList<Crew> crew;

  public static class Cast {
    public int id;
    public String name;
    public String character;
    public int order;
    public String profile_path;

    public String getImage(Configuration configuration) {
      if (profile_path == null) {
        return null;
      }
      return configuration.images.base_url
          + configuration.images.profile_sizes.get(0)
          + profile_path;
    }
  }

  public static class Crew {
    public int id;
    public String name;
    public String department;
    public String job;
    public String profile_path;

    public String getImage(Configuration configuration) {
      if (profile_path == null) {
        return null;
      }
      return configuration.images.base_url
          + configuration.images.profile_sizes.get(0)
          + profile_path;
    }
  }
}
