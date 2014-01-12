package com.f2prateek.couchpotato;

import com.crashlytics.android.Crashlytics;
import com.f2prateek.ln.DebugLn;

public class CrashlyticsLn extends DebugLn {

  public CrashlyticsLn(String packageName, int minimumLogLevel) {
    super(packageName, minimumLogLevel);
  }

  @Override protected void println(int priority, String msg) {
    Crashlytics.log(msg);
  }
}
