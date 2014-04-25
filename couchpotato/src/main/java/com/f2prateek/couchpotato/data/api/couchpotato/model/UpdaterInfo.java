package com.f2prateek.couchpotato.data.api.couchpotato.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class UpdaterInfo implements Parcelable {
  private static final String FIELD_LAST_CHECK = "last_check";
  private static final String FIELD_UPDATE_VERSION = "update_version";
  private static final String FIELD_VERSION = "version";
  private static final String FIELD_BRANCH = "branch";

  @SerializedName(FIELD_LAST_CHECK)
  private double lastCheck;
  @SerializedName(FIELD_UPDATE_VERSION)
  private String updateVersion;
  @SerializedName(FIELD_VERSION)
  private Version version;
  @SerializedName(FIELD_BRANCH)
  private String branch;

  public UpdaterInfo() {

  }

  public double getLastCheck() {
    return lastCheck;
  }

  public String getUpdateVersion() {
    return updateVersion;
  }

  public Version getVersion() {
    return version;
  }

  public String getBranch() {
    return branch;
  }

  public UpdaterInfo(Parcel in) {
    lastCheck = in.readDouble();
    updateVersion = in.readString();
    version = in.readParcelable(Version.class.getClassLoader());
    branch = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<UpdaterInfo> CREATOR = new Creator<UpdaterInfo>() {
    public UpdaterInfo createFromParcel(Parcel in) {
      return new UpdaterInfo(in);
    }

    public UpdaterInfo[] newArray(int size) {
      return new UpdaterInfo[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(lastCheck);
    dest.writeString(updateVersion);
    dest.writeParcelable(version, flags);
    dest.writeString(branch);
  }

  @Override public String toString() {
    return "UpdaterInfo{"
        + "lastCheck="
        + lastCheck
        + ", updateVersion='"
        + updateVersion
        + '\''
        + ", version="
        + version
        + ", branch='"
        + branch
        + '\''
        + '}';
  }
}