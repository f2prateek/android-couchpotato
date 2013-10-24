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

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import static org.fest.assertions.api.ANDROID.assertThat;

public abstract class ActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

  protected Instrumentation instrumentation;
  protected T activity;

  public ActivityTest(Class<T> activityClass) {
    super(activityClass);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    instrumentation = getInstrumentation();
    activity = getActivity();
  }

  @SmallTest
  public void testActivityExists() throws Exception {
    assertThat(activity).isNotNull();
  }
}