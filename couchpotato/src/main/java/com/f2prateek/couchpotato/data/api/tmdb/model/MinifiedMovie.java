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

public class MinifiedMovie implements Parcelable, Configuration.Configurable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_ORIGINAL_TITLE = "original_title";
  private static final String FIELD_BACKDROP_PATH = "backdrop_path";
  private static final String FIELD_VOTE_AVERAGE = "vote_average";
  private static final String FIELD_RELEASE_DATE = "release_date";
  private static final String FIELD_TITLE = "title";
  private static final String FIELD_ADULT = "adult";
  private static final String FIELD_VOTE_COUNT = "vote_count";
  private static final String FIELD_POSTER_PATH = "poster_path";
  private static final String FIELD_POPULARITY = "popularity";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_ORIGINAL_TITLE)
  private String originalTitle;
  @SerializedName(FIELD_BACKDROP_PATH)
  private String backdropPath;
  @SerializedName(FIELD_VOTE_AVERAGE)
  private double voteAverage;
  @SerializedName(FIELD_RELEASE_DATE)
  private String releaseDate;
  @SerializedName(FIELD_TITLE)
  private String title;
  @SerializedName(FIELD_ADULT)
  private boolean adult;
  @SerializedName(FIELD_VOTE_COUNT)
  private int voteCount;
  @SerializedName(FIELD_POSTER_PATH)
  private String posterPath;
  @SerializedName(FIELD_POPULARITY)
  private double popularity;

  public long getId() {
    return id;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public String getTitle() {
    return title;
  }

  public boolean isAdult() {
    return adult;
  }

  public int getVoteCount() {
    return voteCount;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public double getPopularity() {
    return popularity;
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    posterPath = configuration.getPosterUrl(posterPath);
    backdropPath = configuration.getBackdropUrl(backdropPath);
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof MinifiedMovie && ((MinifiedMovie) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public MinifiedMovie(Parcel in) {
    id = in.readLong();
    originalTitle = in.readString();
    backdropPath = in.readString();
    voteAverage = in.readDouble();
    releaseDate = in.readString();
    title = in.readString();
    adult = in.readInt() == 1;
    voteCount = in.readInt();
    posterPath = in.readString();
    popularity = in.readDouble();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MinifiedMovie> CREATOR = new Creator<MinifiedMovie>() {
    public MinifiedMovie createFromParcel(Parcel in) {
      return new MinifiedMovie(in);
    }

    public MinifiedMovie[] newArray(int size) {
      return new MinifiedMovie[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeString(originalTitle);
    dest.writeString(backdropPath);
    dest.writeDouble(voteAverage);
    dest.writeString(releaseDate);
    dest.writeString(title);
    dest.writeInt(adult ? 1 : 0);
    dest.writeInt(voteCount);
    dest.writeString(posterPath);
    dest.writeDouble(popularity);
  }

  @Override
  public String toString() {
    return "id = "
        + id
        + ", originalTitle = "
        + originalTitle
        + ", backdropPath = "
        + backdropPath
        + ", voteAverage = "
        + voteAverage
        + ", releaseDate = "
        + releaseDate
        + ", title = "
        + title
        + ", adult = "
        + adult
        + ", voteCount = "
        + voteCount
        + ", posterPath = "
        + posterPath
        + ", popularity = "
        + popularity;
  }
}