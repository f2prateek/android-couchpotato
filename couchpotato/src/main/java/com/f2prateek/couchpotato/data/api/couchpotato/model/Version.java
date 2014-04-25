package com.f2prateek.couchpotato.data.api.couchpotato.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Version implements Parcelable {

  private static final String FIELD_TYPE = "type";
  private static final String FIELD_REPR = "repr";
  private static final String FIELD_HASH = "hash";
  private static final String FIELD_DATE = "date";

  @SerializedName(FIELD_TYPE)
  private String type;
  @SerializedName(FIELD_REPR)
  private String repr;
  @SerializedName(FIELD_HASH)
  private String hash;
  @SerializedName(FIELD_DATE)
  private String date;

  public Version() {

  }

  public String getType() {
    return type;
  }

  public String getRepr() {
    return repr;
  }

  public String getHash() {
    return hash;
  }

  public String getDate() {
    return date;
  }

  public Version(Parcel in) {
    type = in.readString();
    repr = in.readString();
    hash = in.readString();
    date = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Version> CREATOR = new Creator<Version>() {
    public Version createFromParcel(Parcel in) {
      return new Version(in);
    }

    public Version[] newArray(int size) {
      return new Version[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type);
    dest.writeString(repr);
    dest.writeString(hash);
    dest.writeString(date);
  }
}