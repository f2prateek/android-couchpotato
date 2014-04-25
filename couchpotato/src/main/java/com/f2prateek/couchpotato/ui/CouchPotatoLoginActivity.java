package com.f2prateek.couchpotato.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.EditText;
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
import javax.inject.Inject;

public class CouchPotatoLoginActivity extends BaseActivity {
  private static final String DEFAULT_HOST_SCHEME = "http://";
  @InjectView(R.id.host) EditText host;
  @InjectView(R.id.username) EditText username;
  @InjectView(R.id.password) EditText password;

  @Inject CouchPotatoEndpoint endpoint;
  @Inject CouchPotatoDatabase couchPotatoDatabase;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Ln.d(endpoint);

    if (Strings.isBlank(endpoint.getHost())) {
      host.setText(DEFAULT_HOST_SCHEME);
    } else {
      host.setText(endpoint.getHost());
    }

    password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.action_login) {
          login();
          return true;
        }
        return false;
      }
    });
  }

  @Override protected void inflateLayout(ViewGroup container) {
    getLayoutInflater().inflate(R.layout.activity_couchpotato_login, container);
  }

  @OnClick(R.id.login) public void login() {
    boolean hasError = false;
    if (Strings.isBlank(host.getText())) {
      host.setError(getString(R.string.required));
      hasError = true;
    } else {
      host.setError(null);
    }

    // Username is not required (server could be unprotected)
    boolean hasUsername = true;
    if (Strings.isBlank(username.getText())) {
      hasUsername = false;
    }
    // However if username is provided, password must be provided as well.
    if (hasUsername) {
      if (Strings.isBlank(password.getText())) {
        password.setError(getString(R.string.required));
        hasError = true;
      } else {
        password.setError(null);
      }
    }

    if (!hasError) {
      endpoint.setHost(getText(host));
      endpoint.setApiKey(null);
      couchPotatoDatabase.getApiKey(getText(username), getText(password),
          new EndlessObserver<ApiKeyResponse>() {
            @Override public void onNext(ApiKeyResponse apiKeyResponse) {
              if (apiKeyResponse.isSuccess()) {
                endpoint.setApiKey(apiKeyResponse.getApiKey());
              } else {
                showLoginError();
              }
            }

            @Override public void onError(Throwable throwable) {
              super.onError(throwable);
              showLoginError();
            }
          }
      );
    } else {
      // Show a message to the user
      showLoginError();
    }
  }

  private void showLoginError() {
    Crouton.makeText(this, R.string.invalid_fields, Style.ALERT).show();
  }

  private static String getText(EditText editText) {
    return editText.getText().toString();
  }
}
