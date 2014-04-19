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
  private long mId;
  @SerializedName(FIELD_CAST_ID)
  private int mCastId;
  @SerializedName(FIELD_ORDER)
  private int mOrder;
  @SerializedName(FIELD_PROFILE_PATH)
  private String mProfilePath;
  @SerializedName(FIELD_CHARACTER)
  private String mCharacter;
  @SerializedName(FIELD_CREDIT_ID)
  private String mCreditId;
  @SerializedName(FIELD_NAME)
  private String mName;

  public Cast() {

  }

  public long getId() {
    return mId;
  }

  public int getCastId() {
    return mCastId;
  }

  public int getOrder() {
    return mOrder;
  }

  public String getProfilePath() {
    return mProfilePath;
  }

  public String getCharacter() {
    return mCharacter;
  }

  public String getCreditId() {
    return mCreditId;
  }

  public String getName() {
    return mName;
  }

  @Override public void setConfiguration(Configuration configuration) {
    // todo: implement
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Cast) {
      return ((Cast) obj).getId() == mId;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ((Long) mId).hashCode();
  }

  public Cast(Parcel in) {
    mId = in.readLong();
    mCastId = in.readInt();
    mOrder = in.readInt();
    mProfilePath = in.readString();
    mCharacter = in.readString();
    mCreditId = in.readString();
    mName = in.readString();
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
    dest.writeLong(mId);
    dest.writeInt(mCastId);
    dest.writeInt(mOrder);
    dest.writeString(mProfilePath);
    dest.writeString(mCharacter);
    dest.writeString(mCreditId);
    dest.writeString(mName);
  }

  @Override
  public String toString() {
    return "id = "
        + mId
        + ", castId = "
        + mCastId
        + ", order = "
        + mOrder
        + ", profilePath = "
        + mProfilePath
        + ", character = "
        + mCharacter
        + ", creditId = "
        + mCreditId
        + ", name = "
        + mName;
  }
}