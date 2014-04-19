package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Configuration implements Parcelable {

  private static final String FIELD_CHANGE_KEYS = "change_keys";
  private static final String FIELD_IMAGES = "images";

  @SerializedName(FIELD_CHANGE_KEYS)
  private List<String> changeKeys;
  @SerializedName(FIELD_IMAGES)
  private ImageSizes imageSizes;

  public interface Configurable {
    void setConfiguration(Configuration configuration);
  }

  public Configuration() {

  }

  public List<String> getChangeKeys() {
    return changeKeys;
  }

  public ImageSizes getImageSizes() {
    return imageSizes;
  }

  public Configuration(Parcel in) {
    in.readArrayList(String.class.getClassLoader());
    imageSizes = in.readParcelable(ImageSizes.class.getClassLoader());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Configuration> CREATOR =
      new Parcelable.Creator<Configuration>() {
        public Configuration createFromParcel(Parcel in) {
          return new Configuration(in);
        }

        public Configuration[] newArray(int size) {
          return new Configuration[size];
        }
      };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeList(changeKeys);
    dest.writeParcelable(imageSizes, flags);
  }

  @Override
  public String toString() {
    return "changeKeys = " + changeKeys + ", image = " + imageSizes;
  }
}