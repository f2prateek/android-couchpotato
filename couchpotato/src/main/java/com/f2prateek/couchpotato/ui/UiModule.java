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

package com.f2prateek.couchpotato.ui;

import com.f2prateek.couchpotato.ui.fragments.BaseFragment;
import com.f2prateek.couchpotato.ui.fragments.ExploreMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.MoviesGridFragment;
import com.f2prateek.couchpotato.ui.fragments.PopularMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.TopRatedMoviesFragment;
import com.f2prateek.couchpotato.ui.fragments.WantedMoviesFragment;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        MainActivity.class, BaseFragment.class, PopularMoviesFragment.class,
        MoviesGridFragment.class, ExploreMoviesFragment.class, TopRatedMoviesFragment.class,
        WantedMoviesFragment.class, MovieActivity.class, CouchPotatoServerSettingsActivity.class
    },
    complete = false,
    library = true)
public class UiModule {
  @Provides @Singleton AppContainer provideAppContainer() {
    return AppContainer.DEFAULT;
  }

  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }
}
