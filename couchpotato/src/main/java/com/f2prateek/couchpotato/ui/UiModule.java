package com.f2prateek.couchpotato.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        MainActivity.class, ExploreMoviesView.class, NavigationDrawer.class
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
