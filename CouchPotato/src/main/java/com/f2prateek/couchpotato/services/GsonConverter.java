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

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * Use GSON to serialize classes to a bytes.
 * <p/>
 * Note: This will only work when concrete classes are specified for {@code T}. If you want to
 * specify an interface for {@code T} then you need to also include the concrete class name in the
 * serialized byte array so that you can deserialize to the appropriate type.
 */
public class GsonConverter<T> {
  private final Gson gson;
  private final Class<T> type;

  public GsonConverter(Gson gson, Class<T> type) {
    this.gson = gson;
    this.type = type;
  }

  public T from(Reader reader) {
    return gson.fromJson(reader, type);
  }

  public void toStream(T object, OutputStream outputStream) throws IOException {
    Writer writer = new OutputStreamWriter(outputStream);
    gson.toJson(object, writer);
    writer.close();
  }
}
