/*
 * Copyright 2014 Prateek Srivastava
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
import com.f2prateek.couchpotato.data.DebugDataModule;
import com.f2prateek.couchpotato.ui.DebugUiModule;
import com.f2prateek.ln.DebugLn;
import com.f2prateek.ln.LnInterface;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    addsTo = CouchPotatoModule.class,
    includes = {
        DebugUiModule.class, DebugDataModule.class
    },
    overrides = true)
public final class DebugCouchPotatoModule {
  @Provides @Singleton LnInterface provideLnInterface(Application application) {
    return DebugLn.from(application);
  }
}
