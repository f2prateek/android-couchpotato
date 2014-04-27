package com.f2prateek.couchpotato.data.api.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class TMDbMovie implements Parcelable, Configuration.Configurable {
  private static final String FIELD_TAGLINE = "tagline";
  private static final String FIELD_BACKDROP_PATH = "backdrop_path";
  private static final String FIELD_STATUS = "status";
  private static final String FIELD_GENRES = "genres";
  private static final String FIELD_RELEASE_DATE = "release_date";
  private static final String FIELD_VOTE_AVERAGE = "vote_average";
  private static final String FIELD_VOTE_COUNT = "vote_count";
  private static final String FIELD_BUDGET = "budget";
  private static final String FIELD_POPULARITY = "popularity";
  private static final String FIELD_IMDB_ID = "imdb_id";
  private static final String FIELD_SPOKEN_LANGUAGES = "spoken_languages";
  private static final String FIELD_REVENUE = "revenue";
  private static final String FIELD_ORIGINAL_TITLE = "original_title";
  private static final String FIELD_BELONGS_TO_COLLECTION = "belongs_to_collection";
  private static final String FIELD_ID = "id";
  private static final String FIELD_HOMEPAGE = "homepage";
  private static final String FIELD_PRODUCTION_COUNTRIES = "production_countries";
  private static final String FIELD_PRODUCTION_COMPANIES = "production_companies";
  private static final String FIELD_OVERVIEW = "overview";
  private static final String FIELD_TITLE = "title";
  private static final String FIELD_ADULT = "adult";
  private static final String FIELD_POSTER_PATH = "poster_path";
  private static final String FIELD_RUNTIME = "runtime";

  @SerializedName(FIELD_TAGLINE)
  private String tagline;
  @SerializedName(FIELD_BACKDROP_PATH)
  private String backdropPath;
  @SerializedName(FIELD_STATUS)
  private String status;
  @SerializedName(FIELD_GENRES)
  private List<Genre> genres;
  @SerializedName(FIELD_RELEASE_DATE)
  private String releaseDate;
  @SerializedName(FIELD_VOTE_AVERAGE)
  private double voteAverage;
  @SerializedName(FIELD_VOTE_COUNT)
  private int voteCount;
  @SerializedName(FIELD_BUDGET)
  private int budget;
  @SerializedName(FIELD_POPULARITY)
  private double popularity;
  @SerializedName(FIELD_IMDB_ID)
  private String imdbId;
  @SerializedName(FIELD_SPOKEN_LANGUAGES)
  private List<SpokenLanguage> spokenLanguages;
  @SerializedName(FIELD_REVENUE)
  private int revenue;
  @SerializedName(FIELD_ORIGINAL_TITLE)
  private String originalTitle;
  @SerializedName(FIELD_BELONGS_TO_COLLECTION)
  private MovieCollection belongsToCollection;
  @SerializedName(FIELD_ID)
  private long id;
  @SerializedName(FIELD_HOMEPAGE)
  private String homepage;
  @SerializedName(FIELD_PRODUCTION_COUNTRIES)
  private List<ProductionCountry> productionCountries;
  @SerializedName(FIELD_PRODUCTION_COMPANIES)
  private List<ProductionCompany> productionCompanies;
  @SerializedName(FIELD_OVERVIEW)
  private String overview;
  @SerializedName(FIELD_TITLE)
  private String title;
  @SerializedName(FIELD_ADULT)
  private boolean adult;
  @SerializedName(FIELD_POSTER_PATH)
  private String posterPath;
  @SerializedName(FIELD_RUNTIME)
  private int runtime;

  public String getTagline() {
    return tagline;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public String getStatus() {
    return status;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public int getVoteCount() {
    return voteCount;
  }

  public int getBudget() {
    return budget;
  }

  public double getPopularity() {
    return popularity;
  }

  public String getImdbId() {
    return imdbId;
  }

  public List<SpokenLanguage> getSpokenLanguages() {
    return spokenLanguages;
  }

  public int getRevenue() {
    return revenue;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public MovieCollection getBelongsToCollection() {
    return belongsToCollection;
  }

  public long getId() {
    return id;
  }

  public String getHomepage() {
    return homepage;
  }

  public List<ProductionCountry> getProductionCountries() {
    return productionCountries;
  }

  public List<ProductionCompany> getProductionCompanies() {
    return productionCompanies;
  }

  public String getOverview() {
    return overview;
  }

  public String getTitle() {
    return title;
  }

  public boolean isAdult() {
    return adult;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public int getRuntime() {
    return runtime;
  }

  @Override public void setConfiguration(Configuration configuration) {
    // todo: implement
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof TMDbMovie && ((TMDbMovie) obj).getId() == id;
  }

  @Override
  public int hashCode() {
    return ((Long) id).hashCode();
  }

  public TMDbMovie(Parcel in) {
    tagline = in.readString();
    backdropPath = in.readString();
    status = in.readString();
    genres = new ArrayList<>();
    in.readTypedList(genres, Genre.CREATOR);
    releaseDate = in.readString();
    voteAverage = in.readDouble();
    voteCount = in.readInt();
    budget = in.readInt();
    popularity = in.readDouble();
    imdbId = in.readString();
    spokenLanguages = new ArrayList<>();
    in.readTypedList(spokenLanguages, SpokenLanguage.CREATOR);
    revenue = in.readInt();
    originalTitle = in.readString();
    belongsToCollection = in.readParcelable(MovieCollection.class.getClassLoader());
    id = in.readLong();
    homepage = in.readString();
    productionCountries = new ArrayList<>();
    in.readTypedList(productionCountries, ProductionCountry.CREATOR);
    productionCompanies = new ArrayList<>();
    in.readTypedList(productionCompanies, ProductionCompany.CREATOR);
    overview = in.readString();
    title = in.readString();
    adult = in.readInt() == 1;
    posterPath = in.readString();
    runtime = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<TMDbMovie> CREATOR = new Parcelable.Creator<TMDbMovie>() {
    public TMDbMovie createFromParcel(Parcel in) {
      return new TMDbMovie(in);
    }

    public TMDbMovie[] newArray(int size) {
      return new TMDbMovie[size];
    }
  };

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(tagline);
    dest.writeString(backdropPath);
    dest.writeString(status);
    dest.writeTypedList(genres);
    dest.writeString(releaseDate);
    dest.writeDouble(voteAverage);
    dest.writeInt(voteCount);
    dest.writeInt(budget);
    dest.writeDouble(popularity);
    dest.writeString(imdbId);
    dest.writeTypedList(spokenLanguages);
    dest.writeInt(revenue);
    dest.writeString(originalTitle);
    dest.writeParcelable(belongsToCollection, flags);
    dest.writeLong(id);
    dest.writeString(homepage);
    dest.writeTypedList(productionCountries);
    dest.writeTypedList(productionCompanies);
    dest.writeString(overview);
    dest.writeString(title);
    dest.writeInt(adult ? 1 : 0);
    dest.writeString(posterPath);
    dest.writeInt(runtime);
  }

  @Override
  public String toString() {
    return "tagline = "
        + tagline
        + ", backdropPath = "
        + backdropPath
        + ", status = "
        + status
        + ", genres = "
        + genres
        + ", releaseDate = "
        + releaseDate
        + ", voteAverage = "
        + voteAverage
        + ", voteCount = "
        + voteCount
        + ", budget = "
        + budget
        + ", popularity = "
        + popularity
        + ", imdbId = "
        + imdbId
        + ", spokenLanguages = "
        + spokenLanguages
        + ", revenue = "
        + revenue
        + ", originalTitle = "
        + originalTitle
        + ", belongsToCollection = "
        + belongsToCollection
        + ", id = "
        + id
        + ", homepage = "
        + homepage
        + ", productionCountries = "
        + productionCountries
        + ", productionCompanies = "
        + productionCompanies
        + ", overview = "
        + overview
        + ", title = "
        + title
        + ", adult = "
        + adult
        + ", posterPath = "
        + posterPath
        + ", runtime = "
        + runtime;
  }
}