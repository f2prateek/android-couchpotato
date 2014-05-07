/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.data.api.couchpotato.model.movie;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Release {
  private static final String QUALITY_ID = "quality_id";
  private static final String STATUS_ID = "status_id";
  private static final String LAST_EDI = "last_edit";

  public ArrayList<File> files;
  @SerializedName(QUALITY_ID)
  public int qualityId;
  @SerializedName(STATUS_ID)
  public int statusId;
  @SerializedName(LAST_EDI)
  public long lastEdit;
  public String identifier;
  public long id;
}
