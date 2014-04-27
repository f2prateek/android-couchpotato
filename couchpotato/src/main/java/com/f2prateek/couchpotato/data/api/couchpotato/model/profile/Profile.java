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

package com.f2prateek.couchpotato.data.api.couchpotato.model.profile;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Profile implements Parcelable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_ORDER = "order";
  private static final String FIELD_TYPES = "types";
  private static final String FIELD_HIDE = "hide";
  private static final String FIELD_LABEL = "label";
  private static final String FIELD_CORE = "core";

  @SerializedName(FIELD_ID)
  private int id;
  @SerializedName(FIELD_ORDER)
  private int order;
  @SerializedName(FIELD_TYPES)
  private List<ProfileType> types;
  @SerializedName(FIELD_HIDE)
  private boolean hide;
  @SerializedName(FIELD_LABEL)
  private String label;
  @SerializedName(FIELD_CORE)
  private boolean core;

  public int getId() {
    return id;
  }

  public int getOrder() {
    return order;
  }

  public List<ProfileType> getTypes() {
    return types;
  }

  public boolean isHide() {
    return hide;
  }

  public String getLabel() {
    return label;
  }

  public boolean isCore() {
    return core;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Profile && ((Profile) obj).id == id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  public Profile(Parcel in) {
    id = in.readInt();
    order = in.readInt();
    new ArrayList<ProfileType>();
    in.readTypedList(types, ProfileType.CREATOR);
    hide = in.readInt() == 1;
    label = in.readString();
    core = in.readInt() == 1;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Profile> CREATOR = new Creator<Profile>() {
    public Profile createFromParcel(Parcel in) {
      return new Profile(in);
    }

    public Profile[] newArray(int size) {
      return new Profile[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeInt(order);
    dest.writeTypedList(types);
    dest.writeInt(hide ? 1 : 0);
    dest.writeString(label);
    dest.writeInt(core ? 1 : 0);
  }
}