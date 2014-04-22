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

package com.f2prateek.couchpotato;

import android.app.Application;
import com.f2prateek.couchpotato.data.DataModule;
import com.f2prateek.couchpotato.ui.UiModule;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    includes = {
        UiModule.class, DataModule.class
    },
    injects = {
        CouchPotatoApplication.class
    })
public final class CouchPotatoModule {
  private final CouchPotatoApplication app;

  public CouchPotatoModule(CouchPotatoApplication app) {
    this.app = app;
  }

  @Provides @Singleton Application provideApplication() {
    return app;
  }

  @Provides @Singleton Bus provideBus() {
    return new Bus();
  }
}
