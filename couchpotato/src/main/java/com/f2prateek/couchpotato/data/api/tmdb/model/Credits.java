package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Credits implements Parcelable, Configuration.Configurable {

  private static final String FIELD_ID = "id";
  private static final String FIELD_CREW = "crew";
  private static final String FIELD_CAST = "cast";

  @SerializedName(FIELD_ID)
  private long mId;
  @SerializedName(FIELD_CREW)
  private List<Crew> mCrews;
  @SerializedName(FIELD_CAST)
  private List<Cast> mCasts;

  public Credits() {

  }

  public long getId() {
    return mId;
  }

  public List<Crew> getCrews() {
    return mCrews;
  }

  public List<Cast> getCasts() {
    return mCasts;
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    for (Crew crew : mCrews) {
      crew.setConfiguration(configuration);
    }
    for (Cast cast : mCasts) {
      cast.setConfiguration(configuration);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj instanceof Credits) {
      return ((Credits) obj).getId() == mId;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ((Long) mId).hashCode();
  }

  public Credits(Parcel in) {
    mId = in.readLong();
    mCrews = new ArrayList<Crew>();
    in.readTypedList(mCrews, Crew.CREATOR);
    mCasts = new ArrayList<Cast>();
    in.readTypedList(mCasts, Cast.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Credits> CREATOR = new Parcelable.Creator<Credits>() {
    public Credits createFromParcel(Parcel in) {
      return new Credits(in);
    }

    public Credits[] newArray(int size) {
      return new Credits[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(mId);
    dest.writeTypedList(mCrews);
    dest.writeTypedList(mCasts);
  }

  @Override
  public String toString() {
    return "id = " + mId + ", crews = " + mCrews + ", casts = " + mCasts;
  }
}