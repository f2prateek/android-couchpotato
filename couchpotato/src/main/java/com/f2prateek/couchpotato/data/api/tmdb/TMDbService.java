/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.data.api.tmdb;

import com.f2prateek.couchpotato.data.api.tmdb.model.Configuration;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieCollectionResponse;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface TMDbService {
  @GET("/movie/popular") Observable<MovieCollectionResponse> popular(@Query("page") int page);

  @GET("/configuration") Observable<Configuration> configuration();
}