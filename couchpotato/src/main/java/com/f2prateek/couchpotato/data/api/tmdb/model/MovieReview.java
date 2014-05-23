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

public class MovieReview implements Parcelable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_URL = "url";
  private static final String FIELD_CONTENT = "content";
  private static final String FIELD_AUTHOR = "author";

  @SerializedName(FIELD_ID)
  private String id;
  @SerializedName(FIELD_URL)
  private String url;
  @SerializedName(FIELD_CONTENT)
  private String content;
  @SerializedName(FIELD_AUTHOR)
  private String author;

  public String getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public String getContent() {
    return content;
  }

  public String getAuthor() {
    return author;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MovieReview) {
      return ((MovieReview) obj).getId() == id;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public MovieReview(Parcel in) {
    id = in.readString();
    url = in.readString();
    content = in.readString();
    author = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
    public MovieReview createFromParcel(Parcel in) {
      return new MovieReview(in);
    }

    public MovieReview[] newArray(int size) {
      return new MovieReview[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(url);
    dest.writeString(content);
    dest.writeString(author);
  }

  @Override
  public String toString() {
    return "id = " + id + ", url = " + url + ", content = " + content + ", author = " + author;
  }
}