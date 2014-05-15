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

public class Backdrop implements Parcelable, Configuration.Configurable {
  private static final String FIELD_ISO_639_1 = "iso_639_1";
  private static final String FIELD_HEIGHT = "height";
  private static final String FIELD_VOTE_AVERAGE = "vote_average";
  private static final String FIELD_FILE_PATH = "file_path";
  private static final String FIELD_VOTE_COUNT = "vote_count";
  private static final String FIELD_WIDTH = "width";
  private static final String FIELD_ASPECT_RATIO = "aspect_ratio";

  @SerializedName(FIELD_ISO_639_1)
  private String iso6391;
  @SerializedName(FIELD_HEIGHT)
  private int height;
  @SerializedName(FIELD_VOTE_AVERAGE)
  private double voteAverage;
  @SerializedName(FIELD_FILE_PATH)
  private String filePath;
  @SerializedName(FIELD_VOTE_COUNT)
  private int voteCount;
  @SerializedName(FIELD_WIDTH)
  private int width;
  @SerializedName(FIELD_ASPECT_RATIO)
  private double aspectRatio;

  public String getIso6391() {
    return iso6391;
  }

  public int getHeight() {
    return height;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public String getFilePath() {
    return filePath;
  }

  public int getVoteCount() {
    return voteCount;
  }

  public int getWidth() {
    return width;
  }

  public double getAspectRatio() {
    return aspectRatio;
  }

  @Override public void setConfiguration(Configuration configuration) {
    filePath = configuration.getBackdropUrl(filePath);
  }

  public Backdrop(Parcel in) {
    iso6391 = in.readString();
    height = in.readInt();
    voteAverage = in.readDouble();
    filePath = in.readString();
    voteCount = in.readInt();
    width = in.readInt();
    aspectRatio = in.readDouble();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Backdrop> CREATOR = new Parcelable.Creator<Backdrop>() {
    public Backdrop createFromParcel(Parcel in) {
      return new Backdrop(in);
    }

    public Backdrop[] newArray(int size) {
      return new Backdrop[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(iso6391);
    dest.writeInt(height);
    dest.writeDouble(voteAverage);
    dest.writeString(filePath);
    dest.writeInt(voteCount);
    dest.writeInt(width);
    dest.writeDouble(aspectRatio);
  }

  @Override
  public String toString() {
    return "iso6391 = "
        + iso6391
        + ", height = "
        + height
        + ", voteAverage = "
        + voteAverage
        + ", filePath = "
        + filePath
        + ", voteCount = "
        + voteCount
        + ", width = "
        + width
        + ", aspectRatio = "
        + aspectRatio;
  }
}