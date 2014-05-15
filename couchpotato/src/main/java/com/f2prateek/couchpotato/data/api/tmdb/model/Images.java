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

public class Images implements Parcelable, Configuration.Configurable {
  private static final String FIELD_BACKDROPS = "backdrops";
  private static final String FIELD_ID = "id";
  private static final String FIELD_POSTERS = "posters";

  @SerializedName(FIELD_BACKDROPS)
  private List<Backdrop> backdrops;
  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_POSTERS)
  private List<Poster> posters;

  public List<Backdrop> getBackdrops() {
    return backdrops;
  }

  public long getId() {
    return id;
  }

  public List<Poster> getPosters() {
    return posters;
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    for (Poster poster : posters) {
      poster.setConfiguration(configuration);
    }
    for (Backdrop backdrop : backdrops) {
      backdrop.setConfiguration(configuration);
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof Images && ((Images) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public Images(Parcel in) {
    backdrops = new ArrayList<Backdrop>();
    in.readTypedList(backdrops, Backdrop.CREATOR);
    id = in.readLong();
    posters = new ArrayList<Poster>();
    in.readTypedList(posters, Poster.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Images> CREATOR = new Parcelable.Creator<Images>() {
    public Images createFromParcel(Parcel in) {
      return new Images(in);
    }

    public Images[] newArray(int size) {
      return new Images[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(backdrops);
    dest.writeLong(id);
    dest.writeTypedList(posters);
  }

  @Override
  public String toString() {
    return "backdrops = " + backdrops + ", id = " + id + ", posters = " + posters;
  }
}