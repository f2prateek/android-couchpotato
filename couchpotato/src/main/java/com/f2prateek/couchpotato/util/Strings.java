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

import java.util.List;

public final class Strings {
  private Strings() {
    throw new AssertionError("No instances");
  }

  public static boolean isBlank(CharSequence string) {
    return (string == null || string.toString().trim().length() == 0);
  }

  public static String valueOrDefault(String string, String defaultString) {
    return isBlank(string) ? defaultString : string;
  }

  public static String truncateAt(String string, int length) {
    return string.length() > length ? string.substring(0, length) : string;
  }

  public static String join(String token, List<? extends Displayable> list) {
    if (list.size() == 1) {
      return String.valueOf(list.get(0));
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0, size = list.size(); i < size; i++) {
      stringBuilder.append(list.get(i).displayText());
      if (i != size - 1) {
        stringBuilder.append(token);
      }
    }
    return stringBuilder.toString();
  }

  public interface Displayable {
    String displayText();
  }
}
