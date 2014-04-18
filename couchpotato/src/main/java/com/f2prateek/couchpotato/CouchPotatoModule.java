package com.f2prateek.couchpotato;

import android.app.Application;
import com.f2prateek.couchpotato.data.DataModule;
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
}
