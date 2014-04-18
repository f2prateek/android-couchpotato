package com.f2prateek.couchpotato.data.api.moviedb.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DiscoverMoviesResponse {
  public int page;

  @SerializedName("total_pages")
  public long pageCount;

  @SerializedName("total_results")
  public long resultCount;

  public List<TMDbMovieMinified> results;
}
