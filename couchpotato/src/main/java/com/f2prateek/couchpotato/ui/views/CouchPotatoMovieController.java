/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
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

package com.f2prateek.couchpotato.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoDatabase;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoEndpoint;
import com.f2prateek.couchpotato.data.api.couchpotato.model.profile.Profile;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.ln.Ln;
import java.util.List;
import javax.inject.Inject;

public class CouchPotatoMovieController extends LinearLayout {
  @Inject CouchPotatoEndpoint endpoint;
  @Inject CouchPotatoDatabase database;

  public CouchPotatoMovieController(Context context, AttributeSet attrs) {
    super(context, attrs);
    CouchPotatoApplication.get(context).inject(this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  @OnClick(R.id.add) public void add() {
    if (endpoint.isSet()) {
      database.getProfiles(new EndlessObserver<List<Profile>>() {
        @Override public void onNext(List<Profile> profiles) {
          Ln.d(profiles);
        }
      });
    }
  }
}
