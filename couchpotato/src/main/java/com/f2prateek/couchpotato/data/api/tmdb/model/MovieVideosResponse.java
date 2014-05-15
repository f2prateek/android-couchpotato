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
import java.util.ArrayList;
import java.util.List;

public class MovieVideosResponse implements Parcelable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_RESULTS = "results";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_RESULTS)
  private List<Video> videos;

  public long getId() {
    return id;
  }

  public List<Video> getResults() {
    return videos;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this
        || obj instanceof MovieVideosResponse && ((MovieVideosResponse) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public MovieVideosResponse(Parcel in) {
    id = in.readLong();
    videos = new ArrayList<>();
    in.readTypedList(videos, Video.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<MovieVideosResponse> CREATOR =
      new Parcelable.Creator<MovieVideosResponse>() {
        public MovieVideosResponse createFromParcel(Parcel in) {
          return new MovieVideosResponse(in);
        }

        public MovieVideosResponse[] newArray(int size) {
          return new MovieVideosResponse[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeTypedList(videos);
  }

  @Override
  public String toString() {
    return "id = " + id + ", results = " + videos;
  }
}