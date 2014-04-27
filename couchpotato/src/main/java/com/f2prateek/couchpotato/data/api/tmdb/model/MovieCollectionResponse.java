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
import java.util.ArrayList;
import java.util.List;

public class MovieCollectionResponse implements Parcelable {
  private static final String FIELD_TOTAL_RESULTS = "total_results";
  private static final String FIELD_RESULTS = "results";
  private static final String FIELD_TOTAL_PAGES = "total_pages";
  private static final String FIELD_PAGE = "page";

  @SerializedName(FIELD_TOTAL_RESULTS)
  private int totalResult;
  @SerializedName(FIELD_RESULTS)
  private List<MinifiedMovie> minifiedMovies;
  @SerializedName(FIELD_TOTAL_PAGES)
  private int totalPage;
  @SerializedName(FIELD_PAGE)
  private int page;

  public int getTotalResult() {
    return totalResult;
  }

  public List<MinifiedMovie> getResults() {
    return minifiedMovies;
  }

  public int getTotalPage() {
    return totalPage;
  }

  public int getPage() {
    return page;
  }

  public MovieCollectionResponse(Parcel in) {
    totalResult = in.readInt();
    minifiedMovies = new ArrayList<>();
    in.readTypedList(minifiedMovies, MinifiedMovie.CREATOR);
    totalPage = in.readInt();
    page = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MovieCollectionResponse> CREATOR =
      new Creator<MovieCollectionResponse>() {
        public MovieCollectionResponse createFromParcel(Parcel in) {
          return new MovieCollectionResponse(in);
        }

        public MovieCollectionResponse[] newArray(int size) {
          return new MovieCollectionResponse[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(totalResult);
    dest.writeTypedList(minifiedMovies);
    dest.writeInt(totalPage);
    dest.writeInt(page);
  }

  @Override
  public String toString() {
    return "totalResult = "
        + totalResult
        + ", results = "
        + minifiedMovies
        + ", totalPage = "
        + totalPage
        + ", page = "
        + page;
  }
}