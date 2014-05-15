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

package com.f2prateek.couchpotato.data.api.couchpotato.model.file;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/** Response for /file.types */
public class FileTypesResponse implements Parcelable {
  private static final String FIELD_TYPES = "types";

  @SerializedName(FIELD_TYPES)
  private List<FileType> fileTypes;

  public List<FileType> getTypes() {
    return fileTypes;
  }

  public FileTypesResponse(Parcel in) {
    new ArrayList<FileType>();
    in.readTypedList(fileTypes, FileType.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<FileTypesResponse> CREATOR =
      new Parcelable.Creator<FileTypesResponse>() {
        public FileTypesResponse createFromParcel(Parcel in) {
          return new FileTypesResponse(in);
        }

        public FileTypesResponse[] newArray(int size) {
          return new FileTypesResponse[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(fileTypes);
  }
}