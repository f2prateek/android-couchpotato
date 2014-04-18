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
import java.util.List;

public class TMDbConfiguration {
  public ImagesConfiguration images;

  public static class ImagesConfiguration {
    @SerializedName("base_url")
    public String baseUrl;

    @SerializedName("secure_base_url")
    public String secureBaseUrl;

    @SerializedName("poster_sizes")
    public List<String> posterSizes;

    @SerializedName("backdrop_sizes")
    public List<String> backdropSizes;

    @SerializedName("profile_sizes")
    public List<String> profileSizes;

    @SerializedName("logo_sizes")
    public List<String> logoSizes;

    /** Get a poster. */
    public String getPosterUrl(String path) {
      return baseUrl + posterSizes.get(2) + path;
    }

    /** Get a backdrop. */
    public String getBackdropUrl(String path) {
      return baseUrl + backdropSizes.get(1) + path;
    }
  }
}
