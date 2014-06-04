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
import com.f2prateek.couchpotato.util.Strings;
import com.google.gson.annotations.SerializedName;

public class ProductionCountry implements Parcelable, Strings.Displayable {
  private static final String FIELD_ISO_3166_1 = "iso_3166_1";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ISO_3166_1)
  private String iso31661;
  @SerializedName(FIELD_NAME)
  private String name;

  public String getIso31661() {
    return iso31661;
  }

  public String getName() {
    return name;
  }

  public ProductionCountry(Parcel in) {
    iso31661 = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<ProductionCountry> CREATOR =
      new Parcelable.Creator<ProductionCountry>() {
        public ProductionCountry createFromParcel(Parcel in) {
          return new ProductionCountry(in);
        }

        public ProductionCountry[] newArray(int size) {
          return new ProductionCountry[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(iso31661);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "iso31661 = " + iso31661 + ", name = " + name;
  }

  @Override public String displayText() {
    return name;
  }
}