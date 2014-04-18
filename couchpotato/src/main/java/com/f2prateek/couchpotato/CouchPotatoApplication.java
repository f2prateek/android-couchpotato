package com.f2prateek.couchpotato;

import android.app.Application;
import android.content.Context;
import com.f2prateek.couchpotato.ui.ActivityHierarchyServer;
import com.f2prateek.ln.DebugLn;
import com.f2prateek.ln.Ln;
import dagger.ObjectGraph;
import hugo.weaving.DebugLog;
import javax.inject.Inject;

public class CouchPotatoApplication extends Application {

  @Inject ActivityHierarchyServer activityHierarchyServer;

  private ObjectGraph applicationGraph;

  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Ln.set(DebugLn.from(this));
    }

    buildApplicationGraphAndInject();
    registerActivityLifecycleCallbacks(activityHierarchyServer);
  }

  @DebugLog
  private void buildApplicationGraphAndInject() {
    applicationGraph = ObjectGraph.create(Modules.list(this));
    applicationGraph.inject(this);
  }

  public void inject(Object o) {
    applicationGraph.inject(o);
  }

  public static CouchPotatoApplication get(Context context) {
    return (CouchPotatoApplication) context.getApplicationContext();
  }
}
