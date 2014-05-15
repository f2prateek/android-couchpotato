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

package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ImageSizes implements Parcelable {
  private static final String FIELD_POSTER_SIZES = "poster_sizes";
  private static final String FIELD_BACKDROP_SIZES = "backdrop_sizes";
  private static final String FIELD_STILL_SIZES = "still_sizes";
  private static final String FIELD_BASE_URL = "base_url";
  private static final String FIELD_PROFILE_SIZES = "profile_sizes";
  private static final String FIELD_SECURE_BASE_URL = "secure_base_url";
  private static final String FIELD_LOGO_SIZES = "logo_sizes";

  @SerializedName(FIELD_POSTER_SIZES)
  private List<String> posterSizes;
  @SerializedName(FIELD_BACKDROP_SIZES)
  private List<String> backdropSizes;
  @SerializedName(FIELD_STILL_SIZES)
  private List<String> stillSizes;
  @SerializedName(FIELD_BASE_URL)
  private String baseUrl;
  @SerializedName(FIELD_PROFILE_SIZES)
  private List<String> profileSizes;
  @SerializedName(FIELD_SECURE_BASE_URL)
  private String secureBaseUrl;
  @SerializedName(FIELD_LOGO_SIZES)
  private List<String> logoSizes;

  public List<String> getPosterSizes() {
    return posterSizes;
  }

  public List<String> getBackdropSizes() {
    return backdropSizes;
  }

  public List<String> getStillSizes() {
    return stillSizes;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public List<String> getProfileSizes() {
    return profileSizes;
  }

  public String getSecureBaseUrl() {
    return secureBaseUrl;
  }

  public List<String> getLogoSizes() {
    return logoSizes;
  }

  public ImageSizes(Parcel in) {
    in.readArrayList(String.class.getClassLoader());
    in.readArrayList(String.class.getClassLoader());
    in.readArrayList(String.class.getClassLoader());
    baseUrl = in.readString();
    in.readArrayList(String.class.getClassLoader());
    secureBaseUrl = in.readString();
    in.readArrayList(String.class.getClassLoader());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<ImageSizes> CREATOR =
      new Parcelable.Creator<ImageSizes>() {
        public ImageSizes createFromParcel(Parcel in) {
          return new ImageSizes(in);
        }

        public ImageSizes[] newArray(int size) {
          return new ImageSizes[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeList(posterSizes);
    dest.writeList(backdropSizes);
    dest.writeList(stillSizes);
    dest.writeString(baseUrl);
    dest.writeList(profileSizes);
    dest.writeString(secureBaseUrl);
    dest.writeList(logoSizes);
  }

  @Override
  public String toString() {
    return "posterSizes = "
        + posterSizes
        + ", backdropSizes = "
        + backdropSizes
        + ", stillSizes = "
        + stillSizes
        + ", baseUrl = "
        + baseUrl
        + ", profileSizes = "
        + profileSizes
        + ", secureBaseUrl = "
        + secureBaseUrl
        + ", logoSizes = "
        + logoSizes;
  }
}