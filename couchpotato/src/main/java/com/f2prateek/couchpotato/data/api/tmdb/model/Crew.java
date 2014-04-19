package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Crew implements Parcelable, Configuration.Configurable {

  private static final String FIELD_ID = "id";
  private static final String FIELD_DEPARTMENT = "department";
  private static final String FIELD_PROFILE_PATH = "profile_path";
  private static final String FIELD_CREDIT_ID = "credit_id";
  private static final String FIELD_NAME = "name";
  private static final String FIELD_JOB = "job";

  @SerializedName(FIELD_ID)
  private long mId;
  @SerializedName(FIELD_DEPARTMENT)
  private String mDepartment;
  @SerializedName(FIELD_PROFILE_PATH)
  private String mProfilePath;
  @SerializedName(FIELD_CREDIT_ID)
  private String mCreditId;
  @SerializedName(FIELD_NAME)
  private String mName;
  @SerializedName(FIELD_JOB)
  private String mJob;

  public Crew() {

  }

  public long getId() {
    return mId;
  }

  public String getDepartment() {
    return mDepartment;
  }

  public String getProfilePath() {
    return mProfilePath;
  }

  public String getCreditId() {
    return mCreditId;
  }

  public String getName() {
    return mName;
  }

  public String getJob() {
    return mJob;
  }

  @Override public void setConfiguration(Configuration configuration) {
    // todo: implement
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Crew) {
      return ((Crew) obj).getId() == mId;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ((Long) mId).hashCode();
  }

  public Crew(Parcel in) {
    mId = in.readLong();
    mDepartment = in.readString();
    mProfilePath = in.readString();
    mCreditId = in.readString();
    mName = in.readString();
    mJob = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Crew> CREATOR = new Parcelable.Creator<Crew>() {
    public Crew createFromParcel(Parcel in) {
      return new Crew(in);
    }

    public Crew[] newArray(int size) {
      return new Crew[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(mId);
    dest.writeString(mDepartment);
    dest.writeString(mProfilePath);
    dest.writeString(mCreditId);
    dest.writeString(mName);
    dest.writeString(mJob);
  }

  @Override
  public String toString() {
    return "id = "
        + mId
        + ", department = "
        + mDepartment
        + ", profilePath = "
        + mProfilePath
        + ", creditId = "
        + mCreditId
        + ", name = "
        + mName
        + ", job = "
        + mJob;
  }
}