package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ProductionCountry implements Parcelable {
  private static final String FIELD_ISO_3166_1 = "iso_3166_1";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ISO_3166_1)
  private String iso31661;
  @SerializedName(FIELD_NAME)
  private String name;

  public String getIso31661() {
    return iso31661;
  }

  public String getName() {
    return name;
  }

  public ProductionCountry(Parcel in) {
    iso31661 = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<ProductionCountry> CREATOR =
      new Parcelable.Creator<ProductionCountry>() {
        public ProductionCountry createFromParcel(Parcel in) {
          return new ProductionCountry(in);
        }

        public ProductionCountry[] newArray(int size) {
          return new ProductionCountry[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(iso31661);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "iso31661 = " + iso31661 + ", name = " + name;
  }
}