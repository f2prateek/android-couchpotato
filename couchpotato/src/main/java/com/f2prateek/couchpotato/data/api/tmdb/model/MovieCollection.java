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

package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class MovieCollection implements Parcelable, Configuration.Configurable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_BACKDROP_PATH = "backdrop_path";
  private static final String FIELD_POSTER_PATH = "poster_path";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_BACKDROP_PATH)
  private String backdropPath;
  @SerializedName(FIELD_POSTER_PATH)
  private String posterPath;
  @SerializedName(FIELD_NAME)
  private String name;

  public long getId() {
    return id;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public String getName() {
    return name;
  }

  @Override public void setConfiguration(Configuration configuration) {
    // todo:implement
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof MovieCollection && ((MovieCollection) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public MovieCollection(Parcel in) {
    id = in.readLong();
    backdropPath = in.readString();
    posterPath = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MovieCollection> CREATOR = new Creator<MovieCollection>() {
    public MovieCollection createFromParcel(Parcel in) {
      return new MovieCollection(in);
    }

    public MovieCollection[] newArray(int size) {
      return new MovieCollection[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeString(backdropPath);
    dest.writeString(posterPath);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "id = "
        + id
        + ", backdropPath = "
        + backdropPath
        + ", posterPath = "
        + posterPath
        + ", name = "
        + name;
  }
}