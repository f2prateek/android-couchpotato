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

package com.f2prateek.couchpotato.data.api.couchpotato;

import com.f2prateek.couchpotato.data.api.couchpotato.model.ApiKeyResponse;
import com.f2prateek.couchpotato.data.api.couchpotato.model.UpdaterInfo;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.f2prateek.couchpotato.data.Util.md5;

public class CouchPotatoDatabase {
  private final CouchPotatoService couchPotatoService;

  public CouchPotatoDatabase(CouchPotatoService couchPotatoService) {
    this.couchPotatoService = couchPotatoService;
  }

  public Subscription getUpdaterInfo(final Observer<UpdaterInfo> observer) {
    return couchPotatoService.updaterInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public Subscription getApiKey(final String username, final String password,
      final Observer<ApiKeyResponse> observer) {
    return couchPotatoService.apiKey(md5(password), md5(username))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }
}