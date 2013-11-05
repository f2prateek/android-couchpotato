/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.services;

import com.f2prateek.couchpotato.util.Ln;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FilePreference<T> {
  private final GsonConverter<T> gsonConverter;
  private final File file;

  public FilePreference(Gson gson, File directory, Class<T> typeParameterClass) {
    gsonConverter = new GsonConverter(gson, typeParameterClass);
    file = new File(directory, typeParameterClass.getCanonicalName());
  }

  public boolean save(T data) {
    try {
      gsonConverter.toStream(data, new FileOutputStream(file));
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public T get() {
    try {
      return (T) gsonConverter.from(new FileReader(file));
    } catch (FileNotFoundException e) {
      Ln.e("File %s doesn't exist.", file.toString());
      e.printStackTrace();
    }
    return null;
  }
}
