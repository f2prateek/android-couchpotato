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

public class ProfileType implements Parcelable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_FINISH = "finish";
  private static final String FIELD_ORDER = "order";
  private static final String FIELD_QUALITY_ID = "quality_id";
  private static final String FIELD_WAIT_FOR = "wait_for";

  @SerializedName(FIELD_ID)
  private int id;
  @SerializedName(FIELD_FINISH)
  private boolean finish;
  @SerializedName(FIELD_ORDER)
  private int order;
  @SerializedName(FIELD_QUALITY_ID)
  private int qualityId;
  @SerializedName(FIELD_WAIT_FOR)
  private int waitFor;

  public int getId() {
    return id;
  }

  public boolean isFinish() {
    return finish;
  }

  public int getOrder() {
    return order;
  }

  public int getQualityId() {
    return qualityId;
  }

  public int getWaitFor() {
    return waitFor;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ProfileType && ((ProfileType) obj).id == id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  public ProfileType(Parcel in) {
    id = in.readInt();
    finish = in.readInt() == 1;
    order = in.readInt();
    qualityId = in.readInt();
    waitFor = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProfileType> CREATOR = new Creator<ProfileType>() {
    public ProfileType createFromParcel(Parcel in) {
      return new ProfileType(in);
    }

    public ProfileType[] newArray(int size) {
      return new ProfileType[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeInt(finish ? 1 : 0);
    dest.writeInt(order);
    dest.writeInt(qualityId);
    dest.writeInt(waitFor);
  }
}