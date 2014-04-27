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

public class ProfilesResponse implements Parcelable {
  private static final String FIELD_SUCCESS = "success";
  private static final String FIELD_LIST = "list";

  @SerializedName(FIELD_SUCCESS)
  private boolean mSuccess;
  @SerializedName(FIELD_LIST)
  private List<Profile> profiles;

  public boolean isSuccess() {
    return mSuccess;
  }

  public List<Profile> getLists() {
    return profiles;
  }

  public ProfilesResponse(Parcel in) {
    mSuccess = in.readInt() == 1;
    new ArrayList<List>();
    in.readTypedList(profiles, Profile.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProfilesResponse> CREATOR = new Creator<ProfilesResponse>() {
    public ProfilesResponse createFromParcel(Parcel in) {
      return new ProfilesResponse(in);
    }

    public ProfilesResponse[] newArray(int size) {
      return new ProfilesResponse[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mSuccess ? 1 : 0);
    dest.writeTypedList(profiles);
  }
}