package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.f2prateek.couchpotato.util.Strings;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Configuration implements Parcelable {
  private static final String FIELD_CHANGE_KEYS = "change_keys";
  private static final String FIELD_IMAGES = "images";

  private static final String PLACEHOLDER_IMAGE =
      "http://parmeter.net/tech/wp-content/uploads/2012/09/logo.png";
  private static final String AVATAR_PLACEHOLDER =
      "https://secure.gravatar.com/avatar/ad516503a11cd5ca435acc9bb6523536?s=500";

  @SerializedName(FIELD_CHANGE_KEYS)
  private List<String> changeKeys;
  @SerializedName(FIELD_IMAGES)
  private ImageSizes imageSizes;

  public interface Configurable {
    void setConfiguration(Configuration configuration);
  }

  public List<String> getChangeKeys() {
    return changeKeys;
  }

  public ImageSizes getImageSizes() {
    return imageSizes;
  }

  public String getProfileImage(String profilePath) {
    if (Strings.isBlank(profilePath)) {
      return AVATAR_PLACEHOLDER;
    }
    return getImageSizes().getBaseUrl() + getImageSizes().getProfileSizes().get(1) + profilePath;
  }

  public String getPosterUrl(String posterPath) {
    if (Strings.isBlank(posterPath)) {
      return PLACEHOLDER_IMAGE;
    }
    return getImageSizes().getBaseUrl() + getImageSizes().getPosterSizes().get(2) + posterPath;
  }

  public String getBackdropUrl(String backdropPath) {
    if (Strings.isBlank(backdropPath)) {
      return PLACEHOLDER_IMAGE;
    }
    return getImageSizes().getBaseUrl() + getImageSizes().getBackdropSizes().get(1) + backdropPath;
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