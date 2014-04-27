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