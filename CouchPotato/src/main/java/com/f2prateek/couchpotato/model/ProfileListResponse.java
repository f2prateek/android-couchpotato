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

package com.f2prateek.couchpotato.model;

import com.f2prateek.couchpotato.model.movie.Type;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/** Response for /profile.list */
public class ProfileListResponse {
  @SerializedName("list")
  public ArrayList<Profile> profiles;
  public boolean success;

  public static class Profile {
    public boolean core;
    public boolean hide;
    public int id;
    public String label;
    public int order;
    public List<Type> types;

    @Override public String toString() {
      return "Profile{" +
          "core=" + core +
          ", hide=" + hide +
          ", id=" + id +
          ", label='" + label + '\'' +
          ", order=" + order +
          ", types=" + types +
          '}';
    }
  }

  @Override public String toString() {
    return "ProfileListResponse{" +
        "profiles=" + profiles +
        ", success=" + success +
        '}';
  }
}
