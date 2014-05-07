package com.f2prateek.couchpotato.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import javax.inject.Inject;

public class CouchPotatoLoginActivity extends BaseActivity {
  private static final String DEFAULT_HOST_SCHEME = "http://";
  @InjectView(R.id.host) EditText host;
  @InjectView(R.id.username) EditText username;
  @InjectView(R.id.password) EditText password;
  @InjectView(R.id.progress) ProgressBar progressBar;

  @Inject CouchPotatoEndpoint endpoint;
  @Inject CouchPotatoDatabase couchPotatoDatabase;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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

    // Inflate a "Done/Discard" custom action bar view.
    LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
        .getSystemService(LAYOUT_INFLATER_SERVICE);
    final View customActionBarView =
        inflater.inflate(R.layout.actionbar_custom_view_done_discard, null);
    // Show the custom action bar view and hide the normal Home icon and title.
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
        ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    actionBar.setCustomView(customActionBarView,
        new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
    );
  }

  @OnClick(R.id.actionbar_cancel) public void cancel() {
    finish();
  }

  @OnClick(R.id.actionbar_done) public void login() {
    boolean hasError = false;
    if (Strings.isBlank(host.getText())) {
      host.setError(getString(R.string.required));
      hasError = true;
    } else {
      host.setError(null);
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
                Intent intent = new Intent(CouchPotatoLoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
              } else {
                loginError(R.string.invalid_credentials);
              }
            }

            @Override public void onError(Throwable throwable) {
              super.onError(throwable);
              loginError(R.string.invalid_server);
            }
          }
      );
    } else {
      loginError(R.string.invalid_fields);
    }
  }

  private void loginError(int textResourceId) {
    endpoint.setApiKey(null);
    progressBar.setVisibility(View.GONE);
    Crouton.makeText(this, textResourceId, Style.ALERT).show();
  }

  private static String getText(EditText editText) {
    return editText.getText().toString();
  }
}
