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

public class FileType implements Parcelable {
  private static final String FIELD_TYPE = "type";
  private static final String FIELD_ID = "id";
  private static final String FIELD_IDENTIFIER = "identifier";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_TYPE)
  private String type;
  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_IDENTIFIER)
  private String identifier;
  @SerializedName(FIELD_NAME)
  private String name;

  public String getType() {
    return type;
  }

  public long getId() {
    return id;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof FileType && ((FileType) obj).id == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public FileType(Parcel in) {
    type = in.readString();
    id = in.readLong();
    identifier = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<FileType> CREATOR = new Parcelable.Creator<FileType>() {
    public FileType createFromParcel(Parcel in) {
      return new FileType(in);
    }

    public FileType[] newArray(int size) {
      return new FileType[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type);
    dest.writeLong(id);
    dest.writeString(identifier);
    dest.writeString(name);
  }
}