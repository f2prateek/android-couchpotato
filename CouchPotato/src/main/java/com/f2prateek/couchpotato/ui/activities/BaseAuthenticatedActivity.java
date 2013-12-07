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

package com.f2prateek.couchpotato.ui.activities;

import android.os.Bundle;
import com.f2prateek.couchpotato.UserConfig;
import com.f2prateek.couchpotato.util.AccountUtils;
import com.f2prateek.couchpotato.util.Ln;
import javax.inject.Inject;

/**
 * An activity that requires the user to be authenticated.
 */
public class BaseAuthenticatedActivity extends BaseActivity {
  @Inject UserConfig userConfig;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (!AccountUtils.isAuthenticated(userConfig)) {
      Ln.d("exiting: user not authenticated");
      AccountUtils.startAuthenticationFlow(this);
      finish();
    }
  }
}
