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

package com.f2prateek.couchpotato.util;

import android.app.ActivityManager;
import android.content.Context;

import static android.content.Context.ACTIVITY_SERVICE;

public class Utils {
  private Utils() {
    throw new AssertionError("No instances");
  }

  public static int calculateMemoryCacheSize(Context context) {
    ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
    int memoryClass = am.getMemoryClass();
    // Target ~33% of the available heap.
    return 1024 * 1024 * memoryClass / 4;
  }
}
