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

import com.f2prateek.couchpotato.util.CollectionUtils;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Images {
  private static final String POSTER_ORIGINAL = "poster_original";
  private static final String BACKDROP_ORIGINAL = "backdrop_original";

  private static final String PLACEHOLDER_IMAGE =
      "http://parmeter.net/tech/wp-content/uploads/2012/09/logo.png";

  @SerializedName(POSTER_ORIGINAL)
  public List<String> posterOriginal;
  public List<String> poster;
  @SerializedName(BACKDROP_ORIGINAL)
  public List<String> backdropOriginal;
  public List<String> backdrop;

  public String getPoster() {
    if (!CollectionUtils.isNullOrEmpty(poster)) {
      return poster.get(0);
    }
    if (!CollectionUtils.isNullOrEmpty(posterOriginal)) {
      return posterOriginal.get(0);
    }
    return PLACEHOLDER_IMAGE;
  }

  public String getBackdrop() {
    if (!CollectionUtils.isNullOrEmpty(backdrop)) {
      return backdrop.get(0);
    }
    if (!CollectionUtils.isNullOrEmpty(backdropOriginal)) {
      return backdropOriginal.get(0);
    }
    return PLACEHOLDER_IMAGE;
  }
}
