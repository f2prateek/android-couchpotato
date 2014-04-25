package com.f2prateek.couchpotato.data.api.couchpotato.model;

import com.google.gson.annotations.SerializedName;

public class ApiKeyResponse {

  private static final String FIELD_API_KEY = "api_key";
  private static final String FIELD_SUCCESS = "success";

  @SerializedName(FIELD_API_KEY)
  private String apiKey;
  @SerializedName(FIELD_SUCCESS)
  private boolean success;

  public String getApiKey() {
    return apiKey;
  }

  public boolean isSuccess() {
    return success;
  }

  @Override public String toString() {
    return "ApiKeyResponse{" + "apiKey='" + apiKey + '\'' + ", success=" + success + '}';
  }
}
