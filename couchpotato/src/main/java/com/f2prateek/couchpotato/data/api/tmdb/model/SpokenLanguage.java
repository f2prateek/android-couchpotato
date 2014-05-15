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

public class SpokenLanguage implements Parcelable {
  private static final String FIELD_ISO_639_1 = "iso_639_1";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ISO_639_1)
  private String iso6391;
  @SerializedName(FIELD_NAME)
  private String name;

  public String getIso6391() {
    return iso6391;
  }

  public String getName() {
    return name;
  }

  public SpokenLanguage(Parcel in) {
    iso6391 = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<SpokenLanguage> CREATOR =
      new Parcelable.Creator<SpokenLanguage>() {
        public SpokenLanguage createFromParcel(Parcel in) {
          return new SpokenLanguage(in);
        }

        public SpokenLanguage[] newArray(int size) {
          return new SpokenLanguage[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(iso6391);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "iso6391 = " + iso6391 + ", name = " + name;
  }
}