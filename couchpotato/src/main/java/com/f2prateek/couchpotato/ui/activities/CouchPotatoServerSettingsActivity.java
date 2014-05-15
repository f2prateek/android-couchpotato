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

package com.f2prateek.couchpotato.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoDatabase;
import com.f2prateek.couchpotato.data.api.couchpotato.CouchPotatoEndpoint;
import com.f2prateek.couchpotato.data.api.couchpotato.model.ApiKeyResponse;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.util.Strings;
import com.f2prateek.ln.Ln;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;
import java.util.regex.Pattern;
import javax.inject.Inject;

public class CouchPotatoServerSettingsActivity extends BaseActivity {
  private static final String DEFAULT_HOST_SCHEME = "http://";
  private static final Pattern HOST_PATTERN = Pattern.compile(
      "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)"
          + "*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]"
          + "*[A-Za-z0-9])$"
  );
  private static final Pattern IP_PATTERN = Pattern.compile(
      "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.)"
          + "{3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$"
  );

  @InjectView(R.id.host) EditText host;
  @InjectView(R.id.username) EditText username;
  @InjectView(R.id.password) EditText password;
  @InjectView(R.id.progress) ProgressBar progressBar;

  @Inject CouchPotatoEndpoint endpoint;
  @Inject CouchPotatoDatabase couchPotatoDatabase;

  private String oldHost;
  private String oldApiKey;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    inflateLayout(R.layout.activity_couchpotato_server_settings);

    if (Strings.isBlank(endpoint.getHost())) {
      host.setText(DEFAULT_HOST_SCHEME);
    } else {
      host.setText(endpoint.getHost());
    }

    password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @DebugLog @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // if (actionId == R.id.action_login)
        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
          login();
          return true;
        }
        return false;
      }
    });
  }

  @OnClick(R.id.login) public void login() {
    boolean hasError = false;

    // copy the current url
    if (endpoint.isSet()) {
      oldApiKey = endpoint.getApiKey();
      oldHost = endpoint.getHost();
    }

    if (Strings.isBlank(host.getText())) {
      host.setError(getString(R.string.required));
      hasError = true;
    } else {
      String hostText = getText(host);
      int start = hostText.indexOf("://");
      if (start == -1) {
        host.setError(getString(R.string.invalid));
        hasError = true;
      } else {
        int portStart = hostText.indexOf(":", start + 3);
        if (portStart != -1) {
          // port is specified, grab the string ://....:
          String sub = hostText.substring(start + 3, portStart);
          // check if it matches an ip address, hosts with ports are disallowed
          if (!IP_PATTERN.matcher(sub).matches()) {
            host.setError(getString(R.string.invalid));
            hasError = true;
          }
        } else {
          // grab the string ://....
          String sub = hostText.substring(start + 3);
          // check if it matches an ip address or host
          if (!IP_PATTERN.matcher(sub).matches() && !HOST_PATTERN.matcher(sub).matches()) {
            host.setError(getString(R.string.invalid));
            hasError = true;
          }
        }
      }
    }

    // Username is not required (server could be unprotected)
    boolean hasUsername = !Strings.isBlank(username.getText());
    // However if username is provided, password must be provided as well.
    if (hasUsername) {
      if (Strings.isBlank(password.getText())) {
        password.setError(getString(R.string.required));
        hasError = true;
      } else {
        password.setError(null);
      }
    }

    // todo : sanitize input
    if (!hasError) {
      endpoint.setHost(getText(host));
      endpoint.setApiKey(null);
      progressBar.setVisibility(View.VISIBLE);
      couchPotatoDatabase.getApiKey(getText(username), getText(password),
          new EndlessObserver<ApiKeyResponse>() {
            @Override public void onNext(ApiKeyResponse apiKeyResponse) {
              if (apiKeyResponse.isSuccess()) {
                endpoint.setApiKey(apiKeyResponse.getApiKey());
                Crouton.makeText(CouchPotatoServerSettingsActivity.this, R.string.login_successfull,
                    Style.CONFIRM).show();

                progressBar.postDelayed(new Runnable() {
                  @Override public void run() {
                    progressBar.setVisibility(View.GONE);
                    Intent intent =
                        new Intent(CouchPotatoServerSettingsActivity.this, MainActivity.class);
                    intent.setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                  }
                }, 1000);
              } else {
                // Credentials were rejected
                Crouton.makeText(CouchPotatoServerSettingsActivity.this,
                    R.string.invalid_credentials, Style.ALERT).show();
                loginError();
              }
            }

            @Override public void onError(Throwable throwable) {
              super.onError(throwable);

              // Could not reach the server, could be due to no network connectivity, or due to
              // an invalid url
              final String url = endpoint.getUrl();
              Crouton.makeText(CouchPotatoServerSettingsActivity.this,
                  getString(R.string.inaccessible_server, url), Style.ALERT)
                  .setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                      final Intent intent = new Intent(Intent.ACTION_VIEW);
                      intent.setData(Uri.parse(url));
                      startActivity(intent);
                    }
                  })
                  .show();
              loginError();
            }
          }
      );
    } else {
      // Some fields were invalid
      Crouton.makeText(CouchPotatoServerSettingsActivity.this, R.string.invalid_fields, Style.ALERT)
          .show();
      loginError();
    }
  }

  private void loginError() {
    Ln.d("Error while trying to login");
    endpoint.clear();
    if (oldHost != null && oldApiKey != null) {
      Ln.d("Restoring old url");
      endpoint.setHost(oldHost);
      endpoint.setApiKey(oldApiKey);
    }
    progressBar.setVisibility(View.GONE);
  }

  private static String getText(EditText editText) {
    return editText.getText().toString();
  }
}
