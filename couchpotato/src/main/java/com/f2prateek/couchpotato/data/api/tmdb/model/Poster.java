package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Poster implements Parcelable, Configuration.Configurable {

  private static final String FIELD_ISO_639_1 = "iso_639_1";
  private static final String FIELD_ID = "id";
  private static final String FIELD_HEIGHT = "height";
  private static final String FIELD_VOTE_AVERAGE = "vote_average";
  private static final String FIELD_FILE_PATH = "file_path";
  private static final String FIELD_VOTE_COUNT = "vote_count";
  private static final String FIELD_WIDTH = "width";
  private static final String FIELD_ASPECT_RATIO = "aspect_ratio";

  @SerializedName(FIELD_ISO_639_1)
  private String iso6391;
  @SerializedName(FIELD_ID)
  private String id;
  @SerializedName(FIELD_HEIGHT)
  private int height;
  @SerializedName(FIELD_VOTE_AVERAGE)
  private double voteAverage;
  @SerializedName(FIELD_FILE_PATH)
  private String filePath;
  @SerializedName(FIELD_VOTE_COUNT)
  private int voteCount;
  @SerializedName(FIELD_WIDTH)
  private int width;
  @SerializedName(FIELD_ASPECT_RATIO)
  private double aspectRatio;

  public Poster() {

  }

  public String getIso6391() {
    return iso6391;
  }

  public String getId() {
    return id;
  }

  public int getHeight() {
    return height;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public String getFilePath() {
    return filePath;
  }

  public int getVoteCount() {
    return voteCount;
  }

  public int getWidth() {
    return width;
  }

  public double getAspectRatio() {
    return aspectRatio;
  }

  @Override public void setConfiguration(Configuration configuration) {
    filePath = configuration.getPosterUrl(filePath);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj instanceof Poster) {
      return ((Poster) obj).getId() == id;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public Poster(Parcel in) {
    iso6391 = in.readString();
    id = in.readString();
    height = in.readInt();
    voteAverage = in.readDouble();
    filePath = in.readString();
    voteCount = in.readInt();
    width = in.readInt();
    aspectRatio = in.readDouble();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Poster> CREATOR = new Parcelable.Creator<Poster>() {
    public Poster createFromParcel(Parcel in) {
      return new Poster(in);
    }

    public Poster[] newArray(int size) {
      return new Poster[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(iso6391);
    dest.writeString(id);
    dest.writeInt(height);
    dest.writeDouble(voteAverage);
    dest.writeString(filePath);
    dest.writeInt(voteCount);
    dest.writeInt(width);
    dest.writeDouble(aspectRatio);
  }

  @Override
  public String toString() {
    return "iso6391 = "
        + iso6391
        + ", id = "
        + id
        + ", height = "
        + height
        + ", voteAverage = "
        + voteAverage
        + ", filePath = "
        + filePath
        + ", voteCount = "
        + voteCount
        + ", width = "
        + width
        + ", aspectRatio = "
        + aspectRatio;
  }
}