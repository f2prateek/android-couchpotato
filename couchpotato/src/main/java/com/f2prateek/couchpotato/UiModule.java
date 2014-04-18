package com.f2prateek.couchpotato;

import com.f2prateek.couchpotato.ui.ActivityHierarchyServer;
import com.f2prateek.couchpotato.ui.AppContainer;
import com.f2prateek.couchpotato.ui.DiscoverGalleryView;
import com.f2prateek.couchpotato.ui.MainActivity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        MainActivity.class, DiscoverGalleryView.class
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
