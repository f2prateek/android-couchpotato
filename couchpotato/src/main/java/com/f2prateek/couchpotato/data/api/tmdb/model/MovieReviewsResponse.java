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

public class MovieReviewsResponse implements Parcelable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_TOTAL_RESULTS = "total_results";
  private static final String FIELD_RESULTS = "results";
  private static final String FIELD_TOTAL_PAGES = "total_pages";
  private static final String FIELD_PAGE = "page";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_TOTAL_RESULTS)
  private int totalResults;
  @SerializedName(FIELD_RESULTS)
  private List<MovieReview> movieReviews;
  @SerializedName(FIELD_TOTAL_PAGES)
  private int totalPages;
  @SerializedName(FIELD_PAGE)
  private int page;

  public long getId() {
    return id;
  }

  public int getTotalResult() {
    return totalResults;
  }

  public List<MovieReview> getResults() {
    return movieReviews;
  }

  public int getTotalPage() {
    return totalPages;
  }

  public int getPage() {
    return page;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MovieReviewsResponse) {
      return ((MovieReviewsResponse) obj).getId() == id;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public MovieReviewsResponse(Parcel in) {
    id = in.readLong();
    totalResults = in.readInt();
    movieReviews = new ArrayList<MovieReview>();
    in.readTypedList(movieReviews, MovieReview.CREATOR);
    totalPages = in.readInt();
    page = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MovieReviewsResponse> CREATOR = new Creator<MovieReviewsResponse>() {
    public MovieReviewsResponse createFromParcel(Parcel in) {
      return new MovieReviewsResponse(in);
    }

    public MovieReviewsResponse[] newArray(int size) {
      return new MovieReviewsResponse[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeInt(totalResults);
    dest.writeTypedList(movieReviews);
    dest.writeInt(totalPages);
    dest.writeInt(page);
  }

  @Override
  public String toString() {
    return "id = "
        + id
        + ", totalResult = "
        + totalResults
        + ", results = "
        + movieReviews
        + ", totalPage = "
        + totalPages
        + ", page = "
        + page;
  }
}