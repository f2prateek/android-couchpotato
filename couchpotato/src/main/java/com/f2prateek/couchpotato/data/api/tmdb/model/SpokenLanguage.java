package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class SpokenLanguage implements Parcelable {
  private static final String FIELD_ISO_639_1 = "iso_639_1";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ISO_639_1)
  private String iso6391;
  @SerializedName(FIELD_NAME)
  private String name;

  public String getIso6391() {
    return iso6391;
  }

  public String getName() {
    return name;
  }

  public SpokenLanguage(Parcel in) {
    iso6391 = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<SpokenLanguage> CREATOR =
      new Parcelable.Creator<SpokenLanguage>() {
        public SpokenLanguage createFromParcel(Parcel in) {
          return new SpokenLanguage(in);
        }

        public SpokenLanguage[] newArray(int size) {
          return new SpokenLanguage[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(iso6391);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "iso6391 = " + iso6391 + ", name = " + name;
  }
}