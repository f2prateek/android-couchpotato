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

package com.f2prateek.couchpotato.data.api.couchpotato.model.app;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/** Response for /app.version */
public class AppVersionResponse implements Parcelable {
  private static final String FIELD_VERSION = "version";

  @SerializedName(FIELD_VERSION)
  private String version;

  public String getVersion() {
    return version;
  }

  public AppVersionResponse(Parcel in) {
    version = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<AppVersionResponse> CREATOR =
      new Parcelable.Creator<AppVersionResponse>() {
        public AppVersionResponse createFromParcel(Parcel in) {
          return new AppVersionResponse(in);
        }

        public AppVersionResponse[] newArray(int size) {
          return new AppVersionResponse[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(version);
  }
}