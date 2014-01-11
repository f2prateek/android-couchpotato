/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato;

import android.app.Activity;
import android.content.Context;
import com.f2prateek.couchpotato.ui.activities.BaseActivity;
import com.f2prateek.couchpotato.ui.activities.BaseAuthenticatedActivity;
import com.f2prateek.couchpotato.ui.activities.MainActivity;
import com.f2prateek.couchpotato.ui.activities.ServerSetupActivity;
import com.f2prateek.couchpotato.ui.activities.ViewMovieActivity;
import com.f2prateek.couchpotato.ui.fragments.BaseFragment;
import com.f2prateek.couchpotato.ui.fragments.BaseProgressFragment;
import com.f2prateek.couchpotato.ui.fragments.BaseProgressGridFragment;
import com.f2prateek.couchpotato.ui.fragments.DetailedMovieGridFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieCastFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieCrewFragment;
import com.f2prateek.couchpotato.ui.fragments.MovieInfoFragment;
import com.f2prateek.couchpotato.ui.fragments.SimpleMovieGridFragment;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        // Activity
        BaseActivity.class, BaseAuthenticatedActivity.class, MainActivity.class,
        ViewMovieActivity.class,
        // Fragments
        BaseFragment.class, BaseProgressFragment.class, BaseProgressGridFragment.class,
        DetailedMovieGridFragment.class, SimpleMovieGridFragment.class, ServerSetupActivity.class,
        MovieCastFragment.class, MovieCrewFragment.class, MovieInfoFragment.class
    },
    addsTo = CouchPotatoApplicationModule.class)
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @Singleton @ForActivity Context provideActivityContext() {
    return activity;
  }
}