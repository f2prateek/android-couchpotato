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

package com.f2prateek.couchpotato.ui.misc;

import android.content.Context;
import java.util.Collections;
import java.util.List;

/**
 * A {@link BindableAdapter} that displays a {@link java.util.List
 * Assumption is that the
 */
public abstract class BindableListAdapter<T> extends BindableAdapter<T> {
  private List<T> list = Collections.emptyList();

  public BindableListAdapter(Context context) {
    super(context);
  }

  public void replaceWith(List<T> list) {
    this.list = list;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return list.size();
  }

  @Override public T getItem(int position) {
    return list.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }
}
