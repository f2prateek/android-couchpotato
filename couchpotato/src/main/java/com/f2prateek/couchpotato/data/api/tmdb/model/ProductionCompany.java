package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ProductionCompany implements Parcelable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_NAME)
  private String name;

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this
        || obj instanceof ProductionCompany && ((ProductionCompany) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public ProductionCompany(Parcel in) {
    id = in.readLong();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<ProductionCompany> CREATOR =
      new Parcelable.Creator<ProductionCompany>() {
        public ProductionCompany createFromParcel(Parcel in) {
          return new ProductionCompany(in);
        }

        public ProductionCompany[] newArray(int size) {
          return new ProductionCompany[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "id = " + id + ", name = " + name;
  }
}