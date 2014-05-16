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
import android.content.Context;
import com.f2prateek.couchpotato.ui.ActivityHierarchyServer;
import com.f2prateek.ln.Ln;
import com.f2prateek.ln.LnInterface;
import dagger.ObjectGraph;
import hugo.weaving.DebugLog;
import javax.inject.Inject;

public class CouchPotatoApplication extends Application {
  @Inject ActivityHierarchyServer activityHierarchyServer;
  @Inject LnInterface lnInterface;

  private ObjectGraph applicationGraph;

  @Override
  public void onCreate() {
    super.onCreate();

    buildApplicationGraphAndInject();
    registerActivityLifecycleCallbacks(activityHierarchyServer);
    Ln.set(lnInterface);
  }

  @DebugLog
  private void buildApplicationGraphAndInject() {
    applicationGraph = ObjectGraph.create(Modules.list(this));
    applicationGraph.inject(this);
  }

  @DebugLog
  public void inject(Object o) {
    applicationGraph.inject(o);
  }

  public ObjectGraph getApplicationGraph() {
    return applicationGraph;
  }

  public static CouchPotatoApplication get(Context context) {
    return (CouchPotatoApplication) context.getApplicationContext();
  }
}
