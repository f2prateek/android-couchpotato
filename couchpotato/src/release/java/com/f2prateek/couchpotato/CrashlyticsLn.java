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
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.f2prateek.ln.DebugLn;

public class CrashlyticsLn extends DebugLn {
  private CrashlyticsLn(String packageName) {
    super(packageName, Log.VERBOSE);
  }

  public static CrashlyticsLn from(Application application) {
    Crashlytics.start(application);
    return new CrashlyticsLn(application.getPackageName());
  }

  @Override protected void println(int priority, String msg) {
    Crashlytics.log(msg);
  }
}
