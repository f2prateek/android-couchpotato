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

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class TMDbMovieMinified implements Parcelable {
  public boolean adult;

  @SerializedName("backdrop_path")
  public String backdrop;

  public long id;
  @SerializedName("original_title")
  public String originalTitle;

  //public Date release_date;
  @SerializedName("poster_path")
  public String poster;

  public float popularity;
  public String title;

  @SerializedName("vote_average")
  public float voteAverage;

  @SerializedName("vote_count")
  public long voteCount;

  protected TMDbMovieMinified(Parcel in) {
    adult = in.readByte() != 0x00;
    backdrop = in.readString();
    id = in.readLong();
    originalTitle = in.readString();
    poster = in.readString();
    popularity = in.readFloat();
    title = in.readString();
    voteAverage = in.readFloat();
    voteCount = in.readLong();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte((byte) (adult ? 0x01 : 0x00));
    dest.writeString(backdrop);
    dest.writeLong(id);
    dest.writeString(originalTitle);
    dest.writeString(poster);
    dest.writeFloat(popularity);
    dest.writeString(title);
    dest.writeFloat(voteAverage);
    dest.writeLong(voteCount);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<TMDbMovieMinified> CREATOR =
      new Parcelable.Creator<TMDbMovieMinified>() {
        @Override
        public TMDbMovieMinified createFromParcel(Parcel in) {
          return new TMDbMovieMinified(in);
        }

        @Override
        public TMDbMovieMinified[] newArray(int size) {
          return new TMDbMovieMinified[size];
        }
      };
}