package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Cast implements Parcelable, Configuration.Configurable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_CAST_ID = "cast_id";
  private static final String FIELD_ORDER = "order";
  private static final String FIELD_PROFILE_PATH = "profile_path";
  private static final String FIELD_CHARACTER = "character";
  private static final String FIELD_CREDIT_ID = "credit_id";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_CAST_ID)
  private int castId;
  @SerializedName(FIELD_ORDER)
  private int order;
  @SerializedName(FIELD_PROFILE_PATH)
  private String profilePath;
  @SerializedName(FIELD_CHARACTER)
  private String character;
  @SerializedName(FIELD_CREDIT_ID)
  private String creditId;
  @SerializedName(FIELD_NAME)
  private String name;

  public long getId() {
    return id;
  }

  public int getCastId() {
    return castId;
  }

  public int getOrder() {
    return order;
  }

  public String getProfilePath() {
    return profilePath;
  }

  public String getCharacter() {
    return character;
  }

  public String getCreditId() {
    return creditId;
  }

  public String getName() {
    return name;
  }

  @Override public void setConfiguration(Configuration configuration) {
    profilePath = configuration.getProfileImage(profilePath);
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof Cast && ((Cast) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public Cast(Parcel in) {
    id = in.readLong();
    castId = in.readInt();
    order = in.readInt();
    profilePath = in.readString();
    character = in.readString();
    creditId = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
    public Cast createFromParcel(Parcel in) {
      return new Cast(in);
    }

    public Cast[] newArray(int size) {
      return new Cast[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeInt(castId);
    dest.writeInt(order);
    dest.writeString(profilePath);
    dest.writeString(character);
    dest.writeString(creditId);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "id = "
        + id
        + ", castId = "
        + castId
        + ", order = "
        + order
        + ", profilePath = "
        + profilePath
        + ", character = "
        + character
        + ", creditId = "
        + creditId
        + ", name = "
        + name;
  }
}