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

package com.f2prateek.couchpotato.tests;

import android.test.suitebuilder.annotation.SmallTest;
import com.f2prateek.couchpotato.ui.activities.MainActivity;
import com.squareup.spoon.Spoon;

public class MainActivityTest extends ActivityTest<MainActivity> {

  public MainActivityTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @SmallTest
  public void testUI() {
    Spoon.screenshot(activity, "Initial_State");
  }
}
