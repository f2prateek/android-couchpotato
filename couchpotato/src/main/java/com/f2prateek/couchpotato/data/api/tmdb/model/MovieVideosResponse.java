package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MovieVideosResponse implements Parcelable {
  private static final String FIELD_ID = "id";
  private static final String FIELD_RESULTS = "results";

  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_RESULTS)
  private List<Video> videos;

  public MovieVideosResponse() {

  }

  public long getId() {
    return id;
  }

  public List<Video> getResults() {
    return videos;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;

    if (obj instanceof MovieVideosResponse) {
      return ((MovieVideosResponse) obj).getId() == id;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public MovieVideosResponse(Parcel in) {
    id = in.readLong();
    videos = new ArrayList<Video>();
    in.readTypedList(videos, Video.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<MovieVideosResponse> CREATOR =
      new Parcelable.Creator<MovieVideosResponse>() {
        public MovieVideosResponse createFromParcel(Parcel in) {
          return new MovieVideosResponse(in);
        }

        public MovieVideosResponse[] newArray(int size) {
          return new MovieVideosResponse[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeTypedList(videos);
  }

  @Override
  public String toString() {
    return "id = " + id + ", results = " + videos;
  }
}