package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MovieCreditsResponse implements Parcelable, Configuration.Configurable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_CREW = "crew";
  private static final String FIELD_CAST = "cast";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_CREW)
  private List<Crew> crews;
  @SerializedName(FIELD_CAST)
  private List<Cast> casts;

  public long getId() {
    return id;
  }

  public List<Crew> getCrews() {
    return crews;
  }

  public List<Cast> getCasts() {
    return casts;
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    for (Crew crew : crews) {
      crew.setConfiguration(configuration);
    }
    for (Cast cast : casts) {
      cast.setConfiguration(configuration);
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this
        || obj instanceof MovieCreditsResponse && ((MovieCreditsResponse) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public MovieCreditsResponse(Parcel in) {
    id = in.readLong();
    crews = new ArrayList<>();
    in.readTypedList(crews, Crew.CREATOR);
    casts = new ArrayList<>();
    in.readTypedList(casts, Cast.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<MovieCreditsResponse> CREATOR =
      new Parcelable.Creator<MovieCreditsResponse>() {
        public MovieCreditsResponse createFromParcel(Parcel in) {
          return new MovieCreditsResponse(in);
        }

        public MovieCreditsResponse[] newArray(int size) {
          return new MovieCreditsResponse[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeTypedList(crews);
    dest.writeTypedList(casts);
  }

  @Override
  public String toString() {
    return "id = " + id + ", crews = " + crews + ", casts = " + casts;
  }
}