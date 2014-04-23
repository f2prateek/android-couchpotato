package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {
  private static final String FIELD_ISO_639_1 = "iso_639_1";
  private static final String FIELD_TYPE = "type";
  private static final String FIELD_KEY = "key";
  private static final String FIELD_SIZE = "size";
  private static final String FIELD_ID = "id";
  private static final String FIELD_SITE = "site";
  private static final String FIELD_NAME = "name";

  @SerializedName(FIELD_ISO_639_1)
  private String iso6391;
  @SerializedName(FIELD_TYPE)
  private String type;
  @SerializedName(FIELD_KEY)
  private String key;
  @SerializedName(FIELD_SIZE)
  private int size;
  @SerializedName(FIELD_ID)
  private String id;
  @SerializedName(FIELD_SITE)
  private String site;
  @SerializedName(FIELD_NAME)
  private String name;

  public Video() {

  }

  public String getIso6391() {
    return iso6391;
  }

  public String getType() {
    return type;
  }

  public String getKey() {
    return key;
  }

  public int getSize() {
    return size;
  }

  public String getId() {
    return id;
  }

  public String getSite() {
    return site;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return "http://www.youtube.com/watch?v=" + key;
  }

  public String getThumbnail() {
    return "https://img.youtube.com/vi/" + key + "/sddefault.jpg";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;

    if (obj instanceof Video) {
      return ((Video) obj).getId() == id;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public Video(Parcel in) {
    iso6391 = in.readString();
    type = in.readString();
    key = in.readString();
    size = in.readInt();
    id = in.readString();
    site = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
    public Video createFromParcel(Parcel in) {
      return new Video(in);
    }

    public Video[] newArray(int size) {
      return new Video[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(iso6391);
    dest.writeString(type);
    dest.writeString(key);
    dest.writeInt(size);
    dest.writeString(id);
    dest.writeString(site);
    dest.writeString(name);
  }

  @Override
  public String toString() {
    return "iso6391 = "
        + iso6391
        + ", type = "
        + type
        + ", key = "
        + key
        + ", size = "
        + size
        + ", id = "
        + id
        + ", site = "
        + site
        + ", name = "
        + name;
  }
}