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
  private long id;
  @SerializedName(FIELD_DEPARTMENT)
  private String department;
  @SerializedName(FIELD_PROFILE_PATH)
  private String profilePath;
  @SerializedName(FIELD_CREDIT_ID)
  private String creditId;
  @SerializedName(FIELD_NAME)
  private String name;
  @SerializedName(FIELD_JOB)
  private String job;

  public Crew() {

  }

  public long getId() {
    return id;
  }

  public String getDepartment() {
    return department;
  }

  public String getProfilePath() {
    return profilePath;
  }

  public String getCreditId() {
    return creditId;
  }

  public String getName() {
    return name;
  }

  public String getJob() {
    return job;
  }

  @Override public void setConfiguration(Configuration configuration) {
    profilePath = configuration.getProfileImage(profilePath);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj instanceof Crew) {
      return ((Crew) obj).getId() == id;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public Crew(Parcel in) {
    id = in.readLong();
    department = in.readString();
    profilePath = in.readString();
    creditId = in.readString();
    name = in.readString();
    job = in.readString();
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
    dest.writeLong(id);
    dest.writeString(department);
    dest.writeString(profilePath);
    dest.writeString(creditId);
    dest.writeString(name);
    dest.writeString(job);
  }

  @Override
  public String toString() {
    return "id = "
        + id
        + ", department = "
        + department
        + ", profilePath = "
        + profilePath
        + ", creditId = "
        + creditId
        + ", name = "
        + name
        + ", job = "
        + job;
  }
}