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

package com.f2prateek.couchpotato.ui;

import android.view.View;
import butterknife.ButterKnife;
import java.util.List;

public class ButterKnives {
  private ButterKnives() {
    // no instances
  }

  private static final ButterKnife.Action<View> HIDE = new ButterKnife.Action<View>() {
    @Override public void apply(View view, int index) {
      view.setVisibility(View.GONE);
    }
  };

  public static void hide(List<View> viewList) {
    ButterKnife.apply(viewList, HIDE);
  }
}
